
public abstract class NodoLiteral extends NodoOperando{
	
	protected Token literal;

	public Token getLiteral() {
		return literal;
	}
	@Override
	public String toString(){
		return this.literal.getLexema();
	}

}
