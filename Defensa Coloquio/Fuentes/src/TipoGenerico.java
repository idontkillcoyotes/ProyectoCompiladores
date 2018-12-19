
public abstract class TipoGenerico extends Tipo{
	
	protected boolean arreglo;	
	
	public void setArreglo(boolean b){
		this.arreglo=b;
	}
	
	public boolean isArreglo(){
		return this.arreglo;
	}
	
	@Override
	public boolean esTipo(int tipo) {
		return (this.tokenNombre.getTipo()==tipo);
	}
	
	@Override
	public String toString(){
		if (arreglo) return (this.getTipo()+"[]");
		else return this.getTipo();
	}
	
}
