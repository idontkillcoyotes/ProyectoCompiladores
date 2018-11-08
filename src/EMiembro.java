import java.util.ArrayList;
import java.util.LinkedList;

public abstract class EMiembro extends EntradaTS{
	
	protected Token tokenNombre;
	protected ArrayList<EParametro> parametros;
	protected LinkedList<EParametro> varslocales;
	protected EClase clase;
	protected NodoBloque bloque;
	protected String texto;
	protected boolean esConstructor;
	
	@Override
	public String getNombre() {
		return tokenNombre.getLexema();
	}
	
	public abstract Tipo getTipoRetorno();

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
	
	public LinkedList<EParametro> getVarsLocales(){
		return varslocales;
	}
	
	public void copiarParametros(){
		this.varslocales.addAll(this.parametros);
	}
	
	public boolean pushVarLocal(EParametro p) {
		if (!this.varslocales.contains(p)){
			//uso push para luego poder hacer pop de las var locales del bloque
			this.varslocales.push(p);
			//System.out.println("push: "+p.toString());
			return true;
		}
		else{
			return false;
		}
	}
	public void popVarLocales(int n){
		for(int i=0;i<n;i++){			
			//System.out.println("pop: "+
					this.varslocales.pop() ;
			//);
		}
	}
	

	public EParametro getVarLocal(String n) {
		for(EParametro var: varslocales){
			if(var.getNombre().equals(n)){
				return var;
			}
		}
		return null;
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
		//agrego parametros a las varslocales
		this.copiarParametros();
		if (bloque!=null) bloque.check();
	}

	public EParametro getParametro(String n) {
		for(EParametro p:parametros){
			if(p.getNombre().equals(n)){
				return p;
			}
		}		
		return null;
	}

	public abstract boolean esEstatico();
	
	public abstract String nuevaEtiqueta();
	
}
