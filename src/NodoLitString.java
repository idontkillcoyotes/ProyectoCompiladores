
public class NodoLitString extends NodoLiteral{
	
	public NodoLitString(Token t){
		this.literal=t;
	}

	@Override
	public Tipo check() {
		return new TipoString(null);
	}
	
	@Override
	public String toString(){
		return ("\""+this.literal.getLexema()+"\"");
	}

}
