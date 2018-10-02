import java.util.ArrayList;

public abstract class EMiembro extends EntradaTS{
	
	protected Token tokenNombre;
	protected ArrayList<EParametro> parametros;
	protected EClase clase;
	protected Bloque bloque;
	
	public String getNombre() {
		return tokenNombre.getLexema();
	}

	public Token getToken() {
		return tokenNombre;
	}
	
	public String getNombreAridad(){
		return this.getNombre()+"/"+this.getAridad();
	}
	
	public EClase getClase(){
		return this.clase;
	}
	
	public Bloque getBloque(){
		return this.bloque;
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
