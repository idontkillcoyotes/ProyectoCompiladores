
public class AnalizadorLexico {
	
	private EntradaSalida entradaSalida;	
	private char caracterActual;
	private String lexemaActual;
	
	
	public AnalizadorLexico(EntradaSalida io){
		this.entradaSalida=io;
		this.caracterActual=0;
		this.lexemaActual="";
	}
	
	/*
	public Token nextToken(){
		
	}
	*/
}
