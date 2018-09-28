
public class SemanticException extends Exception {

	public SemanticException(int linea, int col, String msg){		
		super("ERROR SEMANTICO: Linea:"+linea+" Columna:"+col+".\n"+msg);
	}
}
