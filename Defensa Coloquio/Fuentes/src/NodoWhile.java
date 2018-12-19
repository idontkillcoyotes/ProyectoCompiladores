
public class NodoWhile extends NodoSentencia{
	
	private Token token;
	private int numid;
	private EMiembro miembro;	
	private NodoExpresion condicion;
	private NodoSentencia sentencia;
	
	public NodoWhile(Token t,EMiembro m,int n) {
		this.token=t;
		this.miembro=m;
		this.numid=n;
		this.condicion=null;
		this.sentencia=null;
	}
	
	public Token getToken() {
		return token;
	}

	public EMiembro getMiembro() {
		return miembro;
	}

	public void setCondicion(NodoExpresion condicion) {
		this.condicion = condicion;
	}
	@Override
	public boolean tieneRetorno() {
		//el while nunca tiene retorno, porque puede
		//que nunca entre por la condicion
		return false;
	}


	public void setSentencia(NodoSentencia sentencia) {
		this.sentencia = sentencia;
	}


	public NodoExpresion getCondicion() {
		return condicion;
	}

	public NodoSentencia getSentencia() {
		return sentencia;
	}
	
	private String labelInit(){
		String s="while_"+this.numid;
		s+=this.miembro.getLabel();
		return s;
	}
	private String labelFin(){
		String s="endwhile_"+this.numid;
		s+=this.miembro.getLabel();
		return s;
	}
	

	@Override
	public void check() throws SemanticException {
		Tipo exp=this.condicion.check();
		if(exp.esTipo(Utl.TPC_BOOLEAN)){
			if (!(sentencia instanceof NodoDeclaracionVars)){
				this.sentencia.check();
			}
			else{
				//si es declaracion de variables seteo que es sentencia unica y chequeo
				NodoDeclaracionVars s=(NodoDeclaracionVars)this.sentencia;
				s.setSentenciaUnica(true);
				this.sentencia.check();	
			}
		}
		else
			throw new SemanticException(token.getNroLinea(),token.getNroColumna()+6,
					"Condicion no booleana.\nEl resultado de la expresion de condicion debe ser de tipo booleano.");
		
	}

	@Override
	public String toString(){
		String s="while (";
		s+=this.condicion.toString()+")\n";
		s+="\t"+this.sentencia.toString();
		return s;
	}

	@Override
	public void generar() {
		//Al final los NOP eran necesarios porque puede haber problemas con las etiquetas
		Utl.gen(labelInit()+": nop\t\t\t;genero label inicio while (nodowhile)");
		this.condicion.generar();
		Utl.gen("bf "+labelFin()+"\t\t\t;si condicion false salto a fin (nodowhile)");
		this.sentencia.generar();
		Utl.gen("jump "+labelInit()+"\t\t\t;salto al principio (nodowhile)");
		Utl.gen(labelFin()+": nop\t\t\t;genero label fin while (nodowhile)");
	}
}
