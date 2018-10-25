
public class NodoExpParent extends NodoPrimario{
	
	private NodoExpresion expresion;
	
	public NodoExpParent() {
		this.expresion = null;
	}
	

	public void setExpresion(NodoExpresion expresion) {
		this.expresion = expresion;
	}

	public NodoExpresion getExpresion() {
		return expresion;
	}

	@Override
	public Tipo check() throws SemanticException {
		if(this.encadenado==null){
			return this.expresion.check();
		}
		else{
			Tipo exp=this.expresion.check();
			//TODO esto es una chanchada
			return this.encadenado.check(exp,expresion.getToken());		
		}		
	}
	
	@Override
	public String toString(){
		String s="("+this.expresion.toString()+")";
		if(encadenado!=null){
			s+=encadenado.toString();
		}
		return s;
	}


	@Override
	public Token getToken() {
		return this.expresion.getToken();
	}


	@Override
	public void setValorAtributo(boolean b) {
		this.valorAtributo=b;
		this.expresion.setValorAtributo(b);
		if (this.encadenado!=null) this.encadenado.setValorAtributo(b);
	}

}
