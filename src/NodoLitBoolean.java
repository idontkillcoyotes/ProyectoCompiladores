
public class NodoLitBoolean extends NodoLiteral{
	
	public NodoLitBoolean(Token t){
		this.literal=t;
	}
	@Override
	public Tipo check() {
		return new TipoBool(null);
	}
	

}
