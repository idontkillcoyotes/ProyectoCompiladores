import java.util.ArrayList;

public abstract class EMiembro extends EntradaTS{
	
	protected Token tokenNombre;
	protected ArrayList<EParametro> parametros;
	protected EClase clase;
	protected NodoBloque bloque;
	protected String texto;
	
	@Override
	public String getNombre() {
		return tokenNombre.getLexema();
	}

	public Token getToken() {
		return tokenNombre;
	}
	
	public String getTexto(){
		return this.texto;
	}
	
	public String getNombreAridad(){
		return this.getNombre()+"/"+this.getAridad();
	}
	
	public EClase getClase(){
		return this.clase;
	}
	
	public NodoBloque getBloque(){
		return this.bloque;
	}
	
	public void setBloque(NodoBloque b){
		this.bloque=b;
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
	
	@Override
	public void consolidar() throws SemanticException{
		if(!consolidado){
			for(EParametro p: parametros){
				p.consolidar();
			}
			this.consolidado=true;
		}
	}
	
	@Override
	public void check() throws SemanticException {
		//seteo miembro actual
		Utl.ts.setMiembroActual(this);
		Utl.ts.setBloqueActual(null);
		if (bloque!=null) bloque.check();
	}
	
}
