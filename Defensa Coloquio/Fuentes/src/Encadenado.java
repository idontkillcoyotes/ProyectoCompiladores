
public abstract class Encadenado {
	
	protected Encadenado encadenado;
	protected boolean valorAtributo;
	protected boolean ladoizq;
	protected boolean enthis;
	
	public abstract Tipo check(Tipo t,Token id) throws SemanticException;
	
	public abstract Token getToken(Token t);

	public boolean enThis() {
		return enthis;
	}

	public void setEnThis(boolean esThis) {
		this.enthis = esThis;
		if(this.encadenado!=null) this.encadenado.setEnThis(true);
	}

	public void setEncadenado(Encadenado encadenado) {
		this.encadenado = encadenado;
	}

	public Encadenado getEncadenado() {
		return encadenado;
	}

	public boolean hasLadoizq() {
		return ladoizq;
	}

	public void setLadoizq(boolean b) {
		this.ladoizq = b;
		if (this.encadenado!=null) encadenado.setLadoizq(b);
	}

	public boolean isValorAtributo() {
		return valorAtributo;
	}

	public void setValorAtributo(boolean b) {
		this.valorAtributo = b;
		if (this.encadenado!=null) this.encadenado.setValorAtributo(b);
	}

	public abstract void generar();

}
