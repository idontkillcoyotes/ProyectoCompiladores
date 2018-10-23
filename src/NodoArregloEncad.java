
public class NodoArregloEncad extends Encadenado{
	
	private NodoExpresion expresion;	
	
	public NodoArregloEncad() {
		this.expresion = null;
	}
	
	
	public NodoExpresion getExpresion() {
		return expresion;
	}
	public void setExpresion(NodoExpresion expresion) {
		this.expresion = expresion;
	}

	@Override
	public Tipo check(Tipo t) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String toString(){
		String s=".[";
		s+=expresion.toString()+"]";
		if(encadenado!=null){
			s+=encadenado.toString();
		}
		return s;
	}

}
