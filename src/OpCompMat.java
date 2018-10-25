
public class OpCompMat extends Operador{
	
	// < > >= <=
	public OpCompMat(Token op){
		this.op=op;
		this.tktipo=new Token(Utl.TPC_BOOLEAN,"boolean",0,0);
	}

	@Override
	public Tipo checkBin(Tipo t1, Tipo t2) throws SemanticException {
		//TODO deberia chequear el operador?
		if((t1.esTipo(Utl.TPC_INT) && (t1.mismoTipoBase(t2)))){
			return new TipoBool(tktipo);
		}
		else{
			throw new SemanticException(op.getNroLinea(),op.getNroColumna(),"Tipos incompatibles.\n"
					+ "El operador "+op.getLexema()+" opera solo con tipos enteros.");
		}
	}

	@Override
	public Tipo checkUn(Tipo t) throws SemanticException {
		//no es usado
		return null;
	}
}
