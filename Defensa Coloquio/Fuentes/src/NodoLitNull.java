
public class NodoLitNull extends NodoLiteral{

	public NodoLitNull(Token t){
		this.literal=t;
		this.tktipo=new Token(Utl.TPC_NULL,"null",0,0);
	}

	@Override
	public Tipo check() {
		return new TipoNull(tktipo);
	}
	
	@Override
	public void generar() {
		Utl.gen("push 0\t\t\t;pusheo null");		
	}

}
