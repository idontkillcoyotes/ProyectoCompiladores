
public abstract class Encadenado {
	
	protected Encadenado encadenado;
	protected boolean ladoizq;
	
	public abstract Tipo check(Tipo t) throws SemanticException;	

	public void setEncadenado(Encadenado encadenado) {
		this.encadenado = encadenado;
	}

	public void setLadoizq(boolean ladoizq) {
		this.ladoizq = ladoizq;
	}

	public Encadenado getEncadenado() {
		return encadenado;
	}

	public boolean hasLadoizq() {
		return ladoizq;
	}

}
