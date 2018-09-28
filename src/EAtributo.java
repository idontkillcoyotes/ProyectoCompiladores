
public class EAtributo {
	
	private Token tokenNombre;
	private String nombre;
	private String tipo;
	private Visibilidad visibilidad;
	
	public EAtributo(){
		this.nombre=null;
		this.tipo=null;
		this.visibilidad=null;
		this.tokenNombre=null;
	}
	public EAtributo(String nombre, String tipo, Visibilidad v,Token t){
		this.nombre=nombre;
		this.tipo=tipo;
		this.visibilidad=v;
		this.tokenNombre=t;
	}
	
	/*
	 * Redefino equals para que considere atributos iguales
	 * a los que tienen el mismo nombre 
	 * TODO nombre y tipo?
	 */
	public boolean equals(EAtributo a){
		return this.nombre.equals(a.getNombre());
	}
	
	public String getNombre() {
		return nombre;
	}

	public String getTipo() {
		return tipo;
	}

	public Visibilidad getVisibilidad() {
		return visibilidad;
	}


	public Token getToken() {
		return tokenNombre;
	}

	public void setToken(Token token) {
		this.tokenNombre = token;
	}
}
