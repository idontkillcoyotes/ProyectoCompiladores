
public class SintacticException extends Exception {

	public SintacticException(int linea, int col, String msg){		
		super("ERROR SINTACTICO: Lin:"+linea+" Col:"+col+" "+msg);
	}
}
