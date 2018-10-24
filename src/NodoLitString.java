
public class NodoLitString extends NodoLiteral{
	
	public NodoLitString(Token t){
		this.literal=t;
		this.tktipo=new Token(Utl.TPC_STRING,"char",0,0);
	}

	@Override
	public Tipo check() {
		return new TipoString(tktipo);
	}
	
	@Override
	public String toString(){
		return ("\""+this.literal.getLexema()+"\"");
	}

}
