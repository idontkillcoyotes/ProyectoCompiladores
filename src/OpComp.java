
public class OpComp extends Operador{
	
	// == !=
	public OpComp(Token op){
		this.op=op;
		this.tktipo=new Token(Utl.TPC_BOOLEAN,"boolean",0,0);
	}

	@Override
	public Tipo checkBin(Tipo t1, Tipo t2) throws SemanticException {
		//TODO deberia chequear el operador?
		return null;
	}

	@Override
	public Tipo checkUn(Tipo t) throws SemanticException {
		//no es usado
		return null;
	}
}
