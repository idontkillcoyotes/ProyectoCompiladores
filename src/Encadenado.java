
public abstract class Encadenado {
	
	protected Encadenado encadenado;
	protected boolean valorAtributo;
	protected boolean ladoIzquierdo;
	
	public abstract Tipo check(Tipo t,Token id) throws SemanticException;
	
	public abstract Token getToken(Token t);

	public void setEncadenado(Encadenado encadenado) {
		this.encadenado = encadenado;
	}

	public Encadenado getEncadenado() {
		return encadenado;
	}

	public boolean hasLadoizq() {
		return ladoIzquierdo;
	}

	public void setLadoizq(boolean b) {
		this.ladoIzquierdo = b;
	}

	public boolean isValorAtributo() {
		return valorAtributo;
	}

	public void setValorAtributo(boolean b) {
		this.valorAtributo = b;
		if (this.encadenado!=null) this.encadenado.setValorAtributo(b);
	}

}
