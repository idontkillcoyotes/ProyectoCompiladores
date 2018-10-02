import java.util.ArrayList;

public class AnalizadorSintactico {
	
	private AnalizadorLexico alex;
	private Token tokenAct;
	private ManejadorTS mts;
	
	public AnalizadorSintactico (AnalizadorLexico al){
		this.alex=al;
		this.mts=new ManejadorTS();
		try {
			this.tokenAct = alex.nextToken();
		} catch (LexicoException e) {
			System.out.println(e.getMessage());
		}		
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
	
	private void consumirToken() {
		try {
			tokenAct = alex.nextToken();
		}
		catch (LexicoException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void start() throws SintacticException, SemanticException{
		inicio();
		mts.consolidarTS();
	}
	
	private void inicio() throws SintacticException, SemanticException{
		//Inicio -> Clase Clases $
		clase();
		clases();
		match(Utl.TT_FINARCHIVO);		
	}
	
	private void clases() throws SintacticException, SemanticException{
		//Clases -> Clase Clases | epsilon
		if (tokenAct.esTipo(Utl.TPC_CLASS)){
			clase();
			clases();
		}
		else if (tokenAct.esTipo(Utl.TT_FINARCHIVO)){
			//vacio
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Declaracion de clase mal formada.\n"
					+"Se esperaba un token de tipo: class o FIN_ARCHIVO.\n"
					+"Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}		
	}
	
	private void clase() throws SintacticException,SemanticException{
		//Clase -> class idClase Herencia { Miembros }
		//If para reportar mejor el error:
		if (tokenAct.esTipo(Utl.TPC_CLASS)){
			match(Utl.TPC_CLASS);
			
			//Guardo el token de nombre de clase
			Token tn=tokenAct;
			match(Utl.TT_IDCLASE);										
			//Guardo el nombre de la clase padre
			Token padre=herencia();
			
			mts.crearClase(tn, padre);
			
			match(Utl.TT_PUNLLAVE_A);
			miembros();
			match(Utl.TT_PUNLLAVE_C);
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Declaracion de clase mal formada.\n"
					+"Se esperaba un token de tipo: class.\n"
					+"Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	//Retorna el token de la clase padre, o un token nuevo con 'Object'
	private Token herencia() throws SintacticException{
		//Herencia -> extends idClase | epsilon
		Token ret=new Token(Utl.TT_IDCLASE,"Object",0,0);
		if (tokenAct.esTipo(Utl.TPC_EXTENDS)){
			match(Utl.TPC_EXTENDS);
			ret=tokenAct;
			match(Utl.TT_IDCLASE);			
		}
		else if (tokenAct.esTipo(Utl.TT_PUNLLAVE_A)){
			//vacio
		}else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: extends (\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
		return ret;
	}
	private void miembros() throws SintacticException, SemanticException{
		//Miembros -> Miembro Miembros | epsilon
		if ( tokenAct.esTipo(new int[]{Utl.TPC_PUBLIC,Utl.TPC_PRIVATE,Utl.TPC_STATIC,Utl.TPC_DYNAMIC,Utl.TT_IDCLASE}) ){
			miembro();
			miembros();
		}
		else if (tokenAct.esTipo(Utl.TT_PUNLLAVE_C)){
			//vacio
		}else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Miembro mal formado.\n"
					+ "Se esperaba un token dentro del grupo: public private static dynamic idClase\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}			
	}
	private void miembro() throws SintacticException, SemanticException{
		//Miembro -> Atributo | Ctor | Metodo
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
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Miembro mal formado.\n"
					+"Se esperaba un token dentro del grupo: public private static dynamic idClase\n"
					+"Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private void atributo() throws SintacticException, SemanticException{
		//Atributo -> Visibilidad Tipo ListaDecVars ;
		
		//Obtengo visibilidad
		Visibilidad vis=visibilidad();
		
		//Obtengo tipo
		Tipo tipo=tipo();
		
		//Creo lista de parametros para pasarle a listaDecVars
		ArrayList<EParametro> lista=new ArrayList<EParametro>();		
		listaDecVars(tipo,lista);		
		
		//Agrego los atributos a la clase
		mts.crearAtributos(lista, vis);
		
		//Comentar la siguiente llamada si es que hay problemas
		inicializacion();		
		match(Utl.TT_PUNPUNTOCOMA);
	}
	//Aca empieza el logro de inicializacion en declaracion
	//Comentar este metedo si es que hay problemas
	private void inicializacion() throws SintacticException {
		//Inicializacion -> = Expresion | epsilon
		if (tokenAct.esTipo(Utl.TT_ASIGIGUAL)){
			match(Utl.TT_ASIGIGUAL);
			expresion();
		}
		else if (tokenAct.esTipo(Utl.TT_PUNPUNTOCOMA)){
			//vacio
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: = ;\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private void metodo() throws SintacticException, SemanticException{
		//Metodo -> FormaMetodo TipoMetodo idMetVar ArgsFormales Bloque
		
		//Obtengo forma de metodo
		FormaMetodo f=formaMetodo();
		
		//Obtengo tipo de retorno de metodo
		Tipo tipo=tipoMetodo();
		
		//Guardo el token del nombre del metodo
		Token tn=tokenAct;
		match(Utl.TT_IDMETVAR);
		
		//Creo metodo
		mts.crearMetodo(tn, f, tipo);
			
		argsFormales();
		
		//Agrego metodo a clase cuando ya tengo todos sus parametros
		mts.agregarMetodo();	
		
		bloque();
	}
	private void ctor() throws SintacticException, SemanticException{
		//Ctor -> idClase ArgsFormales Bloque		
		if (tokenAct.esTipo(Utl.TT_IDCLASE)){
			//Guardo el token de nombre del constructor
			Token tn=tokenAct;
			consumirToken();
			
			//Creo constructor
			mts.crearConstructor(tn);
			
			argsFormales();
			
			//Agrego constructor a clase cuando ya tengo todos sus parametros
			mts.agregarConstructor();
			
			bloque();
		}
		else {
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Constructor mal formado.\n"
					+"Se esperaba un token: idClase\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private void argsFormales() throws SintacticException, SemanticException {
		//ArgsFormales -> ( ListaArg )
		//If para retornar un mejor error
		if (tokenAct.esTipo(Utl.TT_PUNPARENT_A)){
			match(Utl.TT_PUNPARENT_A);
			listaArg();
			match(Utl.TT_PUNPARENT_C);
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Metodo mal formado. Argumentos inexistentes.\n"
					+"Se esperaba un token: (\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private void listaArg()  throws SintacticException, SemanticException{
		//ListaArg -> Arg ArgFormales | epsilon
		if (tokenAct.esTipo(new int[]{Utl.TPC_BOOLEAN,Utl.TPC_CHAR,Utl.TPC_INT,Utl.TT_IDCLASE,Utl.TPC_STRING})){
			arg();
			argFormales();
		}
		else if (tokenAct.esTipo(Utl.TT_PUNPARENT_C)){
			//vacio
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Lista de argumentos mal formada.\n"
					+"Se esperaba un token dentro del grupo: boolean char int String idClase )\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private void argFormales()  throws SintacticException, SemanticException{
		//ArgFormales -> , Arg ArgFormales | epsilon
		if (tokenAct.esTipo(Utl.TT_PUNCOMA)){
			match(Utl.TT_PUNCOMA);
			arg();
			argFormales();
		}
		else if (tokenAct.esTipo(Utl.TT_PUNPARENT_C)){
			//vacio
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Lista de argumentos mal formada.\n"
					+"Se esperaba un token dentro del grupo: , )\n"
					+"Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private void arg() throws SintacticException, SemanticException {
		//Arg -> Tipo idMetVar
		
		//Obtengo el tipo del parametro
		Tipo tipo=tipo();
		//if para reconocer mejor un error sintactico
		if (tokenAct.esTipo(Utl.TT_IDMETVAR)){			
			//Guardo el token del nombre del parametro
			Token tn=tokenAct;
			consumirToken();
			//Agrego parametro al miembro
			mts.crearParametro(tn, tipo);			
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token de tipo: idMetVar\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private FormaMetodo formaMetodo() throws SintacticException {
		//FormaMetodo -> static
		//FormaMetodo -> dynamic
		if (tokenAct.esTipo(Utl.TPC_STATIC)){
			match(Utl.TPC_STATIC);
			return FormaMetodo.fStatic;
		}
		else if (tokenAct.esTipo(Utl.TPC_DYNAMIC)){
			match(Utl.TPC_DYNAMIC);
			return FormaMetodo.fDynamic;
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: static dynamic\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private Visibilidad visibilidad()  throws SintacticException{
		//Visibilidad -> public 
		//Visibilidad -> private
		if (tokenAct.esTipo(Utl.TPC_PUBLIC)){
			match(Utl.TPC_PUBLIC);
			return Visibilidad.vPublic;
		}
		else if (tokenAct.esTipo(Utl.TPC_PRIVATE)){
			match(Utl.TPC_PRIVATE);
			return Visibilidad.vPrivate;
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: public private\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private Tipo tipoMetodo()  throws SintacticException{
		//TipoMetodo -> Tipo
		//TipoMetodo -> void
		if (tokenAct.esTipo(Utl.TPC_VOID)){
			Token tn=tokenAct;
			consumirToken();
			return new TipoSimple(tn);
		}
		else if (tokenAct.esTipo(new int[]{Utl.TPC_BOOLEAN,Utl.TPC_CHAR,Utl.TPC_INT,Utl.TT_IDCLASE,Utl.TPC_STRING})){
			return tipo();
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Metodo mal formado. Tipo de metodo inexistente.\n"
					+"Se esperaba un token dentro del grupo: void boolean char int String idClase\n"
					+"Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private Tipo tipo() throws SintacticException {
		//Tipo -> boolean PosibleArreglo | char PosibleArreglo | int PosibleArreglo | idClase | String
		if (tokenAct.esTipo(Utl.TPC_BOOLEAN)){
			Token tn=tokenAct;
			consumirToken();
			return posibleArreglo(new TipoSimple(tn));
		}
		else if (tokenAct.esTipo(Utl.TPC_CHAR)){
			Token tn=tokenAct;
			consumirToken();
			return posibleArreglo(new TipoSimple(tn));
		}
		else if (tokenAct.esTipo(Utl.TPC_INT)){
			Token tn=tokenAct;
			consumirToken();
			return posibleArreglo(new TipoSimple(tn));
		}
		else if (tokenAct.esTipo(Utl.TT_IDCLASE)){
			Token tk=tokenAct;
			consumirToken();
			//para logro posibleArreglo();
			//Comentar si es que hay problemas
			return posibleArreglo(new TipoClase(tk));
		}
		else if (tokenAct.esTipo(Utl.TPC_STRING)){
			Token tn=tokenAct;
			consumirToken();
			//para logro posibleArreglo();
			//Comentar si es que hay problemas
			return posibleArreglo(new TipoSimple(tn));
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Tipo invalido.\n"
					+"Se esperaba un token dentro del grupo: boolean char int String idClase\n"
					+"Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private Tipo posibleArreglo(Tipo tipo) throws SintacticException {
		//PosibleArreglo -> [ ] | epsilon
		if (tokenAct.esTipo(Utl.TT_PUNCORCH_A)){
			match(Utl.TT_PUNCORCH_A);
			
			if (tokenAct.esTipo(Utl.TT_PUNCORCH_C)){
				match(Utl.TT_PUNCORCH_C);
				//seteo que es arreglo
				tipo.setArreglo();
				//retorno
				return tipo;
				//La siguiente llamada permite definir matrices
				//posibleArreglo();
			}
			else{
				throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Declaracion mal formada.\n"
						+"Se esperaba un token: ]\n"
						+"Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
			}
		}
		else if (tokenAct.esTipo(Utl.TT_IDMETVAR)){
			//vacio
			return tipo;
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Declaracion mal formada.\n"
					+"Se esperaba un token dentro del grupo: [ idMetVar\n"
					+"Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	//Este metodo no es usado si hago el logro de arreglos de todo tipo
	//Descomentar si es que hay problemas
	/*
	private void tipoPrimitivo() throws SintacticException {
		//TipoPrimitivo -> boolean | char | int
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
	*/
	private void listaDecVars(Tipo tipo,ArrayList<EParametro> l)  throws SintacticException, SemanticException{
		//ListaDecVars -> idMetVar ListaDV
		//If para reportar mejor el error
		if (tokenAct.esTipo(Utl.TT_IDMETVAR)){
			//Obtengo el token nombre de la variable
			Token t=tokenAct;
			match(Utl.TT_IDMETVAR);
			
			//creo la variable con el tipo pasado por parametro
			EParametro var=new EParametro(t,tipo);
			//agrego la var a la lista
			l.add(var);
			
			listaDV(tipo,l);
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Declaracion mal formada.\n"
					+"Se esperaba un token: idMetVar\n"
					+"Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private void listaDV(Tipo tipo,ArrayList<EParametro> l) throws SintacticException, SemanticException {
		//ListaDV -> , idMetVar ListaDV | epsilon
		if (tokenAct.esTipo(Utl.TT_PUNCOMA)){
			match(Utl.TT_PUNCOMA);
			if (tokenAct.esTipo(Utl.TT_IDMETVAR)){
				//Obtengo el token nombre de la variable
				Token t=tokenAct;
				match(Utl.TT_IDMETVAR);
				
				//creo la variable con el tipo pasado por parametro
				EParametro var=new EParametro(t,tipo);
				//agrego la var a la lista
				l.add(var);
				
				listaDV(tipo,l);
			}
			else{
				throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Declaracion mal formada.\n"
						+"Se esperaba un token: idMetVar\n"
						+"Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
			}
		}
		//TODO Logro de inicializacion en declaracion, los siguientes de ListaDV con el logro cambian
		//viejos siguientes: ;
		//nuevos siguientes: ; = } idClase public private static dynamic if while return boolean char
		// 					 int String { ( idMetVar this else
		//Tengo dudas si los nuevos siguientes son esos, o son solo: = ;
		else if (tokenAct.esTipo(new int[]{Utl.TT_PUNPUNTOCOMA,Utl.TT_ASIGIGUAL,Utl.TT_PUNLLAVE_C,Utl.TT_IDCLASE,Utl.TPC_PUBLIC,Utl.TPC_PRIVATE,
			Utl.TPC_STATIC,Utl.TPC_DYNAMIC,Utl.TPC_IF,Utl.TPC_WHILE,Utl.TPC_RETURN,Utl.TPC_BOOLEAN,Utl.TPC_CHAR,Utl.TPC_INT,Utl.TPC_STRING,
			Utl.TT_PUNLLAVE_A,Utl.TT_PUNPARENT_A,Utl.TT_IDMETVAR,Utl.TPC_THIS,Utl.TPC_ELSE})){
			//vacio
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Declaracion mal formada.\n"
					+"Se esperaba un token dentro del grupo: , ;\n"
					+"Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}	
	private void bloque() throws SintacticException, SemanticException {
		//Bloque -> { Sentencias }
		match(Utl.TT_PUNLLAVE_A);
		sentencias();
		match(Utl.TT_PUNLLAVE_C);
	}
	private void sentencias() throws SintacticException, SemanticException {
		//Sentencias -> Sentencia Sentencias | epsilon
		if(tokenAct.esTipo(new int[]{Utl.TT_PUNPUNTOCOMA,Utl.TPC_IF,Utl.TPC_WHILE,Utl.TPC_RETURN,Utl.TT_IDMETVAR,Utl.TPC_THIS,Utl.TPC_BOOLEAN,Utl.TPC_CHAR,Utl.TPC_INT,Utl.TT_IDCLASE,Utl.TPC_STRING,Utl.TT_PUNPARENT_A,Utl.TT_PUNLLAVE_A})){
			sentencia();
			sentencias();
		}
		else if (tokenAct.esTipo(Utl.TT_PUNLLAVE_C)){
			//vacio
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Sentencia mal formada.\n"
					+"Se esperaba un token dentro del grupo: ; if while return this idMetVar idClase boolean char int String ( {\n"
					+"Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}	
	}
	private void sentencia() throws SintacticException, SemanticException {
		//Sentencia -> ; | if ( Expresion ) Sentencia SentenciaElse | while ( Expresion ) Sentencia | return Expresiones ;
		//Sentencia -> Asignacion ; | SentenciaLlamada ; | Tipo ListaDecVars ; | Bloque
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
			//tipo listadecvars inicializacion ;
			Tipo t = tipo();
			ArrayList<EParametro> vars=new ArrayList<EParametro>();
			listaDecVars(t,vars);
			//Logro de inicializacion en declaracion
			//Comentar siguiente llamada si es que hay problemas
			inicializacion();
			match(Utl.TT_PUNPUNTOCOMA);
		}
		else if (tokenAct.esTipo(Utl.TT_PUNLLAVE_A)){
			//bloque
			bloque();
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Sentencia mal formada.\n"
					+ "Se esperaba un token dentro del grupo: { ( ; if while return this boolean char int idClase idMetVar String\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private void sentenciaElse()  throws SintacticException, SemanticException{
		//SentenciaElse -> else Sentencia | epsilon
		if (tokenAct.esTipo(Utl.TPC_ELSE)){
			match(Utl.TPC_ELSE);
			sentencia();
		}
		else if(tokenAct.esTipo(new int[]{Utl.TPC_ELSE,Utl.TT_PUNLLAVE_C,Utl.TT_PUNPUNTOCOMA,Utl.TPC_IF,
				Utl.TPC_WHILE,Utl.TPC_RETURN,Utl.TPC_BOOLEAN,Utl.TPC_CHAR,Utl.TPC_INT,Utl.TT_IDCLASE,
				Utl.TPC_STRING,Utl.TT_PUNLLAVE_A,Utl.TT_PUNPARENT_A,Utl.TT_IDMETVAR,Utl.TPC_THIS})){
			//siguientes: else } ; if while return boolean char int idClase String { ( idMetVar this
			//vacio
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Sentencia mal formada.\n"
					+ "Se esperaba un token dentro del grupo: else ; if while return this boolean char int String idMetVar idClase ( {\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private void expresiones()  throws SintacticException{
		//Expresiones -> Expresion | epsilon
		if (tokenAct.esTipo(new int[]{Utl.TT_OPSUMA,Utl.TT_OPRESTA,Utl.TT_OPNEGBOOL,Utl.TPC_NULL,Utl.TPC_TRUE,Utl.TPC_FALSE,Utl.TT_LITENTERO,Utl.TT_LITCARACTER,Utl.TT_LITSTRING,Utl.TT_IDMETVAR,Utl.TT_PUNPARENT_A,Utl.TT_IDCLASE,Utl.TPC_THIS,Utl.TPC_NEW})){
			expresion();
		}
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
		//Asignacion -> AccesoVar = Expresion | AccesoThis = Expresion
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
		//SentenciaLlamada -> ( Primario )
		match(Utl.TT_PUNPARENT_A);
		primario();
		match(Utl.TT_PUNPARENT_C);
	}
	private void expOr()  throws SintacticException{
		//ExpOr -> ExpAnd ExpOrR
		expAnd();
		expOrR();
	}
	private void expresion()  throws SintacticException{
		//Expresion -> ExpOr
		expOr();
	}
	private void expOrR()  throws SintacticException{
		//ExpOrR -> || ExpAnd ExpOrR | epsilon
		if (tokenAct.esTipo(Utl.TT_OPORDOBLE)){
			match(Utl.TT_OPORDOBLE);
			expAnd();
			expOrR();
		}
		else if(tokenAct.esTipo(new int[]{Utl.TT_PUNPUNTOCOMA,Utl.TT_PUNPARENT_C,Utl.TT_PUNCORCH_C,Utl.TT_PUNCOMA})){
			//siguientes ; , ) ]
			//vacio
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Expresion mal formada.\n"
					+ "Se esperaba un token dentro del grupo: || ; , ) ]\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private void expAnd() throws SintacticException{
		//ExpAnd -> ExpIg ExpAndR
		expIg();
		expAndR();
	}
	private void expAndR()  throws SintacticException{
		//ExpAndR -> && ExpIg ExpAndR | epsilon
		if (tokenAct.esTipo(Utl.TT_OPANDDOBLE)){
			match(Utl.TT_OPANDDOBLE);
			expIg();
			expAndR();
		}
		else if(tokenAct.esTipo(new int[]{Utl.TT_PUNPUNTOCOMA,Utl.TT_PUNCOMA,Utl.TT_OPORDOBLE,
				Utl.TT_PUNPARENT_C,Utl.TT_PUNCORCH_C})){
			//siguientes: ; , || ) ] 
			//vacio
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Expresion mal formada.\n"
					+ "Se esperaba un token dentro del grupo: && || ; , ) ]\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private void expIg()  throws SintacticException{
		//ExpIg -> ExpComp ExpIgR
		expComp();
		expIgR();
	} 
	private void expIgR() throws SintacticException {
		//ExpIgR -> OpIgual ExpComp ExpIgR | epsilon
		if (tokenAct.esTipo(new int[]{Utl.TT_OPDOBLEIGUAL,Utl.TT_OPDESIGUAL})){
			opIgual();
			expComp();
			expIgR();
		}
		else if(tokenAct.esTipo(new int[]{Utl.TT_PUNPUNTOCOMA,Utl.TT_PUNCOMA,Utl.TT_OPANDDOBLE,Utl.TT_OPORDOBLE,
				Utl.TT_PUNPARENT_C,Utl.TT_PUNCORCH_C})){
			//siguientes: ; , && || ) ]
			//vacio
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Expresion mal formada.\n"
					+ "Se esperaba un token dentro del grupo: == != ; , && || ) ]\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private void expComp()  throws SintacticException{
		//ExpComp -> ExpAd ExpCompR
		expAd();
		expCompR();
	}
	private void expCompR() throws SintacticException {
		//ExpCompR -> OpComp ExpAd
		if (tokenAct.esTipo(new int[]{Utl.TT_OPMENOR,Utl.TT_OPMENORIG,Utl.TT_OPMAYOR,Utl.TT_OPMAYORIG})){
			opComp();
			expAd();
		}
		else if(tokenAct.esTipo(new int[]{Utl.TT_PUNPUNTOCOMA,Utl.TT_PUNCOMA,Utl.TT_OPDOBLEIGUAL,Utl.TT_OPDESIGUAL,
				Utl.TT_OPANDDOBLE,Utl.TT_OPORDOBLE,Utl.TT_PUNPARENT_C,Utl.TT_PUNCORCH_C})){
			//siguientes: ; , == != && || ) ] 
			//vacio
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Expresion mal formada.\n"
					+ "Se esperaba un token dentro del grupo: < <= > >= ; , == != && || ) ]\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private void expAd()  throws SintacticException{
		//ExpAd -> ExpMul ExpAdR
		expMul();
		expAdR();
	} 
	private void expAdR()  throws SintacticException{
		//ExpAdR -> OpAd ExpMul ExpAdR | epsilon
		if (tokenAct.esTipo(new int[]{Utl.TT_OPSUMA,Utl.TT_OPRESTA})){
			opAd();
			expMul();
			expAdR();
		}
		else if(tokenAct.esTipo(new int[]{Utl.TT_PUNPUNTOCOMA,Utl.TT_PUNCOMA,Utl.TT_OPMENOR,Utl.TT_OPMENORIG,
				Utl.TT_OPMAYOR,Utl.TT_OPMAYORIG,Utl.TT_OPDOBLEIGUAL,Utl.TT_OPDESIGUAL,Utl.TT_OPANDDOBLE,
				Utl.TT_OPORDOBLE,Utl.TT_PUNPARENT_C,Utl.TT_PUNCORCH_C})){
			//siguientes: ; , < > <= >= == != && || ) ] 
			//vacio
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Expresion mal formada.\n"
					+ "Se esperaba un token dentro del grupo: + - ; , < <= > >= == != && || ) ]\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private void expMul()  throws SintacticException{
		//ExpMul -> ExpUn ExpMulR
		expUn();
		expMulR();
	}
	private void expMulR() throws SintacticException {
		//ExpMulR -> OpMul ExpUn ExpMulR | epsilon
		if (tokenAct.esTipo(new int[]{Utl.TT_OPMULT,Utl.TT_OPDIV})){
			opMul();
			expUn();
			expMulR();
		}
		else if(tokenAct.esTipo(new int[]
				{Utl.TT_PUNPUNTOCOMA,Utl.TT_PUNCOMA,Utl.TT_OPSUMA,Utl.TT_OPRESTA,Utl.TT_OPMENOR,Utl.TT_OPMENORIG,
				Utl.TT_OPMAYOR,Utl.TT_OPMAYORIG,Utl.TT_OPDOBLEIGUAL,Utl.TT_OPDESIGUAL,Utl.TT_OPANDDOBLE,
				Utl.TT_OPORDOBLE,Utl.TT_PUNPARENT_C,Utl.TT_PUNCORCH_C})){
			//siguientes: ; , + - < <= > >= == != && || ) ] 
			//vacio
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Expresion mal formada.\n"
					+ "Se esperaba un token dentro del grupo: * /  ; , + - < <= > >= == != && || )\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private void expUn() throws SintacticException {
		//ExpUn -> OpUn ExpUn
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
		//OpIgual -> == | !=
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
		//OpComp -> < | > | <= | >=
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
		//OpAd -> + | -
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
		//OpUn -> + | - | !
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
		//OpMul -> * | /
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
		//Operando -> Literal | Primario
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
		//Literal -> null | true | false | intLiteral | charLiteral | stringLiteral
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
		//Primario -> idMetVar MetodoVariable | ExpresionParentizada | AccesoThis | LlamadaMetodoEstatico | LlamadaCtor
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
		//MetodoVariable -> ArgsActuales Encadenado | Encadenado
		if (tokenAct.esTipo(Utl.TT_PUNPARENT_A)){
			argsActuales();
			encadenado();
		}
		else if (tokenAct.esTipo(new int[]{Utl.TT_PUNPUNTO,Utl.TT_PUNCORCH_A,
				Utl.TT_PUNPUNTOCOMA,Utl.TT_PUNCOMA,Utl.TT_ASIGIGUAL,Utl.TT_OPSUMA,Utl.TT_OPRESTA,Utl.TT_OPMULT,
				Utl.TT_OPDIV,Utl.TT_OPMENOR,Utl.TT_OPMENORIG,Utl.TT_OPMAYOR,Utl.TT_OPMAYORIG,Utl.TT_OPDOBLEIGUAL,
				Utl.TT_OPDESIGUAL,Utl.TT_OPANDDOBLE,Utl.TT_OPORDOBLE,Utl.TT_PUNPARENT_C,Utl.TT_PUNCORCH_C})){
			//primeros y siguientes de encadenado
			//primeros: . [ 
			//siguientes: ; , = + - * / < <= > >= == != && || ) ]
			encadenado();
		}
		else{
			//este error seria siempre de expresiones mal formadas?
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Expresion mal formada.\n"
					+"Se esperaba un token dentro del grupo: ( ) [ ] ; , . = + - * / < <= > >= == != && ||\n"
					+"Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private void expresionParentizada()  throws SintacticException{
		//ExpresionParentizada -> ( Expresion ) Encadenado
		match(Utl.TT_PUNPARENT_A);
		expresion();
		match(Utl.TT_PUNPARENT_C);
		encadenado();
	}
	private void encadenado() throws SintacticException {
		//Encadenado -> . idMetVar Acceso | AccesoArregloEncadenado | epsilon
		if (tokenAct.esTipo(Utl.TT_PUNPUNTO)){
			match(Utl.TT_PUNPUNTO);
			//TODO agregar un if para reportar mejor el error?
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
			//siguientes: ; , = + - * / < <= > >= == != && || ) ] 
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
		//Acceso -> LlamadaMetodoEncadenado | AccesoVarEncadenado
		if (tokenAct.esTipo(Utl.TT_PUNPARENT_A)){
			llamadaMetodoEncadenado();
		}
		else if (tokenAct.esTipo(new int[]{Utl.TT_PUNPUNTO,Utl.TT_PUNCORCH_A,Utl.TT_PUNPUNTOCOMA,
				Utl.TT_PUNCOMA,Utl.TT_ASIGIGUAL,Utl.TT_OPSUMA,Utl.TT_OPRESTA,Utl.TT_OPMULT,
				Utl.TT_OPDIV,Utl.TT_OPMENOR,Utl.TT_OPMENORIG,Utl.TT_OPMAYOR,Utl.TT_OPMAYORIG,
				Utl.TT_OPDOBLEIGUAL,Utl.TT_OPDESIGUAL,Utl.TT_OPANDDOBLE,Utl.TT_OPORDOBLE,
				Utl.TT_PUNPARENT_C,Utl.TT_PUNCORCH_C})){
			//primeros y siguientes de encadenado:
			//primeros: . [
			//siguientes: ; , = + - * / < > <= >= == != && || ) ]
			accesoVarEncadenado();
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: ( ) [ ] . ; , = + - * / < > <= >= == != && ||\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));	
		}
	}
	private void accesoThis() throws SintacticException {
		//AccesoThis -> this Encadenado
		match(Utl.TPC_THIS);
		encadenado();
	}
	private void accesoVar()  throws SintacticException{
		//AccesoVar -> idMetVar Encadenado
		match(Utl.TT_IDMETVAR);
		encadenado();
	}
	private void llamadaMetodo()  throws SintacticException{
		//LlamadaMetodo -> idMetVar ArgsActuales Encadenado
		match(Utl.TT_IDMETVAR);
		argsActuales();
		encadenado();
	}
	private void llamadaMetodoEstatico()  throws SintacticException{
		//LlamadaMetodoEstatico -> idClase . LlamadaMetodo
		match(Utl.TT_IDCLASE);
		match(Utl.TT_PUNPUNTO);
		llamadaMetodo();
	}
	private void llamadaCtor()  throws SintacticException{
		//LlamadaCtor -> new LlamadaCtorR
		match(Utl.TPC_NEW);
		llamadaCtorR();
	}
	private void llamadaCtorR()  throws SintacticException{
		//LlamadaCtorR -> idClase LlamadaCtorRIDClase | TipoArreglo [ Expresion ] Encadenado
		if (tokenAct.esTipo(Utl.TT_IDCLASE)){
			match(Utl.TT_IDCLASE);
			//Logro de arreglo de todo, necesito un nuevo metodo
			//Comentar la siguietne y descomentar las ultimas llamadas si es que hay problemas
			llamadaCtorRIDClase();
			//argsActuales();
			//encadenado();
		}
		else if (tokenAct.esTipo(new int[]{Utl.TPC_BOOLEAN,Utl.TPC_CHAR,Utl.TPC_INT,Utl.TPC_STRING})){
			//Logro de arreglo de todo, comentar la siguiente y descomentar llamada a tipoprimitivo si es que hay problemas
			tipoArreglo();
			//tipoPrimitivo();
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
	//Metodo para logro de arreglo de todo, comentar si es que hay problemas
	private void llamadaCtorRIDClase() throws SintacticException  {
		//LlamadaCtorRIDClase -> ArgsActuales Encadenado | [ Expresion ] Encadenado
		if (tokenAct.esTipo(Utl.TT_PUNCORCH_A)){
			match(Utl.TT_PUNCORCH_A);
			expresion();
			match(Utl.TT_PUNCORCH_C);
			encadenado();
		}
		else if (tokenAct.esTipo(Utl.TT_PUNPARENT_A)){
			argsActuales();
			encadenado();
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: [ (\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));	
		}
	}
	//Metodo para logro de arreglo de todo, comentar si es que hay problemas
	private void tipoArreglo() throws SintacticException {
		//TipoArreglo -> char | int | boolean | String
		if (tokenAct.esTipo(Utl.TPC_CHAR)){
			match(Utl.TPC_CHAR);
		}
		else if (tokenAct.esTipo(Utl.TPC_BOOLEAN)){
			match(Utl.TPC_BOOLEAN);
		}
		else if (tokenAct.esTipo(Utl.TPC_INT)){
			match(Utl.TPC_INT);
		}
		else if (tokenAct.esTipo(Utl.TPC_STRING)){
			match(Utl.TPC_STRING);
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: boolean char int String\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));	
		}		
	}
	private void argsActuales() throws SintacticException {
		//ArgsActuales -> ( ListaExpresiones )
		match(Utl.TT_PUNPARENT_A);
		listaExpresiones();
		match(Utl.TT_PUNPARENT_C);
	}
	private void listaExpresiones() throws SintacticException {
		//ListaExpresiones -> Expresion ListaExp | epsilon
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
		//ListaExp -> , Expresion ListaExp | epsilon
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
		//LlamadaMetodoEncadenado -> ArgsActuales Encadenado
		argsActuales();
		encadenado();
	}
	private void accesoVarEncadenado() throws SintacticException{
		//AccesoVarEncadenado -> Encadenado
		encadenado();
	}
	private void accesoArregloEncadenado() throws SintacticException {
		//AccesoArregloEncadenado -> [ Expresion ] Encadenado
		match(Utl.TT_PUNCORCH_A);
		expresion();
		match(Utl.TT_PUNCORCH_C);
		encadenado();
	}
}