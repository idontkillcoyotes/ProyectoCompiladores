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
		s+="Nombre:\t"+this.getNombre()+"\n";
		s+="Visibilidad:\t"+this.visibilidad+"\n";		
		s+="Tipo:\t"+this.tipo.getTipo()+"\n";		
		return s;
	}
	
	public void consolidar(){
		System.out.println("consolidando atributo...");
		consolidado=true;
	}

}
