
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
					throw new SemanticException(token.getNroLinea(),token.getNroColumna(),
							"Tipo invalido.\nEl tipo de la expresion no es igual al tipo de retorno del metodo");
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
	}
	
	@Override
	public String toString(){
		String s="return "+expresion.toString()+"\n";
		return s;
	}

}
