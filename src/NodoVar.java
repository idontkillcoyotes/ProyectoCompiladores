
public class NodoVar extends NodoAcceso{
	
	private Token id;
	
	public NodoVar(Token id) {
		this.id = id;
	}
	
	public Token getToken() {
		return id;
	}
	
	@Override
	public void setValorAtributo(boolean b) {
		this.valorAtributo=b;
		if (this.encadenado!=null) this.encadenado.setValorAtributo(b);
	}

	@Override
	public Tipo check() throws SemanticException {
		if(!valorAtributo){
			EParametro var = Utl.ts.getMiembroAct().getVarLocal(id.getLexema());
			if(var==null){
				if(!Utl.ts.getMiembroAct().esEstatico()){
					var=Utl.ts.getClaseAct().getAtributoVisible(id.getLexema());
					if (var==null){
						throw new SemanticException(id.getNroLinea(),id.getNroColumna(),
								"Variable desconocida.\nLa variable '"+id.getLexema()+"' no ha sido declarada o no "
										+ "es visible desde este bloque.");
					}
				}
				else{
					throw new SemanticException(id.getNroLinea(),id.getNroColumna(),
							"Acceso invalido.\nNo es posible acceder a un atributo de instancia desde"
									+ " un metodo estatico.");
				}
			}
			Tipo tvar=var.getTipo();
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


}
