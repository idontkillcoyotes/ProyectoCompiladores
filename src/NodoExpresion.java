
public abstract class NodoExpresion {
	
	protected boolean valorAtributo;
	
	public abstract Tipo check() throws SemanticException;

	public void setValorAtributo(boolean b) {
		this.valorAtributo=b;		
	}
	
	public boolean isValorAtributo(){
		return this.valorAtributo;
	}

}
