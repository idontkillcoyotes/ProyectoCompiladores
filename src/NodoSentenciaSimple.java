
public class NodoSentenciaSimple extends NodoSentencia{
	
	private NodoExpresion expresion;


	public NodoSentenciaSimple() {
		this.expresion = null;
	}
	
	public void setExpresion(NodoExpresion expresion) {
		this.expresion = expresion;
	}

	public NodoExpresion getExpresion() {
		return expresion;
	}
	@Override
	public boolean tieneRetorno() {
		return false;
	}

	@Override
	public void check() throws SemanticException{
		expresion.check();
	}
	
	@Override
	public String toString(){
		return this.expresion.toString()+"\n";
	}

	@Override
	public void generar() {
		//TODO ver si puedo mejorar esto y no usar el check de la expresion
		this.expresion.generar();		
		Tipo t;
		try {
			t=expresion.check();						
			if (!t.esTipo(Utl.TPC_VOID)) 
				Utl.gen("pop\t\t\t;hago pop del resultado (nodosentsimple)");
			
		} catch (SemanticException e) {}
		
	}
}
