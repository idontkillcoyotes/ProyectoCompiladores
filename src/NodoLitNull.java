
public class NodoLitNull extends NodoLiteral{

	public NodoLitNull(Token t){
		this.literal=t;
	}

	@Override
	public Tipo check() {
		return new TipoNull(null);
	}

}
