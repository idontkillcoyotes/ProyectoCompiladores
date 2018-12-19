public class EAtributo extends EParametro{
	
	private Visibilidad visibilidad;
	private EClase clase;
	private NodoExpresion valor;
	private boolean checked;

	public EAtributo(EClase clase,Token tn,Tipo tipo,Visibilidad v,NodoExpresion valor){		
		super(tn,tipo);
		this.clase=clase;
		this.visibilidad=v;
		this.valor=valor;
		this.checked=false;
		this.offset=-1;
	}
	
	public EClase getClase(){
		return this.clase;
	}
	
	public Visibilidad getVisibilidad() {
		return visibilidad;
	}	
	
	public boolean isChecked() {
		return checked;
	}

	public boolean esPublico(){
		return (this.visibilidad==Visibilidad.vPublic);
	}
	
	public void setValor(NodoExpresion n){
		this.valor=n;
	}
	
	public NodoExpresion getValor(){
		return this.valor;
	}
	
	@Override
	public void check() throws SemanticException {
		if(!checked){
			if(valor!=null){
				Tipo val=valor.check();
				if(!val.esCompatible(this.tipo))
					throw new SemanticException(tokenNombre.getNroLinea(),tokenNombre.getNroColumna(),
							"Tipos incompatibles.");
			}
			this.checked=true;
		}
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
		String s="";
		s+=this.offset+" ";
		s+=this.visibilidad+" ";		
		s+=this.tipo.toString()+" ";
		s+=this.getNombre();
		return s;
	}
	

}
