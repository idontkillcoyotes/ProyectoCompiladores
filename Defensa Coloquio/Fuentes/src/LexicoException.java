
public class LexicoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LexicoException(int lin, int col, String msg){		
		super("ERROR LEXICO: Linea:"+lin+" Columna:"+col+".\n"+msg);
	}
}
