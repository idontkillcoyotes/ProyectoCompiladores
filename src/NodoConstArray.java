
public class NodoConstArray extends NodoConst{
	
	private NodoExpresion tama�o;
	
	public NodoConstArray(Token t) {
		this.tokenNombre=t;
	}	

	public void setTama�o(NodoExpresion tama�o) {
		this.tama�o = tama�o;
	}

	public NodoExpresion getTama�o() {
		return tama�o;
	}

	@Override
	public Tipo check() {
		return null;		
	}
	@Override
	public String toString(){
		String s=tokenNombre.getLexema()+"[";
		s+=tama�o.toString()+"]";
		if(encadenado!=null){
			s+=encadenado.toString();
		}
		return s;
	}

}
