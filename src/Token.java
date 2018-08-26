
public class Token {
	
	private int tipo;
	private String lexema;
	private int nroLinea;
	private int nroColumna;	
	
	public Token (int t, String lex, int lin){
		this.tipo=t;
		this.lexema=lex;
		this.nroLinea=lin;
		this.nroColumna=0;	
	}
	
	public void setNroLinea(int l){
		this.nroLinea=l;
	}
	
	public void setNroColumna(int c){
		int k=this.lexema.length();
		this.nroColumna=c-k;
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
