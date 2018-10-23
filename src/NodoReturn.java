
public class NodoReturn extends NodoSentencia{
	
	private Token token;
	private NodoExpresion expresion;
	
	public NodoReturn(Token t) {
		this.token=t;
	}
	
	public Token getToken() {
		return token;
	}
	
	public NodoExpresion getExpresion() {
		return expresion;
	}
	
	public void setExpresion(NodoExpresion expresion) {
		this.expresion = expresion;
	}

	@Override
	public void check() {		
	}
	
	@Override
	public String toString(){
		String s="return "+expresion.toString()+"\n";
		return s;
	}

}
