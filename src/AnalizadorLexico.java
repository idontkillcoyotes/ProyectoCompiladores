public class AnalizadorLexico {
	
	private EntradaSalida entradaSalida;	
	private char caracterActual;
	private String lexemaActual;
	
	public AnalizadorLexico(EntradaSalida io){
		this.entradaSalida=io;
		if (!io.finArchivo()){
			nextChar();
		}
		this.lexemaActual="";
	}	
	private void consumir(){
		this.lexemaActual+=this.caracterActual;
	}
	private void nextChar(){
		this.caracterActual=this.entradaSalida.nextChar();
	}	
	public boolean finArchivo(){
		return this.entradaSalida.finArchivo();
	}	
	private Token createToken(int tipo){
		int linea=this.entradaSalida.getNroLinea();
		int col=this.entradaSalida.getNroColumna();						
		Token tkn = new Token(tipo,lexemaActual,linea);
		tkn.setNroColumna(col);
		this.lexemaActual="";
		return tkn;	
	}	
	
	public Token nextToken() throws LexicoException{
		int estado=0;	
		//Uso el sig contador para calcular correctamente el numero de columna de una string o caracter
		//que tiene barras \ dentro (ya que las omito del lexema)
		int cantBarras=0; 
		Token tkn= new Token(Utilidades.TT_FINARCHIVO,"¶",-1,-1);
		while (!entradaSalida.finArchivo()){
			switch (estado){
			case 0:{
				//Estado inicial y reconocedor de tokens de un solo caracter
				if ((caracterActual==0)||(caracterActual==-1)){
					//fin de archivo
					this.lexemaActual="";
					return new Token(Utilidades.TT_FINARCHIVO,"¶",-1,-1);
				}
				else if (Character.isWhitespace(caracterActual)){
					//espacio en blanco, ignoro
					nextChar();
					break;
				}
				else if (Character.isLowerCase(caracterActual)){
					//posible token idMetvar o palabras reservadas
					consumir();
					estado=1;
					break;
				}
				else if (Character.isUpperCase(caracterActual)){
					//posible token idClase
					consumir();
					estado=2;
					break;
				}
				else if (Character.isDigit(caracterActual)){
					//posible token Entero
					consumir();
					estado=3;
					break;
				}
				else if (caracterActual==';'){
					//token es punto coma
					consumir();
					nextChar();
					return createToken(Utilidades.TT_PUNPUNTOCOMA);
				}
				else if (caracterActual=='{'){
					//token es llave abierta
					consumir();
					nextChar();
					return createToken(Utilidades.TT_PUNLLAVE_A);
				}
				else if (caracterActual=='}'){
					//token es llave cerrada
					consumir();
					nextChar();
					return createToken(Utilidades.TT_PUNLLAVE_C);
				}
				else if (caracterActual=='('){
					//token es parentesis abierto
					consumir();
					nextChar();
					return createToken(Utilidades.TT_PUNPARENT_A);
				}
				else if (caracterActual==')'){
					//token es parentesis cerrado
					consumir();
					nextChar();
					return createToken(Utilidades.TT_PUNPARENT_C);
				}
				else if (caracterActual=='+'){
					//token es suma
					consumir();
					nextChar();
					return createToken(Utilidades.TT_OPSUMA);											
				}
				else if (caracterActual=='-'){
					//token es resta
					consumir();	
					nextChar();
					return createToken(Utilidades.TT_OPRESTA);
				}
				else if (caracterActual=='*'){
					//token es multiplicacion
					consumir();
					nextChar();
					return createToken(Utilidades.TT_OPMULT);
				}
				else if (caracterActual=='['){
					//token es corchete abierto
					consumir();
					nextChar();
					return createToken(Utilidades.TT_PUNCORCH_A);
				}
				else if (caracterActual==']'){
					//token es corchete cerrado
					consumir();
					nextChar();
					return createToken(Utilidades.TT_PUNCORCH_C);
				}
				else if (caracterActual=='.'){
					//token es punto
					consumir();
					nextChar();
					return createToken(Utilidades.TT_PUNPUNTO);
				}
				else if (caracterActual==','){
					//token es coma
					consumir();
					nextChar();
					return createToken(Utilidades.TT_PUNCOMA);
				}				
				else if (caracterActual=='"'){
					//posible token String
					//consumir();
					estado=4;
					break;
				}
				else if (caracterActual=='\''){
					//posible token char
					//consumir();
					estado=5;
					break;
				}
				else if (caracterActual=='/'){
					//posibles token division o comentarios
					consumir();
					estado=6;
					break;
				}
				else if (caracterActual=='='){
					//posible token igual o doble igual
					consumir();
					estado=7;
					break;
				}				
				else if (caracterActual=='<'){
					//posibles token menor migual
					consumir();
					estado=8;
					break;
				}
				else if (caracterActual=='>'){
					//posibles token mayor migual
					consumir();
					estado=9;
					break;
				}
				else if (caracterActual=='&'){
					//posible token AND
					consumir();
					estado=10;
					break;
				}
				else if (caracterActual=='|'){
					//posible token OR
					consumir();
					estado=11;
					break;
				}
				else if (caracterActual=='!'){
					//token es negacion o desigual
					consumir();
					estado=12;
					break;
				}
				else{
					// caracter desconocido
					int linea=this.entradaSalida.getNroLinea();
					int col=this.entradaSalida.getNroColumna();
					throw new LexicoException("ERROR: Linea: "+linea+". Columna: "+col+". Se encontro un caracter inesperado: '"+caracterActual+"'");
				}
				
			}
			case 1:{
				//Estado que reconoce los token tipo idMETVAR o palabra clave
				nextChar();
				if ((caracterActual=='_')||(Character.isLetterOrDigit(caracterActual))){
					consumir();					
				}
				else {
					//token es idMetVar
					int id=Utilidades.getIDMetVarOPalabraClave(lexemaActual);
					return createToken(id);
				}
				break;
			}
			case 2:{
				//Estado que reconoce tokes de tipo idClase o palabra clave String
				nextChar();
				if ((caracterActual=='_')||(Character.isLetterOrDigit(caracterActual))){
					consumir();					
				}
				else {
					estado=0;
					if (lexemaActual.equals("String")){
						//token es String
						return createToken(Utilidades.TPC_STRING);
					}
					else{
						//token es idClase
						return createToken(Utilidades.TT_IDCLASE);
					}
				}
				break;				
			}
			case 3:{
				//Estado que reconoce tokens de tipo enteros
				nextChar();
				if (Character.isDigit(caracterActual)){
					consumir();			
				}
				else{
					//token es Entero
					return createToken(Utilidades.TT_LITENTERO);
				}
				break;
			}
			case 4:{
				//Estado que reconoce tokens de tipo String
				nextChar();
				if (caracterActual=='\\'){
					//caracter es la barra de salida
					cantBarras++;
					estado=41;
					break;
				}
				else if((caracterActual!='"')&&(caracterActual!=0)){
					consumir();					
				}
				else if (caracterActual=='"'){
					//fin de string
					nextChar();
					tkn=createToken(Utilidades.TT_LITSTRING);
					tkn.ajustarNroColumna(cantBarras+2);
					cantBarras=0;
					return tkn;
				}
				else if (caracterActual==0){
					//error, se encontro fin de archivo y se esperaba "
					int linea = this.entradaSalida.getNroLinea();
					int col = this.entradaSalida.getNroColumna();
					throw new LexicoException("ERROR: Linea: "+linea+". Columna: "+col+". Error de literal String. Se esperaba \" pero se encontro: '"+caracterActual+"'");
				}
				break;
			}
			case 41:{
				//Estado que reconoce el caracter luego de la barra \ en strings
				nextChar();
				if (caracterActual=='n'){
					//nueva linea
					//no funciona
					lexemaActual+="\n";
				}
				else if (caracterActual=='t'){
					//tab
					//no funciona
					lexemaActual+="\t";				
				}
				else{
					consumir();
				}
				estado=4;
				break;
			}
			case 5:{
				//Estado que reconoce tokens de tipo char
				nextChar();
				if (caracterActual=='\\'){
					estado=51;
					cantBarras++;
					break;
				}
				else if (caracterActual=='\''){
					//error el token char no puede ser vacio
					int linea = this.entradaSalida.getNroLinea();
					int col = this.entradaSalida.getNroColumna();
					throw new LexicoException("ERROR: Linea: "+linea+". Columna: "+col+". Error de literal caracter. Literal caracter vacio");
				}
				else if (caracterActual==0){
					//fin archivo luego de primer '
					int linea = this.entradaSalida.getNroLinea();
					int col = this.entradaSalida.getNroColumna();
					throw new LexicoException("ERROR: Linea: "+linea+". Columna: "+col+". Error de literal caracter. Se esperaba un caracter valido pero se encontro: '"+caracterActual+"'");					
				}
				else if (caracterActual!='\''){
					//token es char no vacio
					consumir();
					estado=52;
					break;										
				}
			}		
			case 51:{
				//Estado que reconoce el caracter luego de la barra \ en chars
				nextChar();
				if (caracterActual=='n'){
					//nueva linea
					lexemaActual+='\n';
				}
				else if (caracterActual=='t'){
					//tab
					lexemaActual+='\t';				
				}
				else if (caracterActual==0){
					//fin de archivo luego de la \
					//error, el caracter tiene no esta entre comillas
					int linea = this.entradaSalida.getNroLinea();
					int col = this.entradaSalida.getNroColumna();
					throw new LexicoException("ERROR: Linea: "+linea+". Columna: "+col+". Error de literal caracter. Se esperaba ' pero se encontro: '"+caracterActual+"'");
				}
				else{
					consumir();
				}
				estado=52;
				break;
			}
			case 52:{
				nextChar();
				//Estado final que reconoce tokens de tipo caracter
				if (caracterActual=='\''){
					//comilla terminadora
					nextChar();
					tkn= createToken(Utilidades.TT_LITCARACTER);
					tkn.ajustarNroColumna(cantBarras+2);
					cantBarras=0;
					return tkn;
				}
				else{
					//error, el caracter tiene no esta entre comillas
					int linea = this.entradaSalida.getNroLinea();
					int col = this.entradaSalida.getNroColumna();
					throw new LexicoException("ERROR: Linea: "+linea+". Columna: "+col+". Error de literal caracter. Se esperaba ' pero se encontro: '"+caracterActual+"'");
				}
			}
			case 6:{
				//Estado que reconoce un token division o posibles comentarios
				nextChar();
				if (caracterActual=='/'){
					//comentario simple
					estado=61;
					break;
				}
				else if (caracterActual=='*'){
					//comentario multilinea
					estado=62;
					break;
				}
				else{
					//token division
					estado=0;
					return createToken(Utilidades.TT_OPDIV);
				}
			}
			case 61:{
				//Estado que ignora comentarios simples
				nextChar();
				if (caracterActual=='\n'){
					//Si es <enter> elimino (reconozco comentario simple)						
					lexemaActual="";
					estado=0;
				}
				break;
			}
			case 62:{
				//Estado que ignora posibles comentarios multilinea
				nextChar();
				if (caracterActual=='*'){
					//Voy a estado "reconocedor" de comentarios multilinea
					estado=621;
				}
				else if(caracterActual==0){
					//fin archivo antes del cierre del comentario
					int linea = this.entradaSalida.getNroLinea();
					int col = this.entradaSalida.getNroColumna();
					throw new LexicoException("ERROR: Linea: "+linea+". Columna: "+col+". Error en cierre de comentario multilinea.");
				}
				break;				
			}
			case 621:{
				//Estado final que ignora comentarios multilinea
				nextChar();
				if (caracterActual=='/'){
					//Si es '/' el comentario fue cerrado correctamente
					lexemaActual="";
					nextChar();
					estado=0;
				}
				else if (caracterActual==0){
					//fin de archivo antes del cierre del comentario
					int linea = this.entradaSalida.getNroLinea();
					int col = this.entradaSalida.getNroColumna();
					throw new LexicoException("ERROR: Linea: "+linea+". Columna: "+col+". Error en cierre de comentario multilinea. Se esperaba '/' pero se encontro: '"+caracterActual+"'");
				}
				else{
					estado=62;
					break;
				}
				break;
			}
			case 7:{
				//Estado que reconoce tokes de tipo igual o doble igual
				nextChar();
				if(caracterActual=='='){
					//token es doble igual
					consumir();
					nextChar();
					return createToken(Utilidades.TT_OPDOBLEIGUAL);
				}else{
					//token es solo igual
					return createToken(Utilidades.TT_ASIGIGUAL);
				}
			}
			case 8:{
				//Estado que reconoce tokens de tipo menor o menor igual
				nextChar();
				if(caracterActual=='='){
					//token es menor igual
					consumir();
					nextChar();
					return createToken(Utilidades.TT_OPMENORIG);
				}
				else{
					//token es menor
					return createToken(Utilidades.TT_OPMENOR);
				}
			}
			case 9:{
				//Estado que reconoce tokens de tipo mayor o mayor igual
				nextChar();
				if(caracterActual=='='){
					//token es mayor igual
					consumir();
					nextChar();
					return createToken(Utilidades.TT_OPMAYORIG);
				}else{
					//token es mayor
					return createToken(Utilidades.TT_OPMAYOR);
				}
			}
			case 10:{
				//Estado que reconoce el token doble AND
				nextChar();
				if(caracterActual=='&'){
					consumir();
					nextChar();
					return createToken(Utilidades.TT_OPANDDOBLE);
				}else{
					//error caracter inesperado
					int linea=this.entradaSalida.getNroLinea();
					int col=this.entradaSalida.getNroColumna();
					throw new LexicoException("ERROR: Linea: "+linea+". Columna: "+col+". Se esperaba '&' pero se encontro: '"+caracterActual+"'");
				}				
			}
			case 11:{
				//Estado que reconoce el token doble OR
				nextChar();
				if(caracterActual=='|'){
					consumir();
					nextChar();
					return createToken(Utilidades.TT_OPORDOBLE);
				}else{
					//error caracter inesperado
					int linea=this.entradaSalida.getNroLinea();
					int col=this.entradaSalida.getNroColumna();
					throw new LexicoException("ERROR: Linea: "+linea+". Columna: "+col+". Se esperaba '|' pero se encontro: '"+caracterActual+"'");					
				}
			}
			case 12:{
				//Estado que reconoce tokens de tipo negacion o desigual
				nextChar();
				if(caracterActual=='='){
					//token es desigual
					consumir();
					nextChar();
					return createToken(Utilidades.TT_OPDESIGUAL);
				}else{
					//token es negacion					
					return createToken(Utilidades.TT_OPNEGBOOL);
				}				
			}
			}
		}
		return tkn;
	}//finnextToken
	
	
}//finclase
