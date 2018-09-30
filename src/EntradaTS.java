
public abstract class EntradaTS {
	
	protected boolean consolidado;
	
	public abstract String getNombre();
	
	public abstract void consolidar();

	public boolean isConsolidado() {
		return consolidado;
	}
}
