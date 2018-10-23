
public class NodoVarEncad extends Encadenado{
	
	private Token id;
	
	public Token getId() {
		return id;
	}

	public NodoVarEncad(Token id) {
		this.id = id;
	}

	@Override
	public Tipo check(Tipo t) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String toString(){
		String s="."+id.getLexema();
		if(encadenado!=null){
			s+=encadenado.toString();
		}
		return s;
	}

}
