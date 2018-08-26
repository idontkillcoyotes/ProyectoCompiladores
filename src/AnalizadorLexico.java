
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
						this.lexemaActual="";
						linea=this.entradaSalida.getNroLinea();
						col=this.entradaSalida.getNroColumna();						
						tkn = new Token(Utilidades.TT_OPSUMA,"+",linea);
						tkn.setNroColumna(col);
						return tkn;
					}					
					if (caracterActual=='*'){
						//token es multiplicacion
						this.lexemaActual="";
						linea=this.entradaSalida.getNroLinea();
						col=this.entradaSalida.getNroColumna();						
						tkn = new Token(Utilidades.TT_OPMULT,"*",linea);
						tkn.setNroColumna(col);
						return tkn;
					}
					if (caracterActual=='('){
						//token es parentesis abierto
						this.lexemaActual="";
						linea=this.entradaSalida.getNroLinea();
						col=this.entradaSalida.getNroColumna();						
						tkn = new Token(Utilidades.TT_PUNPARENT_A,"(",linea);
						tkn.setNroColumna(col);
						return tkn;
					}
					if (caracterActual==')'){
						//token es parentesis cerrado
						this.lexemaActual="";
						linea=this.entradaSalida.getNroLinea();
						col=this.entradaSalida.getNroColumna();						
						tkn = new Token(Utilidades.TT_PUNPARENT_C,")",linea);
						tkn.setNroColumna(col);
						return tkn;
					}
					if (caracterActual=='{'){
						//token es llave abierta
						this.lexemaActual="";
						linea=this.entradaSalida.getNroLinea();
						col=this.entradaSalida.getNroColumna();						
						tkn = new Token(Utilidades.TT_PUNLLAVE_A,"{",linea);
						tkn.setNroColumna(col);
						return tkn;
					}
					if (caracterActual=='}'){
						//token es llave cerrada
						this.lexemaActual="";
						linea=this.entradaSalida.getNroLinea();
						col=this.entradaSalida.getNroColumna();						
						tkn = new Token(Utilidades.TT_PUNLLAVE_C,"}",linea);
						tkn.setNroColumna(col);
						return tkn;
					}
					if (caracterActual=='['){
						//token es corchete abierto
						this.lexemaActual="";
						linea=this.entradaSalida.getNroLinea();
						col=this.entradaSalida.getNroColumna();						
						tkn = new Token(Utilidades.TT_PUNCORCH_A,"[",linea);
						tkn.setNroColumna(col);
						return tkn;
					}
					if (caracterActual==']'){
						//token es corchete cerrado
						this.lexemaActual="";
						linea=this.entradaSalida.getNroLinea();
						col=this.entradaSalida.getNroColumna();						
						tkn = new Token(Utilidades.TT_PUNCORCH_C,"]",linea);
						tkn.setNroColumna(col);
						return tkn;
					}
					if (caracterActual==';'){
						//token es punto coma
						this.lexemaActual="";
						linea=this.entradaSalida.getNroLinea();
						col=this.entradaSalida.getNroColumna();						
						tkn = new Token(Utilidades.TT_PUNPUNTOCOMA,";",linea);
						tkn.setNroColumna(col);
						return tkn;
					}
					if (caracterActual=='.'){
						//token es punto
						this.lexemaActual="";
						linea=this.entradaSalida.getNroLinea();
						col=this.entradaSalida.getNroColumna();						
						tkn = new Token(Utilidades.TT_PUNPUNTO,".",linea);
						tkn.setNroColumna(col);
						return tkn;
					}
					if (caracterActual==','){
						//token es coma
						this.lexemaActual="";
						linea=this.entradaSalida.getNroLinea();
						col=this.entradaSalida.getNroColumna();						
						tkn = new Token(Utilidades.TT_PUNCOMA,",",linea);
						tkn.setNroColumna(col);
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
						//posibles token mayor migual
						consumir();
						estado=5;
						break;
					}
					if (caracterActual=='>'){
						//posibles token menor migual
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
					if ((caracterActual=='_')||(Character.isLetter(caracterActual))||(Character.isDigit(caracterActual))){
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
					if ((caracterActual=='_')||(Character.isLetter(caracterActual))||(Character.isDigit(caracterActual))){
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
					break;//break del case 2					
			}
			case 4:{
					//Estado que reconoce tokens 
					break;
			}
			
			
			}//end_switch	
		}//end_while
	}	
}
