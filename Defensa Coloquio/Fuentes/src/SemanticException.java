
public class SemanticException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SemanticException(int linea, int col, String msg){		
		super("ERROR SEMANTICO: Linea:"+linea+" Columna:"+col+".\n"+msg);
	}
}
