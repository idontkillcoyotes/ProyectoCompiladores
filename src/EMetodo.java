import java.util.ArrayList;

public class EMetodo extends EMiembro{
	
	
	private FormaMetodo forma;
	private String tipoRetorno;
	
	private boolean consolidado;
	
	public EMetodo(Token tn,FormaMetodo f,String tiporet){
		this.tokenNombre=tn;
		this.forma=f;
		this.tipoRetorno=tiporet;		
		this.parametros=new ArrayList<EParametro>();
		this.consolidado=false;
	}	

	public FormaMetodo getForma() {
		return forma;
	}
	
	public String getTipoRetorno() {
		return tipoRetorno;
	}
	
	public boolean isConsolidado() {
		return consolidado;
	}
	
	/*
	 * Redefino equals para que considere a dos metodos iguales
	 * cuando su nombre es igual
	 * TODO logro de metodos con mismo nombre y distintos parametros
	 */
	public boolean equals(EMetodo m){
		return this.getNombre().equals(m.getNombre());
	}
	
	public String toString(){
		String s="\n";
		s+="Metodo "+this.getNombre()+"\n";
		s+="Forma: "+this.forma+"\n";
		s+="Tipo retorno: "+this.tipoRetorno+"\n";
		s+="Parametros: "+this.parametros.toString()+"\n";		
		return s;
	}
}
