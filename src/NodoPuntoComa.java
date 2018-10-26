
public class NodoPuntoComa extends NodoSentencia {
	
	private Token token;
	
	public NodoPuntoComa(Token t) {
		this.token=t;
	}
	public Token getToken() {
		return token;
	}
	
	@Override
	public void check() {
		return;
	}
	@Override
	public boolean tieneRetorno() {
		return false;
	}
	
	@Override
	public String toString(){
		return "";
	}

}
