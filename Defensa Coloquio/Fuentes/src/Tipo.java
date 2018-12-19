
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
	public abstract Tipo clonarTipoBase();
	
	/*
	 * Chequea que los tipos sean exactamente iguales
	 */
	public abstract boolean mismoTipo(Tipo t);
	
	/*
	 * Chequea que los tipos sean compatibles, usado para tipos clase
	 */
	public abstract boolean esCompatible(Tipo t);	
	
	/*
	 * Chequea solo el tipo base, no chequea los arreglos	
	 */
	public abstract boolean esTipo(int tipo);
	
	public abstract boolean isArreglo();	

	public abstract void setArreglo(boolean b);

	/*
	 * Chequea que el tipo este definido, en los tipo clases
	 * ademas setea la entrada clase asociada
	 */
	public abstract boolean estaDefinido();

}
