import java.util.List;

public class EAtributo extends EParametro{
	
	private Visibilidad visibilidad;
	
	public EAtributo(Token tn,String tipo){
		super(tn,tipo);	
	}
	
	public Visibilidad getVisibilidad() {
		return visibilidad;
	}
		
	public void setVisibilidad(Visibilidad vis){
		this.visibilidad=vis;
	}
	
	/*
	 * Redefino equals para que considere atributos iguales
	 * a los que tienen el mismo nombre 
	 * TODO nombre y tipo?
	 */
	public boolean equals(EAtributo a){
		return getNombre().equals(a.getNombre());
	}

}
