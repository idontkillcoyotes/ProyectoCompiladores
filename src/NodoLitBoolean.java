
public class NodoLitBoolean extends NodoLiteral{
	
	public NodoLitBoolean(Token t){
		this.literal=t;
		this.tktipo=new Token(Utl.TPC_BOOLEAN,"boolean",0,0);
	}
	@Override
	public Tipo check() {
		return new TipoBool(tktipo);
	}
	


}
