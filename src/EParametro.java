
public class EParametro extends EntradaTS{
	
	protected Token tokenNombre;
	protected Tipo tipo;
	protected int offset;
	
	public EParametro(Token tn,Tipo tipo){
		this.tokenNombre=tn;
		this.tipo=tipo;
		this.consolidado=false;
		this.offset=-1;
	}

	public Tipo getTipo(){
		return this.tipo;
	}
	
	public Token getToken(){
		return this.tokenNombre;
	}
	
	@Override
	public String getNombre(){
		return this.tokenNombre.getLexema();
	}
	

	@Override
	public boolean equals(Object e){
		EParametro par=(EParametro) e;
		return this.getNombre().equals(par.getNombre());
	}
	@Override
	public int hashCode() {
		int hash=this.getNombre().hashCode();
		return hash;
	}
	
	@Override
	public String toString(){
		String s="";		
		s+=this.tipo.toString()+" ";
		s+=this.offset+" ";
		s+=this.getNombre();
		return s;
	}	

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	@Override
	public void consolidar() throws SemanticException{
		if(!consolidado){
			if (!this.tipo.estaDefinido()){
				throw new SemanticException(tipo.getToken().getNroLinea(),tipo.getToken().getNroColumna(),
						"El tipo "+this.tipo.toString()+" no est� definido.");
			}
			this.consolidado=true;
		}
	}

	@Override
	public void check() throws SemanticException {		
	}


}
