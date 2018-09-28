public class EAtributo extends EParametro{
	
	private Visibilidad visibilidad;
	
	public EAtributo(Token tn,String tipo){
		super(tn,tipo);	
	}
	public EAtributo(EParametro p,Visibilidad vis){
		super(p.getToken(),p.getTipo());
		this.visibilidad=vis;
	}
	
	public Visibilidad getVisibilidad() {
		return visibilidad;
	}	
	/*
	 * Redefino equals para que considere atributos iguales
	 * a los que tienen el mismo nombre 
	 * TODO nombre y tipo?
	 */
	public boolean equals(EAtributo a){
		return getNombre().equals(a.getNombre());
	}
	public String toString(){
		String s="\n";
		s+="Visibilidad: "+this.visibilidad+"\n";
		s+="Nombre:\t"+this.getNombre()+"\n";
		s+="Tipo:\t"+this.tipo+"\n";		
		return s;
	}

}
