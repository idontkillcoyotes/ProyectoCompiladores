
public class NodoReturn extends NodoSentencia{
	
	private Token token;
	private NodoExpresion expresion;
	
	public NodoReturn(Token t) {
		this.token=t;
	}
	
	public Token getToken() {
		return token;
	}
	
	public NodoExpresion getExpresion() {
		return expresion;
	}
	
	public void setExpresion(NodoExpresion expresion) {
		this.expresion = expresion;
	}

	@Override
	public void check() throws SemanticException {
		if (!Utl.ts.getMiembroAct().esConstructor){
			Tipo retorno=Utl.ts.getMiembroAct().getTipoRetorno();
			if(this.expresion!=null){
				//expresion no nula
				Tipo exp=this.expresion.check();
				if(!exp.esCompatible(retorno))
					throw new SemanticException(expresion.getToken().getNroLinea(),expresion.getToken().getNroColumna(),
							"Tipo invalido.\nEl tipo de retorno no es compatible con el tipo de retorno del metodo");
			}
			else{
				//expresion nula
				if (!retorno.esTipo(Utl.TPC_VOID))
					//si no es tipo void deberia retornar algo
					throw new SemanticException(token.getNroLinea(),token.getNroColumna(),
							"Falta expresion de retorno.\nEste metodo debe retornar un resultado"
							+ "de tipo "+retorno.getTipo());
			}
			
		}
		else{
			throw new SemanticException(token.getNroLinea(),token.getNroColumna(),
					"Constructor con retorno.\nUn constructor no puede retornar un valor.");
		}
	}
	@Override
	public boolean tieneRetorno() {
		return true;
	}
	
	
	@Override
	public String toString(){
		String s="return "+expresion.toString()+"\n";
		return s;
	}

}
