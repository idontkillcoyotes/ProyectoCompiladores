
public class NodoLitNull extends NodoLiteral{

	public NodoLitNull(Token t){
		this.literal=t;
		this.tktipo=new Token(Utl.TPC_NULL,"null",0,0);
	}

	@Override
	public Tipo check() {
		return new TipoNull(tktipo);
	}

}
