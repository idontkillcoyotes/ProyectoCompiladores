
public class NodoConstArray extends NodoConst{
	
	private NodoExpresion tamaño;
	
	public NodoConstArray(Token t) {
		this.tokenNombre=t;
	}	

	public void setTamaño(NodoExpresion tamaño) {
		this.tamaño = tamaño;
	}

	public NodoExpresion getTamaño() {
		return tamaño;
	}

	@Override
	public Tipo check() {
		return null;		
	}
	@Override
	public String toString(){
		String s=tokenNombre.getLexema()+"[";
		s+=tamaño.toString()+"]";
		if(encadenado!=null){
			s+=encadenado.toString();
		}
		return s;
	}

}
