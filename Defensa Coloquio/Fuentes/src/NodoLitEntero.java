
public class NodoLitEntero extends NodoLiteral{
	
	public NodoLitEntero(Token t){
		this.literal=t;
		this.tktipo=new Token(Utl.TPC_INT,"int",0,0);
	}
	
	@Override
	public Tipo check() {
		return new TipoInt(tktipo);
	}
	
	@Override
	public void generar() {
		Utl.gen("push "+literal.getLexema()+"\t\t\t;pusheo literal entero");		
	}
}
