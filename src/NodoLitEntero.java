
public class NodoLitEntero extends NodoLiteral{
	
	public NodoLitEntero(Token t){
		this.literal=t;
	}
	
	@Override
	public Tipo check() {
		return new TipoInt(null);
	}
}
