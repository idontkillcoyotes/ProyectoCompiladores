
public class NodoSentenciaSimple extends NodoSentencia{
	
	private NodoExpresion expresion;
	private Tipo texp;


	public NodoSentenciaSimple() {
		this.expresion = null;
		this.texp = null;
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
		this.texp=expresion.check();
	}
	
	@Override
	public String toString(){
		return this.expresion.toString()+"\n";
	}

	@Override
	public void generar() {
		this.expresion.generar();		
		if (!texp.esTipo(Utl.TPC_VOID))
				//si no es void debo hacer pop
				Utl.gen("pop\t\t\t;hago pop del resultado (nodosentsimple)");		
	}
}
