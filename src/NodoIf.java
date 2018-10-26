
public class NodoIf extends NodoSentencia {
	
	private Token token;
	private NodoExpresion condicion;
	private NodoSentencia sentenciathen;
	private NodoSentencia sentenciaelse;
	
	public NodoIf(Token t) {
		this.token=t;		
	}
	public Token getToken() {
		return token;
	}
	
	public void setCondicion(NodoExpresion condicion) {
		this.condicion = condicion;
	}

	public void setSentenciathen(NodoSentencia sentenciathen) {
		this.sentenciathen = sentenciathen;
	}

	public void setSentenciaelse(NodoSentencia sentenciaelse) {
		this.sentenciaelse = sentenciaelse;
	}

	public NodoExpresion getCondicion() {
		return condicion;
	}

	public NodoSentencia getSentenciathen() {
		return sentenciathen;
	}

	public NodoSentencia getSentenciaelse() {
		return sentenciaelse;
	}

	@Override
	public void check() throws SemanticException {
		Tipo tcon=this.condicion.check();
		if(tcon.esTipo(Utl.TPC_BOOLEAN)){
			//please dont boch me jajaja 			
			if (!(sentenciathen instanceof NodoDeclaracionVars)){
				this.sentenciathen.check();
				if(this.sentenciaelse!=null){
					if (!(sentenciathen instanceof NodoDeclaracionVars)){
						this.sentenciaelse.check();
					}
					else{
						//si es declaracion de variables seteo que es sentencia unica y chequeo
						NodoDeclaracionVars selse=(NodoDeclaracionVars)this.sentenciaelse;
						selse.setSentenciaUnica(true);
						this.sentenciaelse.check();		
					}
					
				}
			
			}
			else{
				//la sentencia del if es solo una de declaracion de variables
				//seteo sentencia unica
				NodoDeclaracionVars sthen=(NodoDeclaracionVars)this.sentenciathen;
				sthen.setSentenciaUnica(true);
				this.sentenciathen.check();	
				if (!(sentenciathen instanceof NodoDeclaracionVars)){
					this.sentenciaelse.check();
				}
				else{
					NodoDeclaracionVars selse=(NodoDeclaracionVars)this.sentenciaelse;
					selse.setSentenciaUnica(true);
					this.sentenciaelse.check();		
				}
			}
		}
		else throw new SemanticException(token.getNroLinea(),token.getNroColumna()+6,
			"Condicion no booleana.\nEl resultado de la expresion de condicion debe ser de tipo booleano.");
		
	}
	@Override
	public boolean tieneRetorno() {
		if(this.sentenciaelse!=null){
			//si tiene else
			boolean aux=sentenciathen.tieneRetorno();
			aux=(aux||sentenciaelse.tieneRetorno());
			return aux;
		}
		else{
			return this.sentenciathen.tieneRetorno();
		}
	}
	
	@Override
	public String toString(){
		String s="if (";
		s+=this.condicion.toString()+") then\n";
		s+="\t"+this.sentenciathen;
		s+="\n";
		if (this.sentenciaelse!=null){
			s+="\telse\n\t"+this.sentenciaelse.toString()+"\n";
		}
		return s;	
	}

}
