
public class OpComp extends Operador{
	
	// == !=
	public OpComp(Token op){
		this.op=op;
		this.tktipo=new Token(Utl.TPC_BOOLEAN,"boolean",0,0);
	}

	@Override
	public Tipo checkBin(Tipo t1, Tipo t2) throws SemanticException {
		//debo chequear si son compatibles
		if(t1.esCompatible(t2)){
			TipoBool ret=new TipoBool(tktipo);
			return ret;
		}
		else
			throw new SemanticException(op.getNroLinea(),op.getNroColumna(),"Tipos incompatibles.\n"
					+t2.getTipo()+ " es incompatible con "+t1.getTipo());
		
	}

	@Override
	public Tipo checkUn(Tipo t) throws SemanticException {
		//no es usado
		return null;
	}
}
