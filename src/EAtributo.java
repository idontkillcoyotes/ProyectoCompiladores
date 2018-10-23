public class EAtributo extends EParametro{
	
	private Visibilidad visibilidad;
	private EClase clase;
	private NodoExpresion valor;

	public EAtributo(EClase clase,Token tn,Tipo tipo,Visibilidad v){		
		super(tn,tipo);
		this.clase=clase;
		this.visibilidad=v;
		this.valor=null;
	}
	
	public EClase getClase(){
		return this.clase;
	}
	
	public Visibilidad getVisibilidad() {
		return visibilidad;
	}
	
	public void setValor(NodoExpresion n){
		this.valor=n;
	}
	
	public NodoExpresion getValor(){
		return this.valor;
	}
	
	@Override
	public void check() throws SemanticException {
		Tipo val=valor.check();
		if(!val.esCompatible(this.tipo))
			throw new SemanticException(tokenNombre.getNroLinea(),tokenNombre.getNroColumna(),
					"Tipos incompatibles.");
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
