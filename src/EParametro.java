
public class EParametro {
	
	private Token tokenNombre;
	private String nombre;
	private String tipo;
	
	public EParametro(String nombre,String tipo,Token t){
		this.nombre=nombre;
		this.tipo=tipo;
		this.tokenNombre=t;
	}
	/*
	 * Redefino equals para que considere dos parametros iguales a aquellos 
	 * que tienen mismo nombre y tipo
	 */
	public boolean equals(EParametro p){
		return ((nombre.equals(p.getNombre())) && (tipo.equals(p.getTipo())));
	}

	public String getNombre() {
		return nombre;
	}

	public String getTipo() {
		return tipo;
	}

	public Token getToken() {
		return tokenNombre;
	}

}
