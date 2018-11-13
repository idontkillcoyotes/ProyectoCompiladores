
public class NodoThis extends NodoAcceso{
	
	private EClase thisclase;
	private Token tk;

	public NodoThis(EClase thisclase,Token t) {
		this.tk=t;
		this.thisclase = thisclase;
	}

	public EClase getThisclase() {
		return thisclase;
	}
	
	@Override
	public void setValorAtributo(boolean b) {
		this.valorAtributo=b;
		if (this.encadenado!=null) this.encadenado.setValorAtributo(b);
	}

	@Override
	public Tipo check() throws SemanticException {
		if(!valorAtributo){
			if(!Utl.ts.getMiembroAct().esEstatico()){
				Tipo t=new TipoClase(thisclase.getToken(),thisclase);
				if(this.encadenado!=null){
					this.encadenado.setEnThis(true);
					return this.encadenado.check(t, tk);
				}
				else{
					//si no tiene encadenado y esta del lado izquierdo hay error
					if(ladoizq)
						throw new SemanticException(tk.getNroLinea(),tk.getNroColumna(),
							"Asignación invalida.\nNo es posible asignarle un valor a la variable this."); 
					else
						return t;
				}
			}
			else{
				throw new SemanticException(tk.getNroLinea(),tk.getNroColumna(),
						"Referencia invalida.\nNo es posible referenciar a una instancia de clase dentro de un metodo estatico.");
			}
		}
		else{
			throw new SemanticException(tk.getNroLinea(),tk.getNroColumna(),"Referencia invalida.\n"
					+ "No es posible referenciar a una instancia de clase para la inicializacion de un atributo.");
		}
	}
	@Override
	public String toString(){
		String s="this";
		if(encadenado!=null){
			s+=encadenado.toString();
		}
		return s;
	}

	@Override
	public Token getToken() {
		return tk;
	}

	@Override
	public void generar() {
		Utl.gen("load 3\t\t\t;cargo this (nodothis)");
		if (this.encadenado!=null) this.encadenado.generar();		
	}

}
