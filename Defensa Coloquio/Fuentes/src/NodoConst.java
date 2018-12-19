
public abstract class NodoConst extends NodoPrimario{
	
	protected Token tokenNombre;

	public Token getToken() {
		if(this.encadenado!=null)
			return this.encadenado.getToken(tokenNombre);		
		else
			return tokenNombre;		
	}

}
