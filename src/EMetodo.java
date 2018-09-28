import java.util.ArrayList;

public class EMetodo extends EMiembro{
	
	private Token tokenNombre;
	private String nombre;
	private FormaMetodo forma;
	private String tipoRetorno;
	private ArrayList<EParametro> parametros;
	private boolean consolidado;
	
	public EMetodo(String nombre,FormaMetodo f,String tiporet,Token t){
		this.nombre=nombre;
		this.forma=f;
		this.tipoRetorno=tiporet;
		this.tokenNombre=t;
		this.parametros=new ArrayList<EParametro>();
		this.consolidado=false;
	}
	
	/*
	 * Redefino equals para que considere a dos metodos iguales
	 * cuando su nombre es igual
	 * TODO logro de metodos con mismo nombre y distintos parametros
	 */
	public boolean equals(EMetodo m){
		return this.nombre.equals(m.getNombre());
	}
	
	public boolean addParametro(EParametro p){
		return this.parametros.add(p);
	}

	public String getNombre() {
		return nombre;
	}

	public FormaMetodo getForma() {
		return forma;
	}

	public String getTipoRetorno() {
		return tipoRetorno;
	}
	
	public Token getToken() {
		return tokenNombre;
	}
	
	public ArrayList<EParametro> getParametros() {
		return parametros;
	}

	public boolean isConsolidado() {
		return consolidado;
	}

}
