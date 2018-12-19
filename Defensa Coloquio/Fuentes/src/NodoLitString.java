
public class NodoLitString extends NodoLiteral{
	
	public NodoLitString(Token t){
		this.literal=t;
		this.tktipo=new Token(Utl.TPC_STRING,"String",0,0);
	}

	@Override
	public Tipo check() {
		return new TipoString(tktipo);
	}
	
	@Override
	public String toString(){
		return ("\""+this.literal.getLexema()+"\"");
	}
	@Override
	public void generar() {
		String etq="str_"+literal.getNroLinea()+literal.getNroColumna();
		Utl.gen(".data");
		Utl.gen(etq+": dw \""+literal.getLexema()+"\", 0 ;defino literal string");
		Utl.gen(".code");
		Utl.gen("push "+etq+"\t\t\t;pusheo dir literal string");		
	}

}

