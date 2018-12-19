
public class NodoExpUnaria extends NodoExpresion{
	
	private NodoExpresion expresion;
	private Operador operador;
	
	public NodoExpUnaria(Operador operador) {
		this.expresion = null;
		this.operador = operador;
	}	

	@Override
	public void setValorAtributo(boolean b) {
		this.valorAtributo=b;
		this.expresion.setValorAtributo(b);
	}

	public NodoExpresion getExpresion() {
		return expresion;
	}
	
	public void setExpresion(NodoExpresion expresion) {
		this.expresion = expresion;
	}

	public Operador getOperador() {
		return operador;
	}

	@Override
	public Tipo check() throws SemanticException{
		Tipo exp=this.expresion.check();
		Tipo ret=this.operador.checkUn(exp);
		return ret;
	}
	
	@Override
	public String toString(){
		String s="("+operador.getOperador().getLexema()+expresion.toString()+")";
		return s;
	}

	@Override
	public Token getToken() {
		return this.operador.getOperador();
	}

	@Override
	public void generar() {
		this.expresion.generar();
		this.operador.generarUn();
	}
}
