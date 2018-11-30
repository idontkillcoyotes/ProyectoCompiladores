
public abstract class NodoExpresion {
	
	protected boolean valorAtributo;

	public abstract Token getToken();
	
	public abstract void generar();
	
	public abstract Tipo check() throws SemanticException;

	public abstract void setValorAtributo(boolean b);
		
	public boolean isValorAtributo(){
		return this.valorAtributo;
	}	

}
