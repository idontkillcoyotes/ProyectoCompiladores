
public class NodoExpBinaria extends NodoExpresion{
	
	private NodoExpresion ladoizq;
	private NodoExpresion ladoder;
	private Operador operador;
	
	public NodoExpBinaria(Operador operador, NodoExpresion izq) {
		this.operador = operador;
		this.ladoizq = izq;
		this.ladoder = null;		
	}

	public NodoExpresion getLadoizq() {
		return ladoizq;
	}

	public NodoExpresion getLadoder() {
		return ladoder;
	}

	public void setLadoder(NodoExpresion ladoder) {
		this.ladoder = ladoder;
	}

	public Operador getOperador() {
		return operador;
	}

	@Override
	public Tipo check() throws SemanticException {
		Tipo der=this.ladoder.check();
		Tipo izq=this.ladoizq.check();
		Tipo ret=this.operador.checkBin(der, izq);
		return ret;
	}
	
	@Override
	public String toString(){
		String s="("+ladoizq.toString()+operador.getOperador().getLexema()+ladoder.toString()+")";
		return s;
	}

	@Override
	public Token getToken() {
		return this.operador.getOperador();
	}

	@Override
	public void setValorAtributo(boolean b) {
		this.valorAtributo=b;
		this.ladoder.setValorAtributo(b);
		this.ladoizq.setValorAtributo(b);
	}

	@Override
	public void generar() {
		this.ladoizq.generar();
		this.ladoder.generar();		
		this.operador.generarBin();		
	}

}
