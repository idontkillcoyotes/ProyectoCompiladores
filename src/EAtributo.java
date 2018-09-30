public class EAtributo extends EParametro{
	
	private Visibilidad visibilidad;

	public EAtributo(Token tn,String tipo,Visibilidad v){
		super(tn,tipo);
		this.visibilidad=v;
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
		s+="Tipo:\t"+this.tipo+"\n";		
		return s;
	}
	
	public void consolidar(){
		System.out.println("consolidando atributo...");
		consolidado=true;
	}

}
