
public class EParametro extends EntradaTS{
	
	protected Token tokenNombre;
	protected Tipo tipo;
	
	public EParametro(Token tn,Tipo tipo){
		this.tokenNombre=tn;
		this.tipo=tipo;
		consolidado=false;
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
		String s="\n";
		s+="Nombre:\t\t\t"+this.getNombre()+"\n";
		s+="Tipo:\t\t\t"+this.tipo.toString()+"\n";
		return s;
	}
	
	@Override
	public void consolidar() throws SemanticException{
		if(!consolidado){
			if (!this.tipo.estaDefinido()){
				throw new SemanticException(tipo.getToken().getNroLinea(),tipo.getToken().getNroColumna(),
						"El tipo "+this.tipo.toString()+" no está definido.");
			}
			this.consolidado=true;
		}
	}

	@Override
	public void check() throws SemanticException {
		// TODO Auto-generated method stub
		
	}

}
