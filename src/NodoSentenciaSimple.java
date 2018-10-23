
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
	public void check() throws SemanticException{
		expresion.check();
	}
	
	@Override
	public String toString(){
		return this.expresion.toString()+"\n"; 
	}
}
