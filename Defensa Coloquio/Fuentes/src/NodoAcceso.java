
public abstract class NodoAcceso extends NodoPrimario{
	
	protected boolean ladoizq;

	public boolean isLadoizq() {
		return ladoizq;
	}

	public void setLadoizq(boolean ladoizq) {
		this.ladoizq = ladoizq;
		if(this.encadenado!=null) this.encadenado.setLadoizq(ladoizq);
	}
}
