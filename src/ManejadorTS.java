import java.util.ArrayList;

public class ManejadorTS {
	
	private EClase claseActual;
	private EMiembro ambienteActual;
	private Token tkObject;
	
	public ManejadorTS(){
		Utl.ts=new TablaSimbolos();
		this.ambienteActual=null;
		this.claseActual=null;
		this.tkObject=new Token(Utl.TT_IDCLASE,"Object",0);

	}
	public void crearClasesPredefinidas() throws SemanticException{
		crearClaseObject();
		crearClaseSystem();
	}
	
	private void crearClaseObject() throws SemanticException{	
		this.crearClase(tkObject,null);
		this.claseActual.setConsolidado();
		this.crearConstructor(this.tkObject);
		this.agregarConstructor();	
	}
	private void crearClaseSystem() throws SemanticException{
		Token tn=new Token(Utl.TT_IDCLASE,"System",0);
		this.crearClase(tn, tkObject);
		this.claseActual.setConsolidado();
		this.crearConstructor(tn);
		this.agregarConstructor();
		this.crearMetodosSystem();
	}
	private void crearMetodosSystem() throws SemanticException{
		Token tn,tt,tp,tpt;
		//static int read()
		tn=new Token(Utl.TT_IDMETVAR,"read",0);
		tt=new Token(Utl.TPC_INT,"int",0);
		this.crearMetodo(tn,FormaMetodo.fStatic,new TipoSimple(tt));
		this.agregarMetodo();

		//static void printB(boolean b)
		tn=new Token(Utl.TT_IDMETVAR,"printB",0);
		tt=new Token(Utl.TPC_VOID,"void",0);
		tpt=new Token(Utl.TPC_BOOLEAN,"boolean",0);
		tp=new Token(Utl.TT_IDMETVAR,"b",0);
		this.crearMetodo(tn,FormaMetodo.fStatic,new TipoSimple(tt));
		this.crearParametro(tp, new TipoSimple(tpt));
		this.agregarMetodo();

		//static void printC(char c)
		tn=new Token(Utl.TT_IDMETVAR,"printC",0);
		tt=new Token(Utl.TPC_VOID,"void",0);
		tpt=new Token(Utl.TPC_CHAR,"char",0);
		tp=new Token(Utl.TT_IDMETVAR,"c",0);
		this.crearMetodo(tn,FormaMetodo.fStatic,new TipoSimple(tt));
		this.crearParametro(tp, new TipoSimple(tpt));
		this.agregarMetodo();

		//static void printI(int i)
		tn=new Token(Utl.TT_IDMETVAR,"printI",0);
		tt=new Token(Utl.TPC_VOID,"void",0);
		tpt=new Token(Utl.TPC_INT,"int",0);
		tp=new Token(Utl.TT_IDMETVAR,"i",0);
		this.crearMetodo(tn,FormaMetodo.fStatic,new TipoSimple(tt));
		this.crearParametro(tp, new TipoSimple(tpt));
		this.agregarMetodo();

		//static void printS(String s)
		tn=new Token(Utl.TT_IDMETVAR,"printS",0);
		tt=new Token(Utl.TPC_VOID,"void",0);
		tpt=new Token(Utl.TPC_STRING,"String",0);
		tp=new Token(Utl.TT_IDMETVAR,"s",0);
		this.crearMetodo(tn,FormaMetodo.fStatic,new TipoSimple(tt));
		this.crearParametro(tp, new TipoSimple(tpt));
		this.agregarMetodo();

		//static void println()
		tn=new Token(Utl.TT_IDMETVAR,"println",0);
		tt=new Token(Utl.TPC_VOID,"void",0);
		this.crearMetodo(tn,FormaMetodo.fStatic,new TipoSimple(tt));
		this.agregarMetodo();

		//static void printBln(boolean b)
		tn=new Token(Utl.TT_IDMETVAR,"printBln",0);
		tt=new Token(Utl.TPC_VOID,"void",0);
		tpt=new Token(Utl.TPC_BOOLEAN,"boolean",0);
		tp=new Token(Utl.TT_IDMETVAR,"b",0);
		this.crearMetodo(tn,FormaMetodo.fStatic,new TipoSimple(tt));
		this.crearParametro(tp, new TipoSimple(tpt));
		this.agregarMetodo();

		//static void printCln(char c)
		tn=new Token(Utl.TT_IDMETVAR,"printCln",0);
		tt=new Token(Utl.TPC_VOID,"void",0);
		tpt=new Token(Utl.TPC_CHAR,"char",0);
		tp=new Token(Utl.TT_IDMETVAR,"c",0);
		this.crearMetodo(tn,FormaMetodo.fStatic,new TipoSimple(tt));
		this.crearParametro(tp, new TipoSimple(tpt));
		this.agregarMetodo();

		//static void printIln(int i)
		tn=new Token(Utl.TT_IDMETVAR,"printIln",0);
		tt=new Token(Utl.TPC_VOID,"void",0);
		tpt=new Token(Utl.TPC_INT,"int",0);
		tp=new Token(Utl.TT_IDMETVAR,"i",0);
		this.crearMetodo(tn,FormaMetodo.fStatic,new TipoSimple(tt));
		this.crearParametro(tp, new TipoSimple(tpt));
		this.agregarMetodo();

		//static void printSln(String s)
		tn=new Token(Utl.TT_IDMETVAR,"printSln",0);
		tt=new Token(Utl.TPC_VOID,"void",0);
		tpt=new Token(Utl.TPC_STRING,"String",0);
		tp=new Token(Utl.TT_IDMETVAR,"s",0);
		this.crearMetodo(tn,FormaMetodo.fStatic,new TipoSimple(tt));
		this.crearParametro(tp, new TipoSimple(tpt));
		this.agregarMetodo();		
	}
	
