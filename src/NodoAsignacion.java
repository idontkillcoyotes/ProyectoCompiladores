
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
		//izq.estaDefinido();
		//der.estaDefinido();
		//System.out.print(ladoizq.toString()+">"+izq.toString());
		//System.out.println(" = "+der.toString()+"<"+ladoder.toString());
		if (!der.esCompatible(izq)) 
			throw new SemanticException(ladoder.getToken().getNroLinea(),ladoder.getToken().getNroColumna(),"Tipos incompatibles.\n"
					+ izq.getTipo() +" es incompatible con "+ der.getTipo());
	}
	
	@Override
	public String toString(){
		String s=ladoizq.toString();
		s+="=";
		s+=ladoder.toString();
		s+="\n";
		return s;
	}

	@Override
	public boolean tieneRetorno() {
		return false;
	}

}
