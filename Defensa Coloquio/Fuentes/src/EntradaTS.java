
public abstract class EntradaTS {
	
	protected boolean consolidado;
	
	public abstract String getNombre();
	
	public abstract void consolidar() throws SemanticException;
	
	public abstract void check() throws SemanticException;

	public boolean isConsolidado(){
		return consolidado;
	}
}
