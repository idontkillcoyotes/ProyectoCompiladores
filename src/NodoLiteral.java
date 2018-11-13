
public abstract class NodoLiteral extends NodoOperando{
	
	protected Token literal;
	protected Token tktipo;

	public Token getToken() {
		return literal;
	}
	public Token getTokenTipo() {
		return tktipo;
	}
	
	public String getValor(){
		return this.literal.getLexema();
	}
	
	@Override
	public String toString(){
		return this.literal.getLexema();
	}
	
	
	
	@Override
	public void setValorAtributo(boolean b) {
		this.valorAtributo=b;
	}

}
