
public class NodoReturn extends NodoSentencia{
	
	private Token token;
	private EMiembro miembro;
	private NodoExpresion expresion;
	
	public NodoReturn(Token t) {
		this.token=t;
		this.expresion=null;
		this.miembro=null;
	}
	
	public Token getToken() {
		return token;
	}
	
	public EMiembro getMiembro(){
		return miembro;
	}
	
	public NodoExpresion getExpresion() {
		return expresion;
	}
	
	public void setExpresion(NodoExpresion expresion) {
		this.expresion = expresion;
	}

	@Override
	public void check() throws SemanticException {
		this.miembro=Utl.ts.getMiembroAct();
		if (!miembro.esConstructor){
			Tipo retorno=miembro.getTipoRetorno();
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

	@Override
	public void generar() {
		int pars=this.miembro.getCantParams();
		if (this.expresion!=null){
			//si la expresion no es nula, quiere decir que tengo un metodo que retorna algo
			this.expresion.generar();			
			int ret=3+1+pars;
			Utl.gen("store "+ret+" ;guardo el resultado (nodoreturn)");
		}
		Utl.gen("fmem "+this.miembro.getCantVarLoc()+" ;libero espacio de vars locales (nodoreturn)");
		Utl.gen("storefp ;retorno (nodoreturn)");
		Utl.gen("ret "+(pars+1)+" ;retorno (nodoreturn)");		
	}

}
