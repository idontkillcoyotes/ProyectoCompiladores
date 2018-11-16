
public class NodoIf extends NodoSentencia {
	
	private Token token;
	private EMiembro miembro;
	private int numid;
	private NodoExpresion condicion;
	private NodoSentencia sentenciathen;
	private NodoSentencia sentenciaelse;
	
	public NodoIf(Token t,EMiembro m,int n) {
		this.token=t;
		this.miembro=m;
		this.numid=n;
		this.condicion=null;
		this.sentenciaelse=null;
		this.sentenciathen=null;
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
			//si tiene else, ambos deben tener retorno
			boolean aux=sentenciathen.tieneRetorno();
			aux=(aux&&sentenciaelse.tieneRetorno());
			return aux;
		}
		else{
			//si no tiene else, no tiene retorno
			return false;
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
	private String labelElse(){
		String s="else_"+this.numid;
		s+=this.miembro.getLabel();
		return s;
	}
	private String labelFin(){
		String s="endif_"+this.numid;
		s+=this.miembro.getLabel();
		return s;
	}
	
	public EMiembro getMiembro() {
		return miembro;
	}
	@Override
	public void generar() {
		//genero condicion
		this.condicion.generar();
		if(this.sentenciaelse!=null){			
			//si la condicion es falsa salto al else
			Utl.gen("bf "+labelElse()+"\t\t\t;si condicion false salto a else (nodoif)");
			//sino sigo con el codigo de el then
			//Utl.gen(";codificacion de: "+sentenciathen.toString());
			this.sentenciathen.generar();
			//cuando termino salto al fin
			Utl.gen("jump "+labelFin()+"\t\t\t;termino el then, salto al fin (nodoif)");			
			//declaro etiqueta else
			Utl.gen(labelElse()+":");
			//genero codigo else
			Utl.gen(";codificacion de: "+sentenciaelse.toString());
			this.sentenciaelse.generar();
			//declaro etiqueta fin
			Utl.gen(labelFin()+": nop");
		}
		else{
			//si la condicion es falsa salto al fin
			Utl.gen("bf "+labelFin()+"\t\t\t;si condicion false salto fin(nodoif)");
			//sino sigo con el codigo del then
			//Utl.gen(";codificacion de: "+sentenciathen.toString());
			this.sentenciathen.generar();
			//declaro etiqueta fin
			Utl.gen(labelFin()+": nop");
		}
	}

}