	public void crearClase(Token tn,Token padre) throws SemanticException{
		if(padre==null){
			padre=this.tkObject;
		}
		EClase c=new EClase(tn,padre);
		boolean b=Utl.ts.addClase(c);
		if (b) {
			Utl.ts.setClaseAct(c);
			this.claseActual=Utl.ts.getClaseAct();
		}
		else throw new SemanticException(tn.getNroLinea(),tn.getNroColumna(),
				"El nombre de la clase esta duplicado.");
	}
	
	
	public void crearMetodo(Token tn,FormaMetodo f,Tipo tipo) throws SemanticException{
		if (this.claseActual!=null){
			Bloque b=new Bloque("Este metodo pertenece a la clase: "+this.claseActual.getNombre());
			EMetodo m=new EMetodo(claseActual,tn,f,tipo,b);
			this.ambienteActual=m;			
		}
	}
	public void agregarMetodo() throws SemanticException{
		boolean b=this.claseActual.addMetodo((EMetodo)this.ambienteActual);
		if (!b){
			throw new SemanticException(ambienteActual.getToken().getNroLinea(),ambienteActual.getToken().getNroColumna(),
					"Ya existe un metodo con mismo nombre y aridad. Sobrecarga invalida.");
		}
	}
	
	public void crearConstructor(Token tn) throws SemanticException{		
		if (this.claseActual!=null){
			if (tn.getLexema().equals(claseActual.getNombre())){				
				EConstructor c=new EConstructor(claseActual,tn,new Bloque("constructor"));
				this.ambienteActual=c;
			}
			else throw new SemanticException(tn.getNroLinea(),tn.getNroColumna(),
					"El nombre del constructor no es el mismo que el nombre de la clase.");
		}
	}
	public void agregarConstructor() throws SemanticException{
		boolean b=this.claseActual.addConstructor((EConstructor)this.ambienteActual);
		if (!b){
			throw new SemanticException(ambienteActual.getToken().getNroLinea(),ambienteActual.getToken().getNroColumna(),
					"Ya existe un constructor con la misma aridad.");
		}
	}
	
	private void crearAtributo(Token tn,Tipo tipo,Visibilidad vis) throws SemanticException{
		if (this.claseActual!=null){
			EAtributo a=new EAtributo(claseActual,tn,tipo,vis);
			boolean b=this.claseActual.addAtributo(a);
			if (!b){
				throw new SemanticException(tn.getNroLinea(),tn.getNroColumna(),
						"El nombre del atributo esta duplicado.");
			}
		}
	}	
	
	public void crearAtributos(ArrayList<EParametro> lista,Visibilidad vis) throws SemanticException {
		for(EParametro p: lista){
			crearAtributo(p.getToken(),p.getTipo(),vis);
		}
	}
	
	public void crearParametro(Token tn, Tipo tipo) throws SemanticException{
		if(this.ambienteActual!=null){
			EParametro p=new EParametro(tn,tipo);
			boolean b=this.ambienteActual.addParametro(p);
			if (!b){
				throw new SemanticException(tn.getNroLinea(),tn.getNroColumna(),
						"El nombre del parametro esta duplicado.");
			}
			
		}
	}
	public void consolidarTS() throws SemanticException{
		Utl.ts.consolidar();
	}
	public void imprimirTS(){
		Utl.ts.imprimir();
	}
	
	public EClase claseAct(){
		return this.claseActual;
	}	
	public EMiembro ambienteAct(){
		return this.ambienteActual;
	}

}
