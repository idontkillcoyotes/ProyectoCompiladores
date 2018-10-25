
public class OpMat extends Operador{
	
	// + - * /
	public OpMat(Token op){
		this.op=op;
		this.tktipo=new Token(Utl.TPC_INT,"int",0,0);
	}

	@Override
	public Tipo checkBin(Tipo t1, Tipo t2) throws SemanticException {
		//TODO deberia chequear el operador?
		if((t1.esTipo(Utl.TPC_INT) && (t1.mismoTipoBase(t2)))){
			return new TipoInt(tktipo);
		}
		else{
			throw new SemanticException(op.getNroLinea(),op.getNroColumna(),"Tipos incompatibles.\n"
				+ "El operador "+op.getLexema()+" opera solo con tipos enteros.");
		}
	}

	@Override
	public Tipo checkUn(Tipo t) throws SemanticException {
		//TODO deberia chequear el operador?
		if(t.esTipo(Utl.TPC_INT)){
			return new TipoInt(tktipo);
		}
		else{
			throw new SemanticException(op.getNroLinea(),op.getNroColumna(),"Tipos incompatibles.\n"
					+ "El operador "+op.getLexema()+" opera solo con un tipo entero.");
		}
	}
}
