
public abstract class Tipo {
	
	public String nombre;
	public boolean arreglo;
	
	//TODO No estoy seguro si voy a necesitar un metodo como este...
	public abstract boolean compatible(Tipo t);
	
	public String getTipo(){
		if (arreglo) return ("ar_"+this.nombre);
		else return this.nombre;
	}
	
	public void setArreglo(){
		this.arreglo=true;
	}
	
	public boolean isArreglo(){
		return this.arreglo;
	}

}
