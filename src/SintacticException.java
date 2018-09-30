
public class SintacticException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SintacticException(int linea, int col, String msg){		
		super("ERROR SINTACTICO: Linea:"+linea+" Columna:"+col+".\n"+msg);
	}
}
