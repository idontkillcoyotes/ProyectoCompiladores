
public class NodoVar extends NodoAcceso{
	
	private Token id;
	private EParametro var;
	private boolean esatributo;
	
	public NodoVar(Token id) {
		this.id = id;
		this.var=null;
		this.esatributo=false;
	}
	
	public Token getToken() {
		return id;
	}
	
	
	public EParametro getVar() {
		return var;
	}

	@Override
	public void setValorAtributo(boolean b) {
		this.valorAtributo=b;
		if (this.encadenado!=null) this.encadenado.setValorAtributo(b);
	}

	@Override
	public Tipo check() throws SemanticException {
		if(!valorAtributo){
			EParametro v = Utl.ts.getMiembroAct().getVarLocal(id.getLexema());
			if(v==null){
				if(!Utl.ts.getMiembroAct().esEstatico()){
					v=Utl.ts.getClaseAct().getAtributoVisible(id.getLexema());
					if (v==null){
						throw new SemanticException(id.getNroLinea(),id.getNroColumna(),
								"Variable desconocida.\nLa variable '"+id.getLexema()+"' no ha sido declarada o no "
										+ "es visible desde este bloque.");
					}
					//aca ya se que la var es un atributo
					this.esatributo=true;
				}
				else{
					throw new SemanticException(id.getNroLinea(),id.getNroColumna(),
							"Acceso invalido.\nNo es posible acceder a un atributo de instancia desde"
									+ " un metodo estatico.");
				}
			}
			Tipo tvar=v.getTipo();
			//guardo la variable
			this.var=v;
			if(this.encadenado!=null){
				return this.encadenado.check(tvar,id);
			}
			else{
				return tvar;
			}
		}
		else{
			//TODO podria ver si el atributo usado fue declarado antes para saber si es legal o no
			throw new SemanticException(id.getNroLinea(),id.getNroColumna(),"Acceso invalido.\n"
					+ "No es posible acceder a un atributo o variable local en la inicializacion"
					+ " de un atributo de clase.");
		}
	}
	@Override
	public String toString(){
		String s=id.getLexema();
		if(encadenado!=null){
			s+=encadenado.toString();
		}
		return s;
	}

	@Override
	public void generar() {
		if((this.ladoizq)&&(this.encadenado==null)){
			//si estoy en un lado izquierdo y no tengo encadenado			
			if(this.esatributo){
				//si var es un atributo
				Utl.gen("load 3\t\t\t;cargo this (nodovar)");
				Utl.gen("swap\t\t\t;(nodovar)");
				Utl.gen("storeref "+var.getOffset()+"\t\t\t;guardo en cir con offset (nodovar)");
			}
			else{
				//si var es un parametro o var local
				Utl.gen("store "+var.getOffset()+"\t\t\t;guardo var local o param (nodovar)");
			}
		}
		else{
			if(this.esatributo){
				Utl.gen("load 3\t\t\t;cargo this (nodovar)");
				Utl.gen("loadref "+var.getOffset()+"\t\t\t;cargo var desde el cir (nodovar)"); 
			}
			else{
				Utl.gen("load "+var.getOffset()+"\t\t\t;cargo var local o param (nodovar)");
			}
			
		}
		if(this.encadenado!=null) this.encadenado.generar();
	}


}
