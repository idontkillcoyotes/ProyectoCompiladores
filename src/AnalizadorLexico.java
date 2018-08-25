
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
	
	
	public Token nextToken(){
		Token tkn;
		int estado=0;
		while (estado!=-1){
			switch (estado){		
			case 0:
		
			}
		}
	}
	
}
