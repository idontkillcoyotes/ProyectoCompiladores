
public class AnalizadorLexico {
	
	private EntradaSalida entradaSalida;	
	private char caracterActual;
	private String lexemaActual;
	
	
	
	public AnalizadorLexico(EntradaSalida io){
		this.entradaSalida=io;
		this.caracterActual=io.nextChar();
		this.lexemaActual="";
	}
	
	private void consumir(){
		this.lexemaActual+=this.caracterActual;
		this.caracterActual=this.entradaSalida.nextChar();
	}
	private void noConsumir(){
		this.caracterActual=this.entradaSalida.nextChar();
	}
	
	public boolean finArchivo(){
		return this.entradaSalida.finArchivo();
	}
	
	public Token nextToken(){
		Token tkn;
		int linea;
		int col;
		int estado=0;
		while (!entradaSalida.finArchivo()){
			switch (estado){		
			case 0:{
					//Estado inicial y reconocedor de tokens de un solo simbolo
					if (Character.isWhitespace(caracterActual)){
						//espacio en blanco
						noConsumir();
						break;
					}
					if (caracterActual=='+'){
						//token es suma
						consumir();						
						linea=this.entradaSalida.getNroLinea();
						col=this.entradaSalida.getNroColumna();						
						tkn = new Token(Utilidades.TT_OPSUMA,lexemaActual,linea);
						tkn.setNroColumna(col);
						this.lexemaActual="";
						return tkn;
					}					
					if (caracterActual=='*'){
						//token es multiplicacion
						consumir();	
						this.lexemaActual="";
						linea=this.entradaSalida.getNroLinea();
						col=this.entradaSalida.getNroColumna();						
						tkn = new Token(Utilidades.TT_OPMULT,"*",linea);
						tkn.setNroColumna(col);
						return tkn;
					}
					if (caracterActual=='('){
						//token es parentesis abierto
						consumir();	
						this.lexemaActual="";
						linea=this.entradaSalida.getNroLinea();
						col=this.entradaSalida.getNroColumna();						
						tkn = new Token(Utilidades.TT_PUNPARENT_A,"(",linea);
						tkn.setNroColumna(col);
						return tkn;
					}
					if (caracterActual==')'){
						//token es parentesis cerrado
						consumir();	
						this.lexemaActual="";
						linea=this.entradaSalida.getNroLinea();
						col=this.entradaSalida.getNroColumna();						
						tkn = new Token(Utilidades.TT_PUNPARENT_C,")",linea);
						tkn.setNroColumna(col);
						return tkn;
					}
					if (caracterActual=='{'){
						//token es llave abierta
						consumir();	
						this.lexemaActual="";
						linea=this.entradaSalida.getNroLinea();
						col=this.entradaSalida.getNroColumna();						
						tkn = new Token(Utilidades.TT_PUNLLAVE_A,"{",linea);
						tkn.setNroColumna(col);
						return tkn;
					}
					if (caracterActual=='}'){
						//token es llave cerrada
						consumir();	
						this.lexemaActual="";
						linea=this.entradaSalida.getNroLinea();
						col=this.entradaSalida.getNroColumna();						
						tkn = new Token(Utilidades.TT_PUNLLAVE_C,"}",linea);
						tkn.setNroColumna(col);
						return tkn;
					}
					if (caracterActual=='['){
						//token es corchete abierto
						consumir();	
						this.lexemaActual="";
						linea=this.entradaSalida.getNroLinea();
						col=this.entradaSalida.getNroColumna();						
						tkn = new Token(Utilidades.TT_PUNCORCH_A,"[",linea);
						tkn.setNroColumna(col);
						return tkn;
					}
					if (caracterActual==']'){
						//token es corchete cerrado
						consumir();	
						this.lexemaActual="";
						linea=this.entradaSalida.getNroLinea();
						col=this.entradaSalida.getNroColumna();						
						tkn = new Token(Utilidades.TT_PUNCORCH_C,"]",linea);
						tkn.setNroColumna(col);
						return tkn;
					}
					if (caracterActual==';'){
						//token es punto coma
						consumir();	
						this.lexemaActual="";
						linea=this.entradaSalida.getNroLinea();
						col=this.entradaSalida.getNroColumna();						
						tkn = new Token(Utilidades.TT_PUNPUNTOCOMA,";",linea);
						tkn.setNroColumna(col);
						return tkn;
					}
					if (caracterActual=='.'){
						//token es punto
						consumir();	
						this.lexemaActual="";
						linea=this.entradaSalida.getNroLinea();
						col=this.entradaSalida.getNroColumna();						
						tkn = new Token(Utilidades.TT_PUNPUNTO,".",linea);
						tkn.setNroColumna(col);
						return tkn;
					}
					if (caracterActual==','){
						//token es coma
						consumir();	
						this.lexemaActual="";
						linea=this.entradaSalida.getNroLinea();
						col=this.entradaSalida.getNroColumna();						
						tkn = new Token(Utilidades.TT_PUNCOMA,",",linea);
						tkn.setNroColumna(col);
						return tkn;
					}
					if (caracterActual=='!'){
						//token es negacion o desigual
						consumir();
						estado=10;
						break;
						linea=this.entradaSalida.getNroLinea();
						col=this.entradaSalida.getNroColumna();						
						tkn = new Token(Utilidades.TT_OPNEGBOOL,lexemaActual,linea);
						tkn.setNroColumna(col);
						this.lexemaActual="";
						return tkn;
					}					
					if (Character.isUpperCase(caracterActual)){
						//posible token idClase
						consumir();
						estado=1;
						break;
					}
					if (Character.isLowerCase(caracterActual)){
						//posible token idMetvar o palabras reservadas
						consumir();
						estado=2;
						break;
					}
					if (Character.isDigit(caracterActual)){
						//posible token Entero
						consumir();
						estado=3;
						break;
					}
					if (caracterActual=='/'){
						//posibles token division o comentarios
						consumir();
						estado=4;
						break;
					}
					if (caracterActual=='<'){
						//posibles token menor migual
						consumir();
						estado=5;
						break;
					}
					if (caracterActual=='>'){
						//posibles token mayor migual
						consumir();
						estado=6;
						break;
					}
					if (caracterActual=='&'){
						//posible token AND
						consumir();
						estado=7;
						break;
					}
					if (caracterActual=='|'){
						//posible token OR
						consumir();
						estado=8;
						break;
					}
					if (caracterActual=='"'){
						//posible token String
						consumir();
						estado=9;
						break;
					}
					//El caracter actual es desconocido
					//Error lexico
					break;//break del case 0
			}
			case 1:{
					//Estado reconocedor de tokens idClase
					if ((caracterActual=='_')||(Character.isLetterOrDigit(caracterActual))){
						consumir();
					}
					else {
						//token es idClase
						linea=this.entradaSalida.getNroLinea();
						col=this.entradaSalida.getNroColumna();						
						tkn = new Token(Utilidades.TT_IDCLASE,lexemaActual,linea);						
						tkn.setNroColumna(col);
						lexemaActual="";
						return tkn;
					}
					//Error lexico si es un caracter extraño?
					break;//break del case 1			
			}
			case 2:{
					//Estado reconocedor de tokens idMetVar y palabras clave
					if ((caracterActual=='_')||(Character.isLetterOrDigit(caracterActual))){
						consumir();
					}
					else {
						//token es idMetVar
						linea=this.entradaSalida.getNroLinea();
						col=this.entradaSalida.getNroColumna();
						int id=Utilidades.getIDMetVarOPalabraClave(lexemaActual);
						tkn = new Token(id,lexemaActual,linea);
						tkn.setNroColumna(col);
						this.lexemaActual="";
						return tkn;
					}
					//Error lexico si es un caracter extraño?
					break;//break del case 2			
			}
			case 3:{
					//Estado que reconoce tokens de tipo enteros
					if (Character.isDigit(caracterActual)){
						consumir();
					}
					else{
						//token es Entero
						linea=this.entradaSalida.getNroLinea();
						col=this.entradaSalida.getNroColumna();
						tkn = new Token(Utilidades.TT_LITENTERO,lexemaActual,linea);
						tkn.setNroColumna(col);
						this.lexemaActual="";
						return tkn;
					}
					//Error lexico si es un caracter extraño?
					break;//break del case 3					
			}
			case 4:{
					//Estado que reconoce division o posibles comentarios				
					if (caracterActual=='/'){
						//posible "token" comentario simple
						noConsumir();
						estado=41;
					}else if (caracterActual=='*'){
						//posible "token" comentario multilinea
						noConsumir();
						estado=42;
					}else{
						//token es division
						linea=this.entradaSalida.getNroLinea();
						col=this.entradaSalida.getNroColumna();
						tkn = new Token(Utilidades.TT_OPDIV,lexemaActual,linea);
						tkn.setNroColumna(col);
						this.lexemaActual="";
						return tkn;						
					}
					//Error lexico si es un caracter extraño?
					break;//break del case 4	
			}
			case 41:{
					//Estado que reconoce(ignora) comentarios simples
					if (caracterActual!='\n'){
						//Si no es <enter> ignorar
						noConsumir();
					}else {
						//Es <enter> entonces "acepto" token
						//Como es un comentario y no retorno nada solo debo cambiar de estado
						estado=0;
					}
					//Error lexico si es un caracter extraño?
					break;//break del case 41	
			}
			case 42:{
					//Estado que reconoce(ignora) comentarios simples
					if (caracterActual!='\n'){
						//Si no es <enter> ignorar
						noConsumir();
					}else {
						//Es <enter> entonces "acepto" token
						//Como es un comentario y no retorno nada solo debo cambiar de estado
						estado=421;
					}
					//Error lexico si es un caracter extraño?
					break;//break del case 42	
			}
			case 421:{
				//Estado que reconoce(ignora) comentarios multilinea
				if (caracterActual!='*'){
					//Si no es * ignorar
					noConsumir();
				}else {
					//Es * entonces voy al estado "aceptador" de token
					//Como es un comentario y no retorno nada solo debo cambiar de estado
					estado=4211;
				}
				//Error lexico si es un caracter extraño?
				break;//break del case 421	
			}
			case 4211:{
				//Estado que reconoce(ignora) comentarios multilinea
				if (caracterActual=='/'){
					//El comentario multilinea esta cerrado correctamente
					noConsumir();
					estado=0;
				}else{
					//Deberia lanzar error?
					noConsumir();
				}				
				//Error lexico si es un caracter extraño?
				break;//break del case 4211
			}
			case 5:{
				//Estado que reconoce menor o menor igual
				if(caracterActual=='='){
					consumir();
					//token es menor igual
					linea=this.entradaSalida.getNroLinea();
					col=this.entradaSalida.getNroColumna();
					tkn = new Token(Utilidades.TT_OPMENORIG,lexemaActual,linea);
					tkn.setNroColumna(col);
					this.lexemaActual="";
					return tkn;
				}
				else{
					//token es menor
					linea=this.entradaSalida.getNroLinea();
					col=this.entradaSalida.getNroColumna();
					tkn = new Token(Utilidades.TT_OPMENOR,lexemaActual,linea);
					tkn.setNroColumna(col);
					this.lexemaActual="";
					return tkn;
				}
				//Error lexico si es un caracter extraño?
			}
			case 6:{
				//Estado que reconoce mayor o mayor igual
				if(caracterActual=='='){
					consumir();
					//token es mayor igual
					linea=this.entradaSalida.getNroLinea();
					col=this.entradaSalida.getNroColumna();
					tkn = new Token(Utilidades.TT_OPMAYORIG,lexemaActual,linea);
					tkn.setNroColumna(col);
					this.lexemaActual="";
					return tkn;
				}else{
					//token es menor
					linea=this.entradaSalida.getNroLinea();
					col=this.entradaSalida.getNroColumna();
					tkn = new Token(Utilidades.TT_OPMAYOR,lexemaActual,linea);
					tkn.setNroColumna(col);
					this.lexemaActual="";
					return tkn;
				}
				//Error lexico si es un caracter extraño?
			}
			case 7:{
				//Estado que reconoce el token AND
				if(caracterActual=='&'){
					consumir();
					//token es AND
					linea=this.entradaSalida.getNroLinea();
					col=this.entradaSalida.getNroColumna();
					tkn = new Token(Utilidades.TT_OPANDDOBLE,lexemaActual,linea);
					tkn.setNroColumna(col);
					this.lexemaActual="";
					return tkn;
				}else{
					estado=0;					
				}
				//Error lexico si es un caracter extraño?
				break;//break del case 7
			}
			case 8:{
				//Estado que reconoce el token OR
				if(caracterActual=='|'){
					consumir();
					//token es OR
					linea=this.entradaSalida.getNroLinea();
					col=this.entradaSalida.getNroColumna();
					tkn = new Token(Utilidades.TT_OPORDOBLE,lexemaActual,linea);
					tkn.setNroColumna(col);
					this.lexemaActual="";
					return tkn;
				}else{
					estado=0;					
				}
				//Error lexico si es un caracter extraño?
				break;//break del case 8
			}
			case 9:{
				//Estado que reconoce token String
				if(caracterActual=='"'){
					consumir();
					//token es string
					linea=this.entradaSalida.getNroLinea();
					col=this.entradaSalida.getNroColumna();
					tkn = new Token(Utilidades.TT_LITSTRING,lexemaActual,linea);
					tkn.setNroColumna(col);
					this.lexemaActual="";
					return tkn;
				}else{
					noConsumir();
				}
				//Error lexico si es un caracter extraño?
				break;//break del case 9
			}
			
			}//end_switch	
		}//end_while
		tkn=new Token(Utilidades.TT_FINARCHIVO,"",-1,-1);
		return tkn;
	}	
}
