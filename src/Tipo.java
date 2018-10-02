
public abstract class Tipo {
	
	protected Token tokenNombre;
	protected boolean arreglo;	
	
	@Override
	public String toString(){
		if (arreglo) return ("ar_"+this.tokenNombre.getLexema());
		else return this.tokenNombre.getLexema();
	}
	
	public Token getToken(){
		return this.tokenNombre;
	}
	
	public void setArreglo(){
		this.arreglo=true;
	}
	
	public boolean isArreglo(){
		return this.arreglo;
	}
	
	public abstract boolean estaDefinido();

}
