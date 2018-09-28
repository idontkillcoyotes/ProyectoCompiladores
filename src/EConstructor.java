import java.util.ArrayList;

public class EConstructor extends EMiembro {

	private Token tokenNombre;
	private ArrayList<EParametro> parametros;
	
	public EConstructor(Token t){
		this.parametros=new ArrayList<EParametro>();
		this.tokenNombre=t;
	}
	
	/*
	 * Redefino equals para que considere iguales a los constructores
	 * con los mismos parametros
	 */
	public boolean equals(EConstructor c){
		return this.parametros.equals(c.getParametros());
	}
	
	public boolean addParametro(EParametro p){
		return this.parametros.add(p);
	}
	
	public ArrayList<EParametro> getParametros(){
		return this.parametros;
	}

	public Token getToken() {
		return tokenNombre;
	}

}
