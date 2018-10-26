
public class NodoWhile extends NodoSentencia{
	
	private Token token;
	private NodoExpresion condicion;
	private NodoSentencia sentencia;
	
	public NodoWhile(Token t) {
		this.token=t;
	}
	
	public Token getToken() {
		return token;
	}
	

	public void setCondicion(NodoExpresion condicion) {
		this.condicion = condicion;
	}
	@Override
	public boolean tieneRetorno() {
		return sentencia.tieneRetorno();
	}


	public void setSentencia(NodoSentencia sentencia) {
		this.sentencia = sentencia;
	}


	public NodoExpresion getCondicion() {
		return condicion;
	}

	public NodoSentencia getSentencia() {
		return sentencia;
	}

	@Override
	public void check() throws SemanticException {
		Tipo exp=this.condicion.check();
		if(exp.esTipo(Utl.TPC_BOOLEAN)){
			if (!(sentencia instanceof NodoDeclaracionVars)){
				this.sentencia.check();
			}
			else{
				//si es declaracion de variables seteo que es sentencia unica y chequeo
				NodoDeclaracionVars s=(NodoDeclaracionVars)this.sentencia;
				s.setSentenciaUnica(true);
				this.sentencia.check();	
			}
		}
		else
			throw new SemanticException(token.getNroLinea(),token.getNroColumna()+6,
					"Condicion no booleana.\nEl resultado de la expresion de condicion debe ser de tipo booleano.");
		
	}

	@Override
	public String toString(){
		String s="while (";
		s+=this.condicion.toString()+")\n";
		s+="\t"+this.sentencia.toString();
		return s;
	}
}
