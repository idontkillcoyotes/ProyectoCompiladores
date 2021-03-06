import java.util.ArrayList;

public class NodoDeclaracionVars extends NodoSentencia{
	
	private ArrayList<EParametro> varlocales;
	private NodoExpresion valor;
	private Tipo tipo;
	private boolean sentenciaunica;
	
	public NodoDeclaracionVars(Tipo t,ArrayList<EParametro> varlocales) {
		this.varlocales = varlocales;
		this.tipo=t;
		this.valor=null;
		this.sentenciaunica=false;
	}	
	public void setSentenciaUnica(boolean b){
		this.sentenciaunica=b;
	}
		
	public NodoExpresion getValor() {
		return valor;
	}
	public void setValor(NodoExpresion valor) {
		this.valor = valor;
	}
	
	public Tipo getTipo(){
		return this.tipo;
	}

	public ArrayList<EParametro> getVarlocales() {
		return varlocales;
	}

	@Override
	public void check() throws SemanticException{		
		//si tiene expresion para inicializar el valor
		if (this.tipo.estaDefinido()){
			if(valor!=null){
				//chequeo expresion y obtengo su tipo
				Tipo exp=valor.check();
				//si los tipos son incompatibles hay error
				if(!exp.esCompatible(tipo))
					throw new SemanticException(valor.getToken().getNroLinea(),valor.getToken().getNroColumna(),
							"Tipos incompatibles.\n"+exp.toString()+" es incompatible con "+tipo.toString());
			}
			if(!sentenciaunica){
				//si no es sentencia unica agrego las variables
				for(EParametro var: varlocales){
					//intento agregar la variable local a las variables locales del miembro actual
					if (!Utl.ts.getMiembroAct().pushVarLocal(var)){
						//si hay conflictos de nombre lanzo error
						throw new SemanticException(var.getToken().getNroLinea(),var.getToken().getNroColumna(),
								"Variable duplicada.\nLa variable '"+var.getNombre()+"' ya est� en uso.");
					}
					else{
						//si no hay errores la agrego a las variables locales del bloque actual				
						Utl.ts.getBloqueAct().addVarLocal(var);
					}
				}
			}
			//seteo al contador con la cantidad de vars locales del miembro 
			int i=Utl.ts.getMiembroAct().getCantVarLoc()+1;
			for (EParametro e: varlocales){
				//empiezo a setear offset con (1 - (la cantidad de vars locales del miembro + 1) )
				e.setOffset(1-i);
				i++;
			}
			//sumo la cantidad de variables locales de este nodo al miembro 
			Utl.ts.getMiembroAct().addVarLoc(this.varlocales.size());
			
			//si es sentencia unica, me basta con chequear el valor 
		}
		else{
			throw new SemanticException(tipo.getToken().getNroLinea(),tipo.getToken().getNroColumna(),
					"Clase no definida.\nLa clase "+tipo.getTipo()+" no esta definida.");
		}	
	}
	
	@Override
	public String toString(){
		String s="";
		for(EParametro var: this.varlocales){
			s+=var.getTipo().getTipo()+" "+var.getOffset()+" "+var.getNombre();
			s+=",";
		}
		if(this.valor!=null){
			s+="= "+this.valor.toString();
		}
		s+="\n";
		return s;
	}
	@Override
	public boolean tieneRetorno() {
		return false;
	}
	@Override
	public void generar() {
		//si hay un valor debo inicializar las variables		
		if (this.valor!=null){
			//genero valor
			this.valor.generar();
			//para cada variable debo asignar el valor que quedo en el tope de la pila
			for(int i=1;i<this.varlocales.size();i++){
				//genero n-1 dups con n=cant de vars
				Utl.gen("dup\t\t\t;duplico valor para no perderlo (nododecvars)");
			}
			for(EParametro e:this.varlocales){
				Utl.gen("store "+(e.getOffset())+"\t\t\t;guardo valor en var local (nododecvars)");		
			}
		}
		Utl.gen("nop\t\t\t;(nododecvars)");
	}
	

}
