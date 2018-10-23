
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
			this.sentencia.check();
		}
	}

	@Override
	public String toString(){
		String s="while (";
		s+=this.condicion.toString()+")\n";
		s+="\t"+this.sentencia.toString();
		return s;
	}
}
