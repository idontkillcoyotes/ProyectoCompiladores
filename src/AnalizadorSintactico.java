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
	
	private void matchSimple() {
		try {
			tokenAct = alex.nextToken();
		}
		catch (LexicoException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void start() throws SintacticException, SemanticException{
		mts.crearClasesPredefinidas();
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
			matchSimple();
			
			//Guardo el token de nombre de clase
			Token tn=tokenAct;
			match(Utl.TT_IDCLASE);										
			//Guardo el token de nombre de la clase padre
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
		Token ret=null;
		if (tokenAct.esTipo(Utl.TPC_EXTENDS)){
			matchSimple();
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
		
		//obtengo el valor de inicializacion
		NodoExpresion val=inicializacion();
		
		//Agrego los atributos a la clase
		mts.crearAtributos(lista, vis, val);
		
		if(val!=null){
			//Seteo que esta en una declaracion de atributos
			val.setValorAtributo(true);		
		}	
		
		match(Utl.TT_PUNPUNTOCOMA);
	}
	//Aca empieza el logro de inicializacion en declaracion
	//Comentar este metedo si es que hay problemas
	private NodoExpresion inicializacion() throws SintacticException {
		//Inicializacion -> = Expresion | epsilon
		if (tokenAct.esTipo(Utl.TT_ASIGIGUAL)){
			matchSimple();
			return expresion();
		}
		else if (tokenAct.esTipo(Utl.TT_PUNPUNTOCOMA)){
			//vacio
			return null;
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
		
		//Seteo bloque actual como null
		mts.setBloqueActual(null);
		
		NodoBloque b=bloque();
		
		//Agrego el bloque al miembro
		mts.agregarBloque(b);
	}
	private void ctor() throws SintacticException, SemanticException{
		//Ctor -> idClase ArgsFormales Bloque		
		if (tokenAct.esTipo(Utl.TT_IDCLASE)){
			//Guardo el token de nombre del constructor
			Token tn=tokenAct;
			matchSimple();
			
			//Creo constructor
			mts.crearConstructor(tn);
			
			argsFormales();
			
			//Agrego constructor a clase cuando ya tengo todos sus parametros
			mts.agregarConstructor();
			
			//Seteo bloque actual como null
			mts.setBloqueActual(null);
			
			NodoBloque b=bloque();
			
			//Agrego el bloque al miembro
			mts.agregarBloque(b);			
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
			matchSimple();
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
			matchSimple();
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
			matchSimple();
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
			matchSimple();
			return FormaMetodo.fStatic;
		}
		else if (tokenAct.esTipo(Utl.TPC_DYNAMIC)){
			matchSimple();
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
			matchSimple();
			return Visibilidad.vPublic;
		}
		else if (tokenAct.esTipo(Utl.TPC_PRIVATE)){
			matchSimple();
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
			matchSimple();
			return new TipoVoid(tn);
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
			matchSimple();
			return posibleArreglo(new TipoBool(tn));
		}
		else if (tokenAct.esTipo(Utl.TPC_CHAR)){
			Token tn=tokenAct;
			matchSimple();
			return posibleArreglo(new TipoChar(tn));
		}
		else if (tokenAct.esTipo(Utl.TPC_INT)){
			Token tn=tokenAct;
			matchSimple();
			return posibleArreglo(new TipoInt(tn));
		}
		else if (tokenAct.esTipo(Utl.TT_IDCLASE)){
			Token tk=tokenAct;
			matchSimple();
			//para logro posibleArreglo();
			//Comentar si es que hay problemas
			return posibleArreglo(new TipoClase(tk));
		}
		else if (tokenAct.esTipo(Utl.TPC_STRING)){
			Token tn=tokenAct;
			matchSimple();
			//para logro posibleArreglo();
			//Comentar si es que hay problemas
			return posibleArreglo(new TipoString(tn));
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
			matchSimple();
			
			if (tokenAct.esTipo(Utl.TT_PUNCORCH_C)){
				matchSimple();
				//seteo que es arreglo
				tipo.setArreglo(true);
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

	private void listaDecVars(Tipo tipo,ArrayList<EParametro> l)  throws SintacticException, SemanticException{
		//ListaDecVars -> idMetVar ListaDV
		//If para reportar mejor el error
		if (tokenAct.esTipo(Utl.TT_IDMETVAR)){
			//Obtengo el token nombre de la variable
			Token t=tokenAct;
			matchSimple();
			
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
			matchSimple();
			if (tokenAct.esTipo(Utl.TT_IDMETVAR)){
				//Obtengo el token nombre de la variable
				Token t=tokenAct;
				matchSimple();
				
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
		//siguientes: ; = } idClase public private static dynamic if while return boolean char
		// 			  int String { ( idMetVar this else
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
	private NodoBloque bloque() throws SintacticException, SemanticException {
		//Bloque -> { Sentencias }
		match(Utl.TT_PUNLLAVE_A);
		
		//Creo nodo bloque con padre bloque actual
		NodoBloque b=new NodoBloque(mts.bloqueAct());		
		
		/*
		//Si el padre no es nulo deberia agregarle el nuevo bloque a sus sentencias
		if(mts.bloqueAct()!=null){
			mts.bloqueAct().addSentencia(b);		
		}		
		*/
		
		//El nuevo bloque es el bloque actual
		mts.setBloqueActual(b);
		
		//Voy agregando sentencias
		sentencias();
		
		match(Utl.TT_PUNLLAVE_C);
		
		//Aca termino el bloque, seteo el padre como bloque actual		
		mts.setBloqueActual(mts.bloqueAct().getPadre());
		
		//Retorno el bloque nuevo
		return b;
	}
	private void sentencias() throws SintacticException, SemanticException {
		//Sentencias -> Sentencia Sentencias | epsilon
		if(tokenAct.esTipo(new int[]{Utl.TT_PUNPUNTOCOMA,Utl.TPC_IF,Utl.TPC_WHILE,Utl.TPC_RETURN,Utl.TT_IDMETVAR,Utl.TPC_THIS,Utl.TPC_BOOLEAN,Utl.TPC_CHAR,Utl.TPC_INT,Utl.TT_IDCLASE,Utl.TPC_STRING,Utl.TT_PUNPARENT_A,Utl.TT_PUNLLAVE_A})){
			NodoSentencia s=sentencia();
			//agrego sentencia al bloque actual
			mts.agregarSentencia(s);
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
	private NodoSentencia sentencia() throws SintacticException, SemanticException {
		//Sentencia -> ; | if ( Expresion ) Sentencia SentenciaElse | while ( Expresion ) Sentencia | return Expresiones ;
		//Sentencia -> Asignacion ; | SentenciaLlamada ; | Tipo ListaDecVars ; | Bloque
		if (tokenAct.esTipo(Utl.TT_PUNPUNTOCOMA)){	
			// ;
			//Creo un nuevo nodo punto y coma
			NodoPuntoComa n=new NodoPuntoComa(tokenAct);
			matchSimple();
			return n;
		}
		else if (tokenAct.esTipo(Utl.TPC_IF)){
			//if ( expresion ) sentencia sentenciaElse 
			
			//Creo un nuevo nodo if
			NodoIf n=new NodoIf(tokenAct);
			matchSimple();
			match(Utl.TT_PUNPARENT_A);
			//Asigno la condicion
			n.setCondicion(expresion());
			match(Utl.TT_PUNPARENT_C);
			
			//Asigno sentencia y else
			n.setSentenciathen(sentencia());	
			n.setSentenciaelse(sentenciaElse());			
			
			return n;
		}
		else if (tokenAct.esTipo(Utl.TPC_WHILE)){
			//while ( expresion ) sentencia 
			//Creo nodo while
			NodoWhile n=new NodoWhile(tokenAct);
			matchSimple();			
			match(Utl.TT_PUNPARENT_A);
			//Asigno condicion
			n.setCondicion(expresion());
			match(Utl.TT_PUNPARENT_C);
			//Asigno sentencia
			n.setSentencia(sentencia());
			return n;
		}
		else if (tokenAct.esTipo(Utl.TPC_RETURN)){
			//return expresiones 
			NodoReturn n=new NodoReturn(tokenAct);
			matchSimple();
			//Asigno expresiones 
			n.setExpresion(expresiones());
			match(Utl.TT_PUNPUNTOCOMA);
			return n;
		}
		else if (tokenAct.esTipo(new int[]{Utl.TT_IDMETVAR,Utl.TPC_THIS})){
			//asignacion			
			NodoAsignacion n=asignacion();
			match(Utl.TT_PUNPUNTOCOMA);
			return n;
		}
		else if (tokenAct.esTipo(Utl.TT_PUNPARENT_A)){
			//sentenciallamada			 
			NodoSentenciaSimple n=new NodoSentenciaSimple();
			n.setExpresion(sentenciaLlamada());
			match(Utl.TT_PUNPUNTOCOMA);
			return n;
		}
		else if (tokenAct.esTipo(new int[]{Utl.TPC_BOOLEAN,Utl.TPC_CHAR,Utl.TPC_INT,Utl.TT_IDCLASE,Utl.TPC_STRING})){
			//tipo listadecvars inicializacion 
			Tipo t = tipo();			
			ArrayList<EParametro> vars=new ArrayList<EParametro>();
			listaDecVars(t,vars);
			//Creo nodo declaracion variables con la lista
			NodoDeclaracionVars n=new NodoDeclaracionVars(t,vars);
			//Asigno la expresion valor
			n.setValor(inicializacion());
			match(Utl.TT_PUNPUNTOCOMA);
			return n;
		}
		else if (tokenAct.esTipo(Utl.TT_PUNLLAVE_A)){
			//bloque
			return bloque();
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Sentencia mal formada.\n"
					+ "Se esperaba un token dentro del grupo: { ( ; if while return this boolean char int idClase idMetVar String\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private NodoSentencia sentenciaElse()  throws SintacticException, SemanticException{
		//SentenciaElse -> else Sentencia | epsilon
		if (tokenAct.esTipo(Utl.TPC_ELSE)){
			matchSimple();
			return sentencia();
		}
		else if(tokenAct.esTipo(new int[]{Utl.TPC_ELSE,Utl.TT_PUNLLAVE_C,Utl.TT_PUNPUNTOCOMA,Utl.TPC_IF,
				Utl.TPC_WHILE,Utl.TPC_RETURN,Utl.TPC_BOOLEAN,Utl.TPC_CHAR,Utl.TPC_INT,Utl.TT_IDCLASE,
				Utl.TPC_STRING,Utl.TT_PUNLLAVE_A,Utl.TT_PUNPARENT_A,Utl.TT_IDMETVAR,Utl.TPC_THIS})){
			//siguientes: else } ; if while return boolean char int idClase String { ( idMetVar this
			//vacio
			//TODO ver si conviene que retorno un nodo puntocoma en vez de null
			return null;
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Sentencia mal formada.\n"
					+ "Se esperaba un token dentro del grupo: else ; if while return this boolean char int String idMetVar idClase ( {\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private NodoExpresion expresiones()  throws SintacticException{
		//Expresiones -> Expresion | epsilon
		if (tokenAct.esTipo(new int[]{Utl.TT_OPSUMA,Utl.TT_OPRESTA,Utl.TT_OPNEGBOOL,Utl.TPC_NULL,Utl.TPC_TRUE,Utl.TPC_FALSE,Utl.TT_LITENTERO,Utl.TT_LITCARACTER,Utl.TT_LITSTRING,Utl.TT_IDMETVAR,Utl.TT_PUNPARENT_A,Utl.TT_IDCLASE,Utl.TPC_THIS,Utl.TPC_NEW})){
			return expresion();
		}
		else if(tokenAct.esTipo(Utl.TT_PUNPUNTOCOMA)){
			//vacio
			//TODO ver si conviene que retorne una expresion vacia en vez de null
			return null;
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: + - ! null true false litEntero litCaracter litString idMetVar idClase this new (\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	} 
	private NodoAsignacion asignacion()  throws SintacticException{
		//Asignacion -> AccesoVar = Expresion | AccesoThis = Expresion		
		//Creo el nodo asignacion
		NodoAsignacion n=new NodoAsignacion();
		
		if (tokenAct.esTipo(Utl.TT_IDMETVAR)){
			NodoVar v=accesoVar(true);
			n.setLadoizq(v);
			Token t=tokenAct;
			match(Utl.TT_ASIGIGUAL);
			n.setTokenIgual(t);
			n.setLadoder(expresion());
			return n;
		}
		else if(tokenAct.esTipo(Utl.TPC_THIS)){
			NodoThis t=accesoThis(true);
			n.setLadoizq(t);
			Token ig=tokenAct;
			match(Utl.TT_ASIGIGUAL);
			n.setTokenIgual(ig);
			n.setLadoder(expresion());
			return n;
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: this idMetVar\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	} 
	private NodoPrimario sentenciaLlamada() throws SintacticException {
		//SentenciaLlamada -> ( Primario )
		
		match(Utl.TT_PUNPARENT_A);
		NodoPrimario n=primario();
		match(Utl.TT_PUNPARENT_C);
		return n;
	}
	private NodoExpresion expresion()  throws SintacticException{
		//Expresion -> ExpOr
		return expOr();
	}	
	private NodoExpresion expOr()  throws SintacticException{
		//ExpOr -> ExpAnd ExpOrR
		NodoExpresion n=expAnd();
		return expOrR(n);
	}
	private NodoExpresion expOrR(NodoExpresion izq)  throws SintacticException{
		//ExpOrR -> || ExpAnd ExpOrR | epsilon
		if (tokenAct.esTipo(Utl.TT_OPORDOBLE)){
			//creo operador bool
			OpBool op=new OpBool(tokenAct);
			matchSimple();
			//creo nodo expresion binaria con operador y lado izq
			NodoExpBinaria n=new NodoExpBinaria(op,izq);
			//asigno lado derecho con el resultado de llamada
			n.setLadoder(expAnd());
			//paso como parametro el nodo creado y retorno
			return expOrR(n);			
		}
		else if(tokenAct.esTipo(new int[]{Utl.TT_PUNPUNTOCOMA,Utl.TT_PUNPARENT_C,Utl.TT_PUNCORCH_C,Utl.TT_PUNCOMA})){
			//siguientes ; , ) ]
			//vacio
			return izq;
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Expresion mal formada.\n"
					+ "Se esperaba un token dentro del grupo: || ; , ) ]\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private NodoExpresion expAnd() throws SintacticException{
		//ExpAnd -> ExpIg ExpAndR
		NodoExpresion n=expIg();
		return expAndR(n);
	}
	private NodoExpresion expAndR(NodoExpresion izq)  throws SintacticException{
		//ExpAndR -> && ExpIg ExpAndR | epsilon
		if (tokenAct.esTipo(Utl.TT_OPANDDOBLE)){
			//creo operador bool
			OpBool op=new OpBool(tokenAct);
			matchSimple();
			NodoExpBinaria n=new NodoExpBinaria(op,izq);
			n.setLadoder(expIg());
			return expAndR(n);
		}
		else if(tokenAct.esTipo(new int[]{Utl.TT_PUNPUNTOCOMA,Utl.TT_PUNCOMA,Utl.TT_OPORDOBLE,
				Utl.TT_PUNPARENT_C,Utl.TT_PUNCORCH_C})){
			//siguientes: ; , || ) ] 
			//vacio
			return izq;
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Expresion mal formada.\n"
					+ "Se esperaba un token dentro del grupo: && || ; , ) ]\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private NodoExpresion expIg()  throws SintacticException{
		//ExpIg -> ExpComp ExpIgR
		NodoExpresion n=expComp();
		return expIgR(n);
	} 
	private NodoExpresion expIgR(NodoExpresion izq) throws SintacticException {
		//ExpIgR -> OpIgual ExpComp ExpIgR | epsilon
		if (tokenAct.esTipo(new int[]{Utl.TT_OPDOBLEIGUAL,Utl.TT_OPDESIGUAL})){			
			OpComp op=opIgual();
			NodoExpBinaria n=new NodoExpBinaria(op,izq);
			n.setLadoder(expComp());
			return expIgR(n);
		}
		else if(tokenAct.esTipo(new int[]{Utl.TT_PUNPUNTOCOMA,Utl.TT_PUNCOMA,Utl.TT_OPANDDOBLE,Utl.TT_OPORDOBLE,
				Utl.TT_PUNPARENT_C,Utl.TT_PUNCORCH_C})){
			//siguientes: ; , && || ) ]
			//vacio
			return izq;
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Expresion mal formada.\n"
					+ "Se esperaba un token dentro del grupo: == != ; , && || ) ]\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private NodoExpresion expComp()  throws SintacticException{
		//ExpComp -> ExpAd ExpCompR
		NodoExpresion n=expAd();
		return expCompR(n);
	}
	private NodoExpresion expCompR(NodoExpresion izq) throws SintacticException {
		//ExpCompR -> OpComp ExpAd
		if (tokenAct.esTipo(new int[]{Utl.TT_OPMENOR,Utl.TT_OPMENORIG,Utl.TT_OPMAYOR,Utl.TT_OPMAYORIG})){
			Operador op=opComp();
			NodoExpBinaria n=new NodoExpBinaria(op,izq);
			n.setLadoder(expAd());
			//TODO ver si esto es correcto
			return n;
		}
		else if(tokenAct.esTipo(new int[]{Utl.TT_PUNPUNTOCOMA,Utl.TT_PUNCOMA,Utl.TT_OPDOBLEIGUAL,Utl.TT_OPDESIGUAL,
				Utl.TT_OPANDDOBLE,Utl.TT_OPORDOBLE,Utl.TT_PUNPARENT_C,Utl.TT_PUNCORCH_C})){
			//siguientes: ; , == != && || ) ] 
			//vacio
			return izq;
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Expresion mal formada.\n"
					+ "Se esperaba un token dentro del grupo: < <= > >= ; , == != && || ) ]\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private NodoExpresion expAd()  throws SintacticException{
		//ExpAd -> ExpMul ExpAdR
		NodoExpresion n=expMul();
		return expAdR(n);
	} 
	private NodoExpresion expAdR(NodoExpresion izq)  throws SintacticException{
		//ExpAdR -> OpAd ExpMul ExpAdR | epsilon
		if (tokenAct.esTipo(new int[]{Utl.TT_OPSUMA,Utl.TT_OPRESTA})){
			Operador op=opAd();
			NodoExpBinaria n=new NodoExpBinaria(op,izq);
			n.setLadoder(expMul());
			return expAdR(n);
		}
		else if(tokenAct.esTipo(new int[]{Utl.TT_PUNPUNTOCOMA,Utl.TT_PUNCOMA,Utl.TT_OPMENOR,Utl.TT_OPMENORIG,
				Utl.TT_OPMAYOR,Utl.TT_OPMAYORIG,Utl.TT_OPDOBLEIGUAL,Utl.TT_OPDESIGUAL,Utl.TT_OPANDDOBLE,
				Utl.TT_OPORDOBLE,Utl.TT_PUNPARENT_C,Utl.TT_PUNCORCH_C})){
			//siguientes: ; , < > <= >= == != && || ) ] 
			//vacio
			return izq;
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Expresion mal formada.\n"
					+ "Se esperaba un token dentro del grupo: + - ; , < <= > >= == != && || ) ]\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private NodoExpresion expMul()  throws SintacticException{
		//ExpMul -> ExpUn ExpMulR
		NodoExpresion n=expUn();
		return expMulR(n);
	}
	private NodoExpresion expMulR(NodoExpresion izq) throws SintacticException {
		//ExpMulR -> OpMul ExpUn ExpMulR | epsilon
		if (tokenAct.esTipo(new int[]{Utl.TT_OPMULT,Utl.TT_OPDIV})){
			Operador op=opMul();
			NodoExpBinaria n=new NodoExpBinaria(op,izq);
			n.setLadoder(expUn());
			return expMulR(n);
		}
		else if(tokenAct.esTipo(new int[]
				{Utl.TT_PUNPUNTOCOMA,Utl.TT_PUNCOMA,Utl.TT_OPSUMA,Utl.TT_OPRESTA,Utl.TT_OPMENOR,Utl.TT_OPMENORIG,
				Utl.TT_OPMAYOR,Utl.TT_OPMAYORIG,Utl.TT_OPDOBLEIGUAL,Utl.TT_OPDESIGUAL,Utl.TT_OPANDDOBLE,
				Utl.TT_OPORDOBLE,Utl.TT_PUNPARENT_C,Utl.TT_PUNCORCH_C})){
			//siguientes: ; , + - < <= > >= == != && || ) ] 
			//vacio
			return izq;
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Expresion mal formada.\n"
					+ "Se esperaba un token dentro del grupo: * /  ; , + - < <= > >= == != && || )\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private NodoExpresion expUn() throws SintacticException {
		//ExpUn -> OpUn ExpUn
		if (tokenAct.esTipo(new int[]{Utl.TT_OPSUMA,Utl.TT_OPRESTA,Utl.TT_OPNEGBOOL})){
			Operador op=opUn();
			NodoExpUnaria n=new NodoExpUnaria(op);
			n.setExpresion(expUn());
			return n;
		}
		else if(tokenAct.esTipo(new int[]{Utl.TPC_NULL,Utl.TPC_TRUE,Utl.TPC_FALSE,Utl.TT_LITENTERO,Utl.TT_LITCARACTER,Utl.TT_LITSTRING,Utl.TT_IDMETVAR,Utl.TT_PUNPARENT_A,Utl.TT_IDCLASE,Utl.TPC_THIS,Utl.TPC_NEW})){
			return operando();
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Expresion mal formada.\n"
					+ "Se esperaba un token dentro del grupo: + - ! ( null true false litEntero litCaracter litString idMetVar idClase this new\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	} 
	private OpComp opIgual() throws SintacticException {
		//OpIgual -> == | !=
		if (tokenAct.esTipo(Utl.TT_OPDOBLEIGUAL)){
			OpComp op=new OpComp(tokenAct);
			matchSimple();
			return op;
		}
		else if (tokenAct.esTipo(Utl.TT_OPDESIGUAL)){
			OpComp op=new OpComp(tokenAct);
			matchSimple();
			return op;
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: == !=\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}		
	}
	private OpCompMat opComp() throws SintacticException {
		//OpComp -> < | > | <= | >=
		OpCompMat op=new OpCompMat(null);
		if (tokenAct.esTipo(Utl.TT_OPMENOR)){
			op.setOperador(tokenAct);
			matchSimple();
		}
		else if (tokenAct.esTipo(Utl.TT_OPMENORIG)){
			op.setOperador(tokenAct);
			matchSimple();
		}
		else if (tokenAct.esTipo(Utl.TT_OPMAYOR)){
			op.setOperador(tokenAct);
			matchSimple();
		}
		else if (tokenAct.esTipo(Utl.TT_OPMAYORIG)){
			op.setOperador(tokenAct);
			matchSimple();
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: < <= > >=\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
		return op;
	}
	private OpMat opAd() throws SintacticException {
		//OpAd -> + | -
		OpMat op=new OpMat(null);
		if (tokenAct.esTipo(Utl.TT_OPSUMA)){
			op.setOperador(tokenAct);
			matchSimple();
		}
		else if (tokenAct.esTipo(Utl.TT_OPRESTA)){
			op.setOperador(tokenAct);
			matchSimple();
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: + -\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
		return op;
	} 
	private Operador opUn()  throws SintacticException{
		//OpUn -> + | - | !
		if (tokenAct.esTipo(Utl.TT_OPSUMA)){
			OpMat op=new OpMat(tokenAct);
			matchSimple();
			return op;
		}
		else if (tokenAct.esTipo(Utl.TT_OPRESTA)){
			OpMat op=new OpMat(tokenAct);
			matchSimple();
			return op;
		}
		else if (tokenAct.esTipo(Utl.TT_OPNEGBOOL)){
			OpBool op=new OpBool(tokenAct);
			matchSimple();
			return op;
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: + - !\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	} 
	private OpMat opMul()  throws SintacticException{
		//OpMul -> * | /
		OpMat op=new OpMat(null);
		if (tokenAct.esTipo(Utl.TT_OPMULT)){
			op.setOperador(tokenAct);
			matchSimple();
		}
		else if (tokenAct.esTipo(Utl.TT_OPDIV)){
			op.setOperador(tokenAct);
			matchSimple();
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: * / \n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
		return op;
	} 
	private NodoExpresion operando() throws SintacticException {
		//Operando -> Literal | Primario
		if (tokenAct.esTipo(new int[]{Utl.TPC_NULL,Utl.TPC_TRUE,Utl.TPC_FALSE,
				Utl.TT_LITENTERO,Utl.TT_LITCARACTER,Utl.TT_LITSTRING})){
			return literal();
		}
		else if (tokenAct.esTipo(new int[]{Utl.TT_IDMETVAR,Utl.TT_IDCLASE,Utl.TT_PUNPARENT_A,
				Utl.TPC_THIS,Utl.TPC_NEW})){
			return primario();
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: null true false litEntero litCaracter litString"
					+ "idMetVar idClase this new (\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private NodoLiteral literal()  throws SintacticException{
		//Literal -> null | true | false | intLiteral | charLiteral | stringLiteral
		if (tokenAct.esTipo(Utl.TPC_NULL)){
			Token t=tokenAct;
			matchSimple();
			return new NodoLitNull(t);
		}
		else if (tokenAct.esTipo(Utl.TPC_TRUE)){
			Token t=tokenAct;
			matchSimple();
			return new NodoLitBoolean(t);
		}
		else if (tokenAct.esTipo(Utl.TPC_FALSE)){
			Token t=tokenAct;
			matchSimple();
			return new NodoLitBoolean(t);
		}
		else if (tokenAct.esTipo(Utl.TT_LITENTERO)){
			Token t=tokenAct;
			matchSimple();
			return new NodoLitEntero(t);
		}
		else if (tokenAct.esTipo(Utl.TT_LITCARACTER)){
			Token t=tokenAct;
			matchSimple();
			return new NodoLitChar(t);
		}
		else if (tokenAct.esTipo(Utl.TT_LITSTRING)){
			Token t=tokenAct;
			matchSimple();
			return new NodoLitString(t);
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: null true false litEntero litCaracter litString\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	} 
	private NodoPrimario primario() throws SintacticException {
		//Primario -> idMetVar MetodoVariable | ExpresionParentizada | AccesoThis | LlamadaMetodoEstatico | LlamadaCtor
		if (tokenAct.esTipo(Utl.TT_IDMETVAR)){
			Token id=tokenAct;
			matchSimple();
			return metodoVariable(id);
		}
		else if (tokenAct.esTipo(Utl.TT_PUNPARENT_A)){
			return expresionParentizada();
		}
		else if (tokenAct.esTipo(Utl.TPC_THIS)){
			return accesoThis(true);
		}
		else if (tokenAct.esTipo(Utl.TT_IDCLASE)){
			return llamadaMetodoEstatico();
		}
		else if (tokenAct.esTipo(Utl.TPC_NEW)){
			return llamadaCtor();
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: idMetVar idClase this new (\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private NodoPrimario metodoVariable(Token id) throws SintacticException {
		//MetodoVariable -> ArgsActuales Encadenado | Encadenado
		if (tokenAct.esTipo(Utl.TT_PUNPARENT_A)){
			//creo nodo llamada directa con token id
			NodoLlamadaDirecta n=new NodoLlamadaDirecta(id);
			//paso lista de argumentos para que se agreguen
			argsActuales(n.getArgsactuales());
			//asigno encadenado
			n.setEncadenado(encadenado());
			return n;
		}
		else if (tokenAct.esTipo(new int[]{Utl.TT_PUNPUNTO,Utl.TT_PUNCORCH_A,
				Utl.TT_PUNPUNTOCOMA,Utl.TT_PUNCOMA,Utl.TT_ASIGIGUAL,Utl.TT_OPSUMA,Utl.TT_OPRESTA,Utl.TT_OPMULT,
				Utl.TT_OPDIV,Utl.TT_OPMENOR,Utl.TT_OPMENORIG,Utl.TT_OPMAYOR,Utl.TT_OPMAYORIG,Utl.TT_OPDOBLEIGUAL,
				Utl.TT_OPDESIGUAL,Utl.TT_OPANDDOBLE,Utl.TT_OPORDOBLE,Utl.TT_PUNPARENT_C,Utl.TT_PUNCORCH_C})){
			//primeros y siguientes de encadenado
			//primeros: . [ 
			//siguientes: ; , = + - * / < <= > >= == != && || ) ]

			//Creo nodo var
			NodoVar n=new NodoVar(id);
			//Asigno encadenado
			n.setEncadenado(encadenado());
			return n;
		}
		else{
			//este error seria siempre de expresiones mal formadas?
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),"Expresion mal formada.\n"
					+"Se esperaba un token dentro del grupo: ( ) [ ] ; , . = + - * / < <= > >= == != && ||\n"
					+"Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));
		}
	}
	private NodoExpParent expresionParentizada()  throws SintacticException{
		//ExpresionParentizada -> ( Expresion ) Encadenado
		match(Utl.TT_PUNPARENT_A);
		//creo nodo expresion parentizada
		NodoExpParent n=new NodoExpParent();
		//asigno expresion
		n.setExpresion(expresion());
		match(Utl.TT_PUNPARENT_C);
		//asigno encadenado
		n.setEncadenado(encadenado());
		return n;		
	}
	private Encadenado encadenado() throws SintacticException {
		//Encadenado -> . idMetVar Acceso | AccesoArregloEncadenado | epsilon
		if (tokenAct.esTipo(Utl.TT_PUNPUNTO)){
			matchSimple();
			//guardo el token id
			Token id=tokenAct;
			match(Utl.TT_IDMETVAR);
			return acceso(id);
		}
		else if (tokenAct.esTipo(Utl.TT_PUNCORCH_A)){
			return accesoArregloEncadenado();
		}
		else if(tokenAct.esTipo(new int[]
				{Utl.TT_PUNPUNTOCOMA,Utl.TT_PUNCOMA,Utl.TT_ASIGIGUAL,Utl.TT_OPSUMA,Utl.TT_OPRESTA,Utl.TT_OPMULT,
				Utl.TT_OPDIV,Utl.TT_OPMENOR,Utl.TT_OPMENORIG,Utl.TT_OPMAYOR,Utl.TT_OPMAYORIG,Utl.TT_OPDOBLEIGUAL,
				Utl.TT_OPDESIGUAL,Utl.TT_OPANDDOBLE,Utl.TT_OPORDOBLE,Utl.TT_PUNPARENT_C,Utl.TT_PUNCORCH_C})){
			//siguientes: ; , = + - * / < <= > >= == != && || ) ] 
			//vacio
			//TODO ver si conviene retornar null o un tipo especial de encadenado
			return null;
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: . [ ; , = + - * / < <="
					+ " > >= == != && || ) ]\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));			
		}
	}
	private Encadenado acceso(Token id) throws SintacticException {
		//Acceso -> LlamadaMetodoEncadenado | AccesoVarEncadenado
		if (tokenAct.esTipo(Utl.TT_PUNPARENT_A)){
			return llamadaMetodoEncadenado(id);
		}
		else if (tokenAct.esTipo(new int[]{Utl.TT_PUNPUNTO,Utl.TT_PUNCORCH_A,Utl.TT_PUNPUNTOCOMA,
				Utl.TT_PUNCOMA,Utl.TT_ASIGIGUAL,Utl.TT_OPSUMA,Utl.TT_OPRESTA,Utl.TT_OPMULT,
				Utl.TT_OPDIV,Utl.TT_OPMENOR,Utl.TT_OPMENORIG,Utl.TT_OPMAYOR,Utl.TT_OPMAYORIG,
				Utl.TT_OPDOBLEIGUAL,Utl.TT_OPDESIGUAL,Utl.TT_OPANDDOBLE,Utl.TT_OPORDOBLE,
				Utl.TT_PUNPARENT_C,Utl.TT_PUNCORCH_C})){
			//primeros y siguientes de encadenado:
			//primeros: . [
			//siguientes: ; , = + - * / < > <= >= == != && || ) ]
			return accesoVarEncadenado(id);
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: ( ) [ ] . ; , = + - * / < > <= >= == != && ||\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));	
		}
	}
	private NodoThis accesoThis(boolean ladoizq) throws SintacticException {
		//AccesoThis -> this Encadenado
		Token t=tokenAct;
		match(Utl.TPC_THIS);
		//creo el nodo this con la clase actual
		NodoThis n=new NodoThis(mts.claseAct(),t);
		n.setLadoizq(ladoizq);
		//asigno el encadenado
		n.setEncadenado(encadenado());
		return n;
	}
	private NodoVar accesoVar(boolean ladoizq)  throws SintacticException{
		//AccesoVar -> idMetVar Encadenado
		Token t=tokenAct;
		match(Utl.TT_IDMETVAR);
		//creo el nodo variable con el token id
		NodoVar n=new NodoVar(t);
		n.setLadoizq(ladoizq);
		//asigno el encadenado
		n.setEncadenado(encadenado());
		return n;
	}
	private NodoLlamadaEstatica llamadaMetodo(Token tc)  throws SintacticException{
		//LlamadaMetodo -> idMetVar ArgsActuales Encadenado
		Token tm=tokenAct;
		match(Utl.TT_IDMETVAR);
		//TODO ver como conseguir la clase estatica
		//creo nodo llamada estatica con tokens del metodo y clase
		NodoLlamadaEstatica n=new NodoLlamadaEstatica(tc, tm);
		//paso la lista de argumentos para que se vayan agregando
		argsActuales(n.getArgsactuales());
		//asigno encadenado
		n.setEncadenado(encadenado());
		return n;
	}
	private NodoLlamadaEstatica llamadaMetodoEstatico()  throws SintacticException{
		//LlamadaMetodoEstatico -> idClase . LlamadaMetodo
		//TODO podria agregar ifs para reportar mejor un error sintáctico
		//guardo el token de idclase
		Token tc=tokenAct;	
		match(Utl.TT_IDCLASE);
		match(Utl.TT_PUNPUNTO);		
		return llamadaMetodo(tc);
	}
	private NodoConst llamadaCtor()  throws SintacticException{
		//LlamadaCtor -> new LlamadaCtorR
		match(Utl.TPC_NEW);
		return llamadaCtorR();
	}
	private NodoConst llamadaCtorR()  throws SintacticException{
		//LlamadaCtorR -> idClase LlamadaCtorRIDClase | TipoArreglo [ Expresion ] Encadenado
		if (tokenAct.esTipo(Utl.TT_IDCLASE)){
			Token t=tokenAct;
			matchSimple();
			return llamadaCtorRIDClase(t);
		}
		else if (tokenAct.esTipo(new int[]{Utl.TPC_BOOLEAN,Utl.TPC_CHAR,Utl.TPC_INT,Utl.TPC_STRING})){
			//Creo nodo constructor arreglo
			NodoConstArray n=new NodoConstArray(tokenAct);
			tipoArreglo();
			match(Utl.TT_PUNCORCH_A);
			
			//Asigno la expresion del tamaño
			n.setTamaño(expresion());
			match(Utl.TT_PUNCORCH_C);
			
			//Asigno el encadenado
			n.setEncadenado(encadenado());
			return n;
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: idClase boolean char int\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));	
		}
	}
	//Metodo para logro de arreglo de todo, comentar si es que hay problemas
	private NodoConst llamadaCtorRIDClase(Token t) throws SintacticException  {
		//LlamadaCtorRIDClase -> ArgsActuales Encadenado | [ Expresion ] Encadenado
		if (tokenAct.esTipo(Utl.TT_PUNCORCH_A)){			
			matchSimple();
			
			//Creo nuevo nodo constructor de arreglo de clase
			NodoConstArray n=new NodoConstArray(t);			
						
			//Asigno la expresion del tamaño al nodo
			n.setTamaño(expresion());			
			match(Utl.TT_PUNCORCH_C);
			
			//Asigno el encadenado al nodo
			n.setEncadenado(encadenado());
			
			return n;			
		}
		else if (tokenAct.esTipo(Utl.TT_PUNPARENT_A)){
			
			//Creo nuevo nodo constructor comun de clase
			NodoConstComun n=new NodoConstComun(t);
			
			//Paso lista de argumentos para que sean agregados
			argsActuales(n.getArgsactuales());
			
			//Asigno el encadenado al nodo
			n.setEncadenado(encadenado());
			
			return n;
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
			matchSimple();
		}
		else if (tokenAct.esTipo(Utl.TPC_BOOLEAN)){
			matchSimple();
		}
		else if (tokenAct.esTipo(Utl.TPC_INT)){
			matchSimple();
		}
		else if (tokenAct.esTipo(Utl.TPC_STRING)){
			matchSimple();
		}
		else{
			throw new SintacticException(tokenAct.getNroLinea(),tokenAct.getNroColumna(),
					"Se esperaba un token dentro del grupo: boolean char int String\n"
					+ "Pero se encontro un token: "+Utl.getTipoID(tokenAct.getTipo()));	
		}		
	}
	private void argsActuales(ArrayList<NodoExpresion> l) throws SintacticException {
		//ArgsActuales -> ( ListaExpresiones )
		match(Utl.TT_PUNPARENT_A);
		listaExpresiones(l);
		match(Utl.TT_PUNPARENT_C);
	}
	private void listaExpresiones(ArrayList<NodoExpresion> l) throws SintacticException {
		//ListaExpresiones -> Expresion ListaExp | epsilon
		if (tokenAct.esTipo(new int[]{Utl.TT_OPSUMA,Utl.TT_OPRESTA,Utl.TT_OPNEGBOOL,Utl.TPC_NULL,
				Utl.TPC_TRUE,Utl.TPC_FALSE,Utl.TT_LITENTERO,Utl.TT_LITCARACTER,Utl.TT_LITSTRING,
				Utl.TT_IDMETVAR,Utl.TT_PUNPARENT_A,Utl.TT_IDCLASE,Utl.TPC_THIS,Utl.TPC_NEW})){
			NodoExpresion e=expresion();
			l.add(e);
			listaExp(l);
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
	private void listaExp(ArrayList<NodoExpresion> l) throws SintacticException {
		//ListaExp -> , Expresion ListaExp | epsilon
		if (tokenAct.esTipo(Utl.TT_PUNCOMA)){
			matchSimple();
			NodoExpresion e=expresion();
			l.add(e);
			listaExp(l);
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
	private NodoLlamadaEncad llamadaMetodoEncadenado(Token id) throws SintacticException {
		//LlamadaMetodoEncadenado -> ArgsActuales Encadenado
		NodoLlamadaEncad n=new NodoLlamadaEncad(id);
		argsActuales(n.getArgsactuales());
		n.setEncadenado(encadenado());
		return n;
	}
	private NodoVarEncad accesoVarEncadenado(Token id) throws SintacticException{
		//AccesoVarEncadenado -> Encadenado
		NodoVarEncad n=new NodoVarEncad(id);
		n.setEncadenado(encadenado());
		return n;
	}
	private NodoArregloEncad accesoArregloEncadenado() throws SintacticException {
		//AccesoArregloEncadenado -> [ Expresion ] Encadenado
		NodoArregloEncad n=new NodoArregloEncad();
		match(Utl.TT_PUNCORCH_A);		
		n.setExpresion(expresion());
		match(Utl.TT_PUNCORCH_C);
		n.setEncadenado(encadenado());
		return n;
	}
}