
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

	@Override
	public void generar() {
		char c=this.literal.getLexema().charAt(0);
		int k=(int)c;
		Utl.gen("push "+k+"\t\t\t;pusheo literal caracter");		
	}
	
}
