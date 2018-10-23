
public class NodoLitChar extends NodoLiteral{

	public NodoLitChar(Token t){
		this.literal=t;
	}

	@Override
	public Tipo check() {
		return new TipoChar(null);
	}
	
	@Override
	public String toString(){
		return ("'"+this.literal.getLexema()+"'");
	}
	
}
