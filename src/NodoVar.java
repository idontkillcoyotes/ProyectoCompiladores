
public class NodoVar extends NodoAcceso{
	
	private Token id;
	
	public NodoVar(Token id) {
		this.id = id;
	}
	
	public Token getId() {
		return id;
	}

	@Override
	public Tipo check() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String toString(){
		String s=id.getLexema();
		if(encadenado!=null){
			s+=encadenado.toString();
		}
		return s;
	}


}
