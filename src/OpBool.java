
public class OpBool extends Operador{
	
	// && || !
	public OpBool(Token op){
		this.op=op;
		this.tktipo=new Token(Utl.TPC_BOOLEAN,"boolean",0,0);
	}

	@Override
	public Tipo checkBin(Tipo t1, Tipo t2) throws SemanticException {
		if((t1.esTipo(Utl.TPC_BOOLEAN) && (t1.mismoTipoBase(t2)))){
			return new TipoBool(tktipo);
		}
		else{
			throw new SemanticException(op.getNroLinea(),op.getNroColumna(),"Tipos incompatibles.\n"
					+ "El operador "+op.getLexema()+" opera solo con tipos booleanos.");
		}
	}

	@Override
	public Tipo checkUn(Tipo t) throws SemanticException {
		if(t.esTipo(Utl.TPC_BOOLEAN)){
			return new TipoBool(tktipo);
		}
		else{
			throw new SemanticException(op.getNroLinea(),op.getNroColumna(),"Tipos invalidos.");
		}
	}

	@Override
	public void generarBin() {
		if (this.op.esTipo(Utl.TT_OPANDDOBLE))
			Utl.gen("and\t\t\t;operador and");
		else
			Utl.gen("or\t\t\t;operador or");
	}

	@Override
	public void generarUn() {
		Utl.gen("not\t\t\t;operador not");
	}
}
