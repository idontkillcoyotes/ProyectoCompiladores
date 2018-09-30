import java.util.ArrayList;

public abstract class EMiembro extends EntradaTS{
	
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

	public boolean addParametro(EParametro p) {
		if (!this.parametros.contains(p)){
			this.parametros.add(p);
			return true;
		}
		else{
			return false;
		}
	}
	public int getAridad(){
		return this.parametros.size();
	}
}
