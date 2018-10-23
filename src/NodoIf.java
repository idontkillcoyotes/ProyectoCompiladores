
public class NodoIf extends NodoSentencia {
	
	private Token token;
	private NodoExpresion condicion;
	private NodoSentencia sentenciathen;
	private NodoSentencia sentenciaelse;
	
	public NodoIf(Token t) {
		this.token=t;		
	}
	public Token getToken() {
		return token;
	}
	
	public void setCondicion(NodoExpresion condicion) {
		this.condicion = condicion;
	}

	public void setSentenciathen(NodoSentencia sentenciathen) {
		this.sentenciathen = sentenciathen;
	}

	public void setSentenciaelse(NodoSentencia sentenciaelse) {
		this.sentenciaelse = sentenciaelse;
	}

	public NodoExpresion getCondicion() {
		return condicion;
	}

	public NodoSentencia getSentenciathen() {
		return sentenciathen;
	}

	public NodoSentencia getSentenciaelse() {
		return sentenciaelse;
	}

	@Override
	public void check() {		
	}
	
	@Override
	public String toString(){
		String s="if (";
		s+=this.condicion.toString()+") then\n";
		s+="\t"+this.sentenciathen;
		s+="\n";
		if (this.sentenciaelse!=null){
			s+="\telse\n\t"+this.sentenciaelse.toString()+"\n";
		}
		return s;		
	}

}
