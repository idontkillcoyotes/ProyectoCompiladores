import java.util.ArrayList;

public class EConstructor extends EMiembro {
	
	public EConstructor(Token t){
		this.parametros=new ArrayList<EParametro>();
		this.tokenNombre=t;
	}	
	/*
	 * Redefino equals para que considere iguales a los constructores
	 * con los mismos parametros
	 */
	public boolean equals(EConstructor c){
		return this.parametros.equals(c.getParametros());
	}	
	public ArrayList<EParametro> getParametros(){
		return this.parametros;
	}
	public String toString(){
		String s="\n";
		s+="Constructor: \n";
		s+="Parametros: "+this.parametros.toString()+"\n";		
		return s;
	}
}
