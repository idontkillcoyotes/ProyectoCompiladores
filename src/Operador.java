
public abstract class Operador {
	
	protected Token op;
	protected Token tktipo;
	
	public Token getOperador(){
		return this.op;
	}
	public void setOperador(Token t){
		this.op=t;
	}
	
	public abstract Tipo checkBin(Tipo t1, Tipo t2) throws SemanticException;
	
	public abstract Tipo checkUn(Tipo t) throws SemanticException;
}
