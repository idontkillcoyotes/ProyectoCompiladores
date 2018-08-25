
public class Token {
	
	private int tipo;
	private String lexema;
	private int nroLinea;
	private int nroColumna;
	
	public Token (int t, String lex, int lin, int col){
		this.tipo=t;
		this.lexema=lex;
		this.nroLinea=lin;
		this.nroColumna=col;		
	}
	
	public int getTipo() {
		return tipo;
	}
	public String getLexema() {
		return lexema;
	}
	public int getNroLinea() {
		return nroLinea;
	}
	public int getNroColumna() {
		return nroColumna;
	}
	
	public boolean esTipo(int otroTipo){
		return (this.tipo == otroTipo);		
	}
	
	public boolean esTipo(int[] tipos){
		boolean toReturn=false;
		for (int t: tipos){
			if (this.tipo==t){
				toReturn=true;
			}
		}
		return toReturn;
	}

}
