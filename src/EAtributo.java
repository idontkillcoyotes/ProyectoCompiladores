public class EAtributo extends EParametro{
	
	private Visibilidad visibilidad;
	private EClase clase;

	public EAtributo(EClase clase,Token tn,Tipo tipo,Visibilidad v){		
		super(tn,tipo);
		this.clase=clase;
		this.visibilidad=v;
	}
	
	public EClase getClase(){
		return this.clase;
	}
	
	public Visibilidad getVisibilidad() {
		return visibilidad;
	}
	

	@Override
	public boolean equals(Object e){
		EAtributo at=(EAtributo) e;	
		return this.getNombre().equals(at.getNombre());
	}
	@Override
	public int hashCode() {
		int hash=this.getNombre().hashCode();
		return hash;
	}
	
	public String toString(){
		String s="\n";
		s+="Nombre:\t\t\t"+this.getNombre()+"\n";
		s+="Visibilidad:\t\t"+this.visibilidad+"\n";		
		s+="Tipo:\t\t\t"+this.tipo.toString()+"\n";		
		return s;
	}	

}
