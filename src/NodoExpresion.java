
public abstract class NodoExpresion {
	
	protected boolean valorAtributo;
	protected boolean ladoIzquierdo;

	public abstract Token getToken();
	
	public abstract Tipo check() throws SemanticException;

	public abstract void setValorAtributo(boolean b);
	
	public boolean isValorAtributo(){
		return this.valorAtributo;
	}	
	
	public boolean isLadoIzquierdo() {
		return ladoIzquierdo;
	}

	public void setLadoIzquierdo(boolean b) {
		this.ladoIzquierdo = b;
	}

}
