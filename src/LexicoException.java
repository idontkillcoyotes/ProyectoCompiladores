
public class LexicoException extends Exception {

	public LexicoException(int lin, int col, String msg){		
		super("ERROR LEXICO: Linea:"+lin+" Columna:"+col+".\n"+msg);
	}
}
