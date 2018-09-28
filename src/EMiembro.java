import java.util.ArrayList;

public abstract class EMiembro {
	
	protected Token tokenNombre;
	protected ArrayList<EParametro> parametros;
	
	public String getNombre() {
		return tokenNombre.getLexema();
	}

	public Token getToken() {
		return tokenNombre;
	}
	
	public ArrayList<EParametro> getParametros() {
		return parametros;
	}

	public boolean addParametro(EParametro p){
		return this.parametros.add(p);
	}
	
}
