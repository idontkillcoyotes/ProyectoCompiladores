
public class NodoLitChar extends NodoLiteral{

	public NodoLitChar(Token t){
		this.literal=t;
		this.tktipo=new Token(Utl.TPC_CHAR,"char",0,0);
	}

	@Override
	public Tipo check() {
		return new TipoChar(tktipo);
	}
	
	@Override
	public String toString(){
		return ("'"+this.literal.getLexema()+"'");
	}
	
}
