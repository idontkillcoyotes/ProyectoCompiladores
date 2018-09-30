
public class EParametro extends EntradaTS{
	
	protected Token tokenNombre;
	protected String tipo;
	
	public EParametro(Token tn,String tipo){
		this.tokenNombre=tn;
		this.tipo=tipo;
		consolidado=false;
	}

	public String getTipo(){
		return this.tipo;
	}
	
	public String getNombre(){
		return this.tokenNombre.getLexema();
	}
	
	public Token getToken(){
		return this.tokenNombre;
	}
	
	/*
	 * Redefino equals para que considere dos parametros iguales a aquellos 
	 * que tienen mismo nombre y tipo
	 */
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
	
	public String toString(){
		String s="\n";
		s+="Nombre:\t"+this.getNombre()+"\n";
		s+="Tipo:\t"+this.tipo+"\n";
		return s;
	}
	
	public void consolidar(){
		System.out.println("consolidando parametro...");
		consolidado=true;
	}

}
