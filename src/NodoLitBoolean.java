
public class NodoLitBoolean extends NodoLiteral{
	
	public NodoLitBoolean(Token t){
		this.literal=t;
		this.tktipo=new Token(Utl.TPC_BOOLEAN,"boolean",0,0);
	}
	@Override
	public Tipo check() {
		return new TipoBool(tktipo);
	}
	@Override
	public void generar() {
		int b;
		if(this.literal.getLexema().equals("false"))	b=0;
		else b=1;
		Utl.gen("push "+b+"\t\t\t;pusheo literal booleano");		
	}


}
