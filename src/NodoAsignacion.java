
public class NodoAsignacion extends NodoSentencia{
	
	private NodoAcceso ladoizq;
	private NodoExpresion ladoder;
	private Token igual;
	
	public NodoAsignacion(){
		this.ladoder=null;
		this.ladoizq=null;
		this.igual=null;
	}
	
	public void setLadoizq(NodoAcceso ladoizq) {
		this.ladoizq = ladoizq;
	}

	public void setLadoder(NodoExpresion ladoder) {
		this.ladoder = ladoder;
	}

	public NodoAcceso getLadoizq() {
		return ladoizq;
	}

	public NodoExpresion getLadoder() {
		return ladoder;
	}
	
	public Token getIgual() {
		return igual;
	}
	
	public void setTokenIgual(Token t){
		this.igual=t;
	}

	@Override
	public void check() throws SemanticException{
		Tipo der=this.ladoder.check();
		Tipo izq=this.ladoizq.check();
		if (!der.esCompatible(izq)) 
			throw new SemanticException(igual.getNroLinea(),igual.getNroColumna(),"Tipos incompatibles.\n"
					+ izq.getTipo() +" es incomp"
							+ "atible con "+ der.getTipo());
	}
	
	@Override
	public String toString(){
		String s=ladoizq.toString();
		s+="=";
		s+=ladoder.toString();
		s+="\n";
		return s;
	}

}
