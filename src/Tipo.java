
public abstract class Tipo {
	
	protected Token tokenNombre;
	
	public Token getToken(){
		return this.tokenNombre;
	}
	
	public String getTipo(){
		return this.tokenNombre.getLexema();
	}
	
	public boolean mismoTipoBase(Tipo t) {
		if(t.esTipo(this.tokenNombre.getTipo())){
			return true;
		}
		else{
			return false;
		}
	}
	
	public abstract boolean mismoTipo(Tipo t);
	
	public abstract boolean esCompatible(Tipo t);		
	
	public abstract boolean isArreglo();
	
	public abstract boolean esTipo(int tipo);

	public abstract void setArreglo();

	public abstract boolean estaDefinido();

}
