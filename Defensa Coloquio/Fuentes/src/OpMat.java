
public class OpMat extends Operador{
	
	// + - * /
	public OpMat(Token op){
		this.op=op;
		this.tktipo=new Token(Utl.TPC_INT,"int",0,0);
	}

	@Override
	public Tipo checkBin(Tipo t1, Tipo t2) throws SemanticException {
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
		if(t.esTipo(Utl.TPC_INT)){
			return new TipoInt(tktipo);
		}
		else{
			throw new SemanticException(op.getNroLinea(),op.getNroColumna(),"Tipos incompatibles.\n"
					+ "El operador "+op.getLexema()+" opera solo con un tipo entero.");
		}
	}
	@Override
	public void generarBin() {
		switch (this.op.getTipo()){
		case Utl.TT_OPSUMA:
			Utl.gen("add\t\t\t;operador suma");
			break;
		case Utl.TT_OPRESTA:
			Utl.gen("sub\t\t\t;operador resta");
			break;
		case Utl.TT_OPMULT:
			Utl.gen("mul\t\t\t;operador multiplicacion");
			break;
		case Utl.TT_OPDIV:
			Utl.gen("div\t\t\t;operador division");
			break;
		}
	}

	@Override
	public void generarUn() {
		switch (this.op.getTipo()){
		case Utl.TT_OPNEGBOOL:
			Utl.gen("not ;operador negacion");
			break;
		case Utl.TT_OPRESTA:
			Utl.gen("neg ;operador negacion");
			break;
		}
		//si es un operador de suma unario no hay que hacer nada
	}
	
}
