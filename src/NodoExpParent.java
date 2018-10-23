
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
			return this.encadenado.check(exp);		
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

}
