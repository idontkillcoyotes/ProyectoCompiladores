
public class AnalizadorSintactico {
	
	private AnalizadorLexico alex;
	private Token tokenAct;
	
	public AnalizadorSintactico (AnalizadorLexico al){
		this.alex=al;
		try {
			this.tokenAct = alex.nextToken();
		} catch (LexicoException e) {
			System.out.println(e.getMessage());
		}		
	}
	private void print(String s){
		System.out.println(s);
	}	
	private void printToken(){
		System.out.println(Utl.getTipoID(tokenAct.getTipo()));
	}
	private void match(int tipoToken) throws SintacticException{
		if(tokenAct.esTipo(tipoToken)){
			try {
				tokenAct = alex.nextToken();
			}
			catch (LexicoException e) {
				System.out.println(e.getMessage());
			}
		}
		else{
			int l=tokenAct.getNroLinea();
			int c=tokenAct.getNroColumna();
			String esperado=Utl.getTipoID(tipoToken);
			String encontrado=Utl.getTipoID(tokenAct.getTipo());
			throw new SintacticException(l,c,"Se esperaba un token de tipo: "+esperado+"\nPero se encontro un token de tipo: "+encontrado);
		}
	}
	
	public void start() throws SintacticException{
		inicio();
	}
	
	private void inicio() throws SintacticException{
		clase();
		clases();
		match(Utl.TT_FINARCHIVO);		
	}
	
	private void clases() throws SintacticException{
		if (tokenAct.esTipo(Utl.TPC_CLASS)){
			clase();
			clases();
		}
		else if (tokenAct.esTipo(Utl.TT_FINARCHIVO)){
			//vacio
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: class FIN_ARCHIVO\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}		
	}
	
	private void clase() throws SintacticException{
		match(Utl.TPC_CLASS);
		match(Utl.TT_IDCLASE);
		herencia();
		match(Utl.TT_PUNLLAVE_A);
		miembros();
		match(Utl.TT_PUNLLAVE_C);		
	}
	
