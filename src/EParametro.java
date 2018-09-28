
public class EParametro{
	
	protected Token tokenNombre;
	protected String tipo;
	
	public EParametro(Token tn,String tipo){
		this.tokenNombre=tn;
		this.tipo=tipo;
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
	public boolean equals(EParametro p){
		return ((getNombre().equals(p.getNombre())) && (tipo.equals(p.getTipo())));
	}


}
