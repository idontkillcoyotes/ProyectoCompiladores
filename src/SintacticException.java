
public class SintacticException extends Exception {

	public SintacticException(int linea, int col, String msg){		
		super("ERROR SINTACTICO: Linea:"+linea+" Columna:"+col+".\n"+msg);
	}
}