	private void herencia() throws SintacticException{
		if (tokenAct.esTipo(Utl.TPC_EXTENDS)){
			match(Utl.TPC_EXTENDS);
			match(Utl.TT_IDCLASE);
		}
		else if (tokenAct.esTipo(Utl.TT_PUNLLAVE_A)){
			//vacio
		}else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: extends (\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}			
	}
	private void miembros() throws SintacticException{
		if ( tokenAct.esTipo(new int[]{Utl.TPC_PUBLIC,Utl.TPC_PRIVATE,Utl.TPC_STATIC,Utl.TPC_DYNAMIC,Utl.TT_IDCLASE}) ){
			miembro();
			miembros();
		}
		else if (tokenAct.esTipo(Utl.TT_PUNLLAVE_C)){
			//vacio
		}else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: public private static dynamic idClase\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}			
	}
	private void miembro() throws SintacticException{
		if ( tokenAct.esTipo(new int[]{Utl.TPC_PUBLIC,Utl.TPC_PRIVATE}) ){
			atributo();
		}
		else if (tokenAct.esTipo(Utl.TT_IDCLASE)){
			ctor();
		}
		else if (tokenAct.esTipo(new int[]{Utl.TPC_STATIC,Utl.TPC_DYNAMIC})){
			metodo();
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: public private static dynamic idClase\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private void atributo() throws SintacticException{
		visibilidad();
		tipo();
		listaDecVars();
		//TODO logro inicializacion
		match(Utl.TT_PUNPUNTOCOMA);
	}
	private void metodo() throws SintacticException{
		formaMetodo();
		tipoMetodo();
		match(Utl.TT_IDMETVAR);
		argsFormales();
		bloque();
	}
	private void ctor() throws SintacticException{
		match(Utl.TT_IDCLASE);
		argsFormales();
		bloque();
	}
	private void argsFormales() throws SintacticException {
		match(Utl.TT_PUNPARENT_A);
		listaArg();
		match(Utl.TT_PUNPARENT_C);
	}
	private void listaArg()  throws SintacticException{
		if (tokenAct.esTipo(new int[]{Utl.TPC_BOOLEAN,Utl.TPC_CHAR,Utl.TPC_INT,Utl.TT_IDCLASE,Utl.TPC_STRING})){
			arg();
			argFormales();
		}
		else if (tokenAct.esTipo(Utl.TT_PUNPARENT_C)){
			//vacio
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: boolean char int String idClase )\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private void argFormales()  throws SintacticException{
		if (tokenAct.esTipo(Utl.TT_PUNCOMA)){
			match(Utl.TT_PUNCOMA);
			arg();
			argFormales();
		}
		else if (tokenAct.esTipo(Utl.TT_PUNPARENT_C)){
			//vacio
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: , )\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private void arg() throws SintacticException {
		tipo();
		match(Utl.TT_IDMETVAR);
	}
	private void formaMetodo() throws SintacticException {
		if (tokenAct.esTipo(Utl.TPC_STATIC)){
			match(Utl.TPC_STATIC);
		}
		else if (tokenAct.esTipo(Utl.TPC_DYNAMIC)){
			match(Utl.TPC_DYNAMIC);
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: static dynamic\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private void visibilidad()  throws SintacticException{
		if (tokenAct.esTipo(Utl.TPC_PUBLIC)){
			match(Utl.TPC_PUBLIC);
		}
		else if (tokenAct.esTipo(Utl.TPC_PRIVATE)){
			match(Utl.TPC_PRIVATE);
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: public private\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private void tipoMetodo()  throws SintacticException{
		if (tokenAct.esTipo(Utl.TPC_VOID)){
			match(Utl.TPC_VOID);
		}
		else if (tokenAct.esTipo(new int[]{Utl.TPC_BOOLEAN,Utl.TPC_CHAR,Utl.TPC_INT,Utl.TT_IDCLASE,Utl.TPC_STRING})){
			tipo();
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: void boolean char int String idClase\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private void tipo() throws SintacticException {
		if (tokenAct.esTipo(Utl.TPC_BOOLEAN)){
			match(Utl.TPC_BOOLEAN);
			posibleArreglo();
		}
		else if (tokenAct.esTipo(Utl.TPC_CHAR)){
			match(Utl.TPC_CHAR);
			posibleArreglo();
		}
		else if (tokenAct.esTipo(Utl.TPC_INT)){
			match(Utl.TPC_INT);
			posibleArreglo();
		}
		else if (tokenAct.esTipo(Utl.TT_IDCLASE)){
			match(Utl.TT_IDCLASE);
			//para logro posibleArreglo();
		}
		else if (tokenAct.esTipo(Utl.TPC_STRING)){
			match(Utl.TPC_STRING);
			//para logro posibleArreglo();
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: boolean char int String idClase\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private void posibleArreglo() throws SintacticException {
		if (tokenAct.esTipo(Utl.TT_PUNCORCH_A)){
			match(Utl.TT_PUNCORCH_A);
			match(Utl.TT_PUNCORCH_C);
		}
		else if (tokenAct.esTipo(Utl.TT_IDMETVAR)){
			//vacio
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: [ idMetVar\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private void tipoPrimitivo() throws SintacticException {
		if (tokenAct.esTipo(Utl.TPC_BOOLEAN)){
			match(Utl.TPC_BOOLEAN);
		}
		else if (tokenAct.esTipo(Utl.TPC_CHAR)){
			match(Utl.TPC_CHAR);
		}
		else if (tokenAct.esTipo(Utl.TPC_INT)){
			match(Utl.TPC_INT);
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: boolean char int\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	/*
	 * 	private void tipoReferencia() {
		}
	*/
	private void listaDecVars()  throws SintacticException{
		match(Utl.TT_IDMETVAR);
		listaDV();
	}
	private void listaDV() throws SintacticException {
		if (tokenAct.esTipo(Utl.TT_PUNCOMA)){
			match(Utl.TT_PUNCOMA);
			match(Utl.TT_IDMETVAR);
			listaDV();
		}
		else if (tokenAct.esTipo(Utl.TT_PUNPUNTOCOMA)){
			//vacio
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: , ;\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}		
	}	
	private void bloque() throws SintacticException {
		match(Utl.TT_PUNLLAVE_A);
		sentencias();
		match(Utl.TT_PUNLLAVE_C);
	}
	private void sentencias() throws SintacticException {
		if(tokenAct.esTipo(new int[]{Utl.TT_PUNPUNTOCOMA,Utl.TPC_IF,Utl.TPC_WHILE,Utl.TPC_RETURN,Utl.TT_IDMETVAR,Utl.TPC_THIS,Utl.TPC_BOOLEAN,Utl.TPC_CHAR,Utl.TPC_INT,Utl.TT_IDCLASE,Utl.TPC_STRING,Utl.TT_PUNPARENT_A,Utl.TT_PUNLLAVE_A})){
			sentencia();
			sentencias();
		}
		else if (tokenAct.esTipo(Utl.TT_PUNLLAVE_C)){
			//vacio
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: ; if while return this idMetVar idClase boolean char int String ( {\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}	
	}
	private void sentencia() throws SintacticException {
		if (tokenAct.esTipo(Utl.TT_PUNPUNTOCOMA)){
			match(Utl.TT_PUNPUNTOCOMA);
		}
		else if (tokenAct.esTipo(Utl.TPC_IF)){
			match(Utl.TPC_IF);
			match(Utl.TT_PUNPARENT_A);
			expresion();
			match(Utl.TT_PUNPARENT_C);
			sentencia();
			sentenciaElse();
			//TODO checkear si la ambiguedad se resuelve aca
		}
		else if (tokenAct.esTipo(Utl.TPC_WHILE)){
			match(Utl.TPC_WHILE);
			match(Utl.TT_PUNPARENT_A);
			expresion();
			match(Utl.TT_PUNPARENT_C);
			sentencia();
		}
		else if (tokenAct.esTipo(Utl.TPC_RETURN)){
			match(Utl.TPC_RETURN);
			expresiones();
			match(Utl.TT_PUNPUNTOCOMA);
		}
		else if (tokenAct.esTipo(new int[]{Utl.TT_IDMETVAR,Utl.TPC_THIS})){
			//asignacion ;
			asignacion();
			match(Utl.TT_PUNPUNTOCOMA);
		}
		else if (tokenAct.esTipo(Utl.TT_PUNPARENT_A)){
			//sentenciallamada ;
			sentenciaLlamada();
			match(Utl.TT_PUNPUNTOCOMA);
		}
		else if (tokenAct.esTipo(new int[]{Utl.TPC_BOOLEAN,Utl.TPC_CHAR,Utl.TPC_INT,Utl.TT_IDCLASE,Utl.TPC_STRING})){
			//tipo listadecvars ;
			tipo();
			listaDecVars();
			match(Utl.TT_PUNPUNTOCOMA);
		}
		else if (tokenAct.esTipo(Utl.TT_PUNLLAVE_A)){
			//bloque
			bloque();
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Sentencia mal formada.\n"
					+ "Se esperaba un token dentro del grupo: ( { ; if while return this boolean char int idClase idMetVar String\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private void sentenciaElse()  throws SintacticException{
		if (tokenAct.esTipo(Utl.TPC_ELSE)){
			match(Utl.TPC_ELSE);
			sentencia();
		}
		else if(tokenAct.esTipo(new int[]{Utl.TT_PUNPUNTOCOMA,Utl.TPC_IF,Utl.TPC_WHILE,Utl.TPC_RETURN,Utl.TT_IDMETVAR,Utl.TPC_THIS,Utl.TPC_BOOLEAN,Utl.TPC_CHAR,Utl.TPC_INT,Utl.TT_IDCLASE,Utl.TPC_STRING,Utl.TT_PUNPARENT_A,Utl.TT_PUNLLAVE_A})){
			//vacio
			//TODO checkear si los siguientes son correctos
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Sentencia else mal formada."
					+ "Se esperaba un token dentro del grupo: else ; if while return this boolean char int String idMetVar idClase ( {\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private void expresiones()  throws SintacticException{
		//primeros de expresion
		if (tokenAct.esTipo(new int[]{Utl.TT_OPSUMA,Utl.TT_OPRESTA,Utl.TT_OPNEGBOOL,Utl.TPC_NULL,Utl.TPC_TRUE,Utl.TPC_FALSE,Utl.TT_LITENTERO,Utl.TT_LITCARACTER,Utl.TT_LITSTRING,Utl.TT_IDMETVAR,Utl.TT_PUNPARENT_A,Utl.TT_IDCLASE,Utl.TPC_THIS,Utl.TPC_NEW})){
			expresion();
		}
		//siguientes expresiones
		else if(tokenAct.esTipo(Utl.TT_PUNPUNTOCOMA)){
			//vacio
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: + - ! null true false litEntero litCaracter litString idMetVar idClase this new (\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	} 
	private void asignacion()  throws SintacticException{
		if (tokenAct.esTipo(Utl.TT_IDMETVAR)){
			accesoVar();
			match(Utl.TT_ASIGIGUAL);
			expresion();
		}
		else if(tokenAct.esTipo(Utl.TPC_THIS)){
			accesoThis();
			match(Utl.TT_ASIGIGUAL);
			expresion();
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: this idMetVar\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	} 
	private void sentenciaLlamada() throws SintacticException {
		match(Utl.TT_PUNPARENT_A);
		primario();
		match(Utl.TT_PUNPARENT_C);
	}
	private void expresion()  throws SintacticException{
		expOr();
	}
	private void expOr()  throws SintacticException{
		expAnd();
		expOrR();
	}
	private void expOrR()  throws SintacticException{
		if (tokenAct.esTipo(Utl.TT_OPORDOBLE)){
			match(Utl.TT_OPORDOBLE);
			expAnd();
			expOrR();
		}
		else if(tokenAct.esTipo(new int[]{Utl.TT_PUNCORCH_C,Utl.TT_PUNCOMA,Utl.TT_PUNPARENT_C,Utl.TT_PUNPUNTOCOMA})){
			//vacio
			//TODO checkear que siguientes sean correctos
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Expresion mal formada.\n"
					+ "Se esperaba un token dentro del grupo: || , ; ] )\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private void expAnd() throws SintacticException{
		expIg();
		expAndR();
	}
	private void expAndR()  throws SintacticException{
		if (tokenAct.esTipo(Utl.TT_OPANDDOBLE)){
			match(Utl.TT_OPANDDOBLE);
			expIg();
			expAndR();
		}
		else if(tokenAct.esTipo(new int[]{Utl.TT_OPSUMA,Utl.TT_OPRESTA,Utl.TT_OPNEGBOOL,Utl.TPC_NULL,
				Utl.TPC_TRUE,Utl.TPC_FALSE,Utl.TT_LITENTERO,Utl.TT_LITCARACTER,Utl.TT_LITSTRING,
				Utl.TT_IDMETVAR,Utl.TT_PUNPARENT_A,Utl.TT_IDCLASE,Utl.TPC_THIS,Utl.TPC_NEW,Utl.TT_PUNCORCH_C,
				Utl.TT_PUNCOMA,Utl.TT_PUNPARENT_C,Utl.TT_PUNPUNTOCOMA})){
			//vacio
			//TODO checkear que siguientes sean correctos!!
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Expresion mal formada.\n"
					+ "Se esperaba un token dentro del grupo: + - ! null true false litEntero litCaracter"
					+ " litString idMetVar idClase this new ( ) ] , ;\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private void expIg()  throws SintacticException{
		expComp();
		expIgR();
	} 
	private void expIgR() throws SintacticException {
		if (tokenAct.esTipo(new int[]{Utl.TT_OPDOBLEIGUAL,Utl.TT_OPDESIGUAL})){
			opIgual();
			expComp();
			expIgR();
		}
		else if(tokenAct.esTipo(Utl.TT_OPANDDOBLE)){
			//vacio
			//TODO checkear que siguientes sean correctos! que pasa con el vacio?
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Expresion mal formada.\n"
					+ "Se esperaba un token dentro del grupo: == != &&\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private void expComp()  throws SintacticException{
		expAd();
		expCompR();
	}
	private void expCompR() throws SintacticException {
		if (tokenAct.esTipo(new int[]{Utl.TT_OPMENOR,Utl.TT_OPMENORIG,Utl.TT_OPMAYOR,Utl.TT_OPMAYORIG})){
			opComp();
			expAd();
		}
		else if(tokenAct.esTipo(new int[]{Utl.TT_OPDOBLEIGUAL,Utl.TT_OPDESIGUAL})){
			//vacio
			//TODO checkear que siguientes sean correctos!
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Expresion mal formada.\n"
					+ "Se esperaba un token dentro del grupo: < <= > >= == !=\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private void expAd()  throws SintacticException{
		expMul();
		expAdR();
	} 
	private void expAdR()  throws SintacticException{
		if (tokenAct.esTipo(new int[]{Utl.TT_OPSUMA,Utl.TT_OPRESTA})){
			opAd();
			expMul();
			expAdR();
		}
		else if(tokenAct.esTipo(new int[]{Utl.TT_OPMENOR,Utl.TT_OPMENORIG,Utl.TT_OPMAYOR,Utl.TT_OPMAYORIG,
				Utl.TT_OPDOBLEIGUAL,Utl.TT_OPDESIGUAL})){
			//vacio
			//TODO checkear que siguientes sean correctos!
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Expresion mal formada.\n"
					+ "Se esperaba un token dentro del grupo: + - < <= > >= == !=\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private void expMul()  throws SintacticException{
		expUn();
		expMulR();
	}
	private void expMulR() throws SintacticException {
		if (tokenAct.esTipo(new int[]{Utl.TT_OPMULT,Utl.TT_OPDIV})){
			opMul();
			expUn();
			expMulR();
		}
		else if(tokenAct.esTipo(new int[]
				{Utl.TT_PUNPUNTOCOMA,Utl.TT_PUNCOMA,Utl.TT_OPSUMA,Utl.TT_OPRESTA,Utl.TT_OPMENOR,Utl.TT_OPMENORIG,
				Utl.TT_OPMAYOR,Utl.TT_OPMAYORIG,Utl.TT_OPDOBLEIGUAL,Utl.TT_OPDESIGUAL,Utl.TT_OPANDDOBLE,
				Utl.TT_OPORDOBLE,Utl.TT_PUNPARENT_C,Utl.TT_PUNCORCH_C})){
			//vacio
			//siguientes: ; , + - < <= > >= == != && || ) ]
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Expresion mal formada.\n"
					+ "Se esperaba un token dentro del grupo: * / + - < <= > >= == != && || ) ; ,\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private void expUn() throws SintacticException {
		if (tokenAct.esTipo(new int[]{Utl.TT_OPSUMA,Utl.TT_OPRESTA,Utl.TT_OPNEGBOOL})){
			opUn();
			expUn();
		}
		else if(tokenAct.esTipo(new int[]{Utl.TPC_NULL,Utl.TPC_TRUE,Utl.TPC_FALSE,Utl.TT_LITENTERO,Utl.TT_LITCARACTER,Utl.TT_LITSTRING,Utl.TT_IDMETVAR,Utl.TT_PUNPARENT_A,Utl.TT_IDCLASE,Utl.TPC_THIS,Utl.TPC_NEW})){
			operando();
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Expresion mal formada.\n"
					+ "Se esperaba un token dentro del grupo: + - ! ( null true false litEntero litCaracter litString idMetVar idClase this new\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	} 
	private void opIgual() throws SintacticException {
		if (tokenAct.esTipo(Utl.TT_OPDOBLEIGUAL)){
			match(Utl.TT_OPDOBLEIGUAL);
		}
		else if (tokenAct.esTipo(Utl.TT_OPDESIGUAL)){
			match(Utl.TT_OPDESIGUAL);
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: == !=\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private void opComp() throws SintacticException {
		if (tokenAct.esTipo(Utl.TT_OPMENOR)){
			match(Utl.TT_OPMENOR);
		}
		else if (tokenAct.esTipo(Utl.TT_OPMENORIG)){
			match(Utl.TT_OPMENORIG);
		}
		else if (tokenAct.esTipo(Utl.TT_OPMAYOR)){
			match(Utl.TT_OPMAYOR);
		}
		else if (tokenAct.esTipo(Utl.TT_OPMAYORIG)){
			match(Utl.TT_OPMAYORIG);
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: < <= > >=\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private void opAd() throws SintacticException {
		if (tokenAct.esTipo(Utl.TT_OPSUMA)){
			match(Utl.TT_OPSUMA);
		}
		else if (tokenAct.esTipo(Utl.TT_OPRESTA)){
			match(Utl.TT_OPRESTA);
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: + -\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	} 
	private void opUn()  throws SintacticException{
		if (tokenAct.esTipo(Utl.TT_OPSUMA)){
			match(Utl.TT_OPSUMA);
		}
		else if (tokenAct.esTipo(Utl.TT_OPRESTA)){
			match(Utl.TT_OPRESTA);
		}
		else if (tokenAct.esTipo(Utl.TT_OPNEGBOOL)){
			match(Utl.TT_OPNEGBOOL);
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: + - !\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	} 
	private void opMul()  throws SintacticException{
		if (tokenAct.esTipo(Utl.TT_OPMULT)){
			match(Utl.TT_OPMULT);
		}
		else if (tokenAct.esTipo(Utl.TT_OPDIV)){
			match(Utl.TT_OPDIV);
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: * / \n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	} 
	private void operando() throws SintacticException {
		if (tokenAct.esTipo(new int[]{Utl.TPC_NULL,Utl.TPC_TRUE,Utl.TPC_FALSE,
				Utl.TT_LITENTERO,Utl.TT_LITCARACTER,Utl.TT_LITSTRING})){
			literal();
		}
		else if (tokenAct.esTipo(new int[]{Utl.TT_IDMETVAR,Utl.TT_IDCLASE,Utl.TT_PUNPARENT_A,
				Utl.TPC_THIS,Utl.TPC_NEW})){
			primario();
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: null true false litEntero litCaracter litString"
					+ "idMetVar idClase this new (\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private void literal()  throws SintacticException{
		if (tokenAct.esTipo(Utl.TPC_NULL)){
			match(Utl.TPC_NULL);
		}
		else if (tokenAct.esTipo(Utl.TPC_TRUE)){
			match(Utl.TPC_TRUE);
		}
		else if (tokenAct.esTipo(Utl.TPC_FALSE)){
			match(Utl.TPC_FALSE);
		}
		else if (tokenAct.esTipo(Utl.TT_LITENTERO)){
			match(Utl.TT_LITENTERO);
		}
		else if (tokenAct.esTipo(Utl.TT_LITCARACTER)){
			match(Utl.TT_LITCARACTER);
		}
		else if (tokenAct.esTipo(Utl.TT_LITSTRING)){
			match(Utl.TT_LITSTRING);
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: null true false litEntero litCaracter litString\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	} 
	private void primario() throws SintacticException {
		if (tokenAct.esTipo(Utl.TT_IDMETVAR)){
			match(Utl.TT_IDMETVAR);
			metodoVariable();
		}
		else if (tokenAct.esTipo(Utl.TT_PUNPARENT_A)){
			expresionParentizada();
		}
		else if (tokenAct.esTipo(Utl.TPC_THIS)){
			accesoThis();
		}
		else if (tokenAct.esTipo(Utl.TT_IDCLASE)){
			llamadaMetodoEstatico();
		}
		else if (tokenAct.esTipo(Utl.TPC_NEW)){
			llamadaCtor();
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: idMetVar idClase this new (\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private void metodoVariable() throws SintacticException {
		if (tokenAct.esTipo(Utl.TT_PUNPARENT_A)){
			argsActuales();
			encadenado();
		}
		else if (tokenAct.esTipo(new int[]{Utl.TT_PUNPUNTO,Utl.TT_PUNCORCH_A,
				Utl.TT_PUNPUNTOCOMA,Utl.TT_PUNCOMA,Utl.TT_ASIGIGUAL,Utl.TT_OPSUMA,Utl.TT_OPRESTA,Utl.TT_OPMULT,
				Utl.TT_OPDIV,Utl.TT_OPMENOR,Utl.TT_OPMENORIG,Utl.TT_OPMAYOR,Utl.TT_OPMAYORIG,Utl.TT_OPDOBLEIGUAL,
				Utl.TT_OPDESIGUAL,Utl.TT_OPANDDOBLE,Utl.TT_OPORDOBLE,Utl.TT_PUNPARENT_C,Utl.TT_PUNCORCH_C})){
			//primeros: . [ + siguientes: ; , = + - * / < <= > >= == != && || ) ]
			encadenado();
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: ( . [ ; , = + - * / < <="
					+ " > >= == != && || ) ]\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private void expresionParentizada()  throws SintacticException{
		match(Utl.TT_PUNPARENT_A);
		expresion();
		match(Utl.TT_PUNPARENT_C);
		encadenado();
	}
	private void encadenado() throws SintacticException {
		if (tokenAct.esTipo(Utl.TT_PUNPUNTO)){
			match(Utl.TT_PUNPUNTO);
			match(Utl.TT_IDMETVAR);
			acceso();
		}
		else if (tokenAct.esTipo(Utl.TT_PUNCORCH_A)){
			accesoArregloEncadenado();
		}
		else if(tokenAct.esTipo(new int[]
				{Utl.TT_PUNPUNTOCOMA,Utl.TT_PUNCOMA,Utl.TT_ASIGIGUAL,Utl.TT_OPSUMA,Utl.TT_OPRESTA,Utl.TT_OPMULT,
				Utl.TT_OPDIV,Utl.TT_OPMENOR,Utl.TT_OPMENORIG,Utl.TT_OPMAYOR,Utl.TT_OPMAYORIG,Utl.TT_OPDOBLEIGUAL,
				Utl.TT_OPDESIGUAL,Utl.TT_OPANDDOBLE,Utl.TT_OPORDOBLE,Utl.TT_PUNPARENT_C,Utl.TT_PUNCORCH_C})){
			//vacio
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: . [ ; , = + - * / < <="
					+ " > >= == != && || ) ]\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));			
		}
	}
	private void acceso() throws SintacticException {
		if (tokenAct.esTipo(Utl.TT_PUNPARENT_A)){
			llamadaMetodoEncadenado();
		}
		else if (tokenAct.esTipo(new int[]{Utl.TT_PUNPUNTO,Utl.TT_PUNCORCH_A})){
			accesoVarEncadenado();
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: . ( [\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));	
		}
	}
	private void accesoThis() throws SintacticException {
		match(Utl.TPC_THIS);
		encadenado();
	}
	private void accesoVar()  throws SintacticException{
		match(Utl.TT_IDMETVAR);
		encadenado();
	}
	private void llamadaMetodo()  throws SintacticException{
		match(Utl.TT_IDMETVAR);
		argsActuales();
		encadenado();
	}
	private void llamadaMetodoEstatico()  throws SintacticException{
		match(Utl.TT_IDCLASE);
		match(Utl.TT_PUNPUNTO);
		llamadaMetodo();
	}
	private void llamadaCtor()  throws SintacticException{
		match(Utl.TPC_NEW);
		llamadaCtorR();
	}
	private void llamadaCtorR()  throws SintacticException{
		if (tokenAct.esTipo(Utl.TT_IDCLASE)){
			match(Utl.TT_IDCLASE);
			argsActuales();
			encadenado();
		}
		else if (tokenAct.esTipo(new int[]{Utl.TPC_BOOLEAN,Utl.TPC_CHAR,Utl.TPC_INT})){
			//aca va logro de arreglo de todo
			tipoPrimitivo();
			match(Utl.TT_PUNCORCH_A);
			expresion();
			match(Utl.TT_PUNCORCH_C);
			encadenado();
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: idClase boolean char int\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));	
		}
	}
	private void argsActuales() throws SintacticException {
		match(Utl.TT_PUNPARENT_A);
		listaExpresiones();
		match(Utl.TT_PUNPARENT_C);
	}
	private void listaExpresiones() throws SintacticException {
		if (tokenAct.esTipo(new int[]{Utl.TT_OPSUMA,Utl.TT_OPRESTA,Utl.TT_OPNEGBOOL,Utl.TPC_NULL,
				Utl.TPC_TRUE,Utl.TPC_FALSE,Utl.TT_LITENTERO,Utl.TT_LITCARACTER,Utl.TT_LITSTRING,
				Utl.TT_IDMETVAR,Utl.TT_PUNPARENT_A,Utl.TT_IDCLASE,Utl.TPC_THIS,Utl.TPC_NEW})){
			expresion();
			listaExp();
		}
		else if (tokenAct.esTipo(Utl.TT_PUNPARENT_C)){
			//vacio
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: + - ! null true false litEntero litCaracter litString "
					+ "idMetVar idClase this new ( )\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));	
		}
	}
	private void listaExp() throws SintacticException {
		if (tokenAct.esTipo(Utl.TT_PUNCOMA)){
			match(Utl.TT_PUNCOMA);
			expresion();
			listaExp();
		}
		else if (tokenAct.esTipo(Utl.TT_PUNPARENT_C)){
			//vacio
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: , )\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private void llamadaMetodoEncadenado() throws SintacticException {
		argsActuales();
		encadenado();
	}
	private void accesoVarEncadenado() throws SintacticException{
		encadenado();
	}
	private void accesoArregloEncadenado() throws SintacticException {
		match(Utl.TT_PUNCORCH_A);
		expresion();
		match(Utl.TT_PUNCORCH_C);
		encadenado();
	}	
}
