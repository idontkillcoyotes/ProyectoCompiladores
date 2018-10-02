import java.util.HashSet;

public class TablaSimbolos {
	
	private HashSet<EClase> clases;
	private EClase claseActual;
	private boolean consolidado;
	
	public TablaSimbolos(){
		this.clases=new HashSet<EClase>();
		this.consolidado=false;
		this.claseActual=null;
	}
	
	/*
	 * Retorna:
	 * True - si se pudo agregar el elemento
	 * False - si ya existia un elemento igual
	 */
	public boolean addClase(EClase c){
		return this.clases.add(c);
	}
	
	public void setClaseAct(EClase c){
		this.claseActual=c;
	}
	
	public EClase getClaseAct(){
		return claseActual;
	}
	
	public void consolidar() throws SemanticException{
		for(EClase c: clases){
			c.consolidar();
		}
		this.consolidado=true;
	}
	public EClase getClase(String n){
		EClase ret=null;
		for(EClase c: clases){
			if(c.getNombre().equals(n)){
				return c;
			}
		}
		return ret;
	}	
	
	public boolean estaDefinida(String n){
		boolean ret=false;
		for(EClase c: clases){
			if(c.getNombre().equals(n)){
				return true;
			}
		}
		return ret;
	}
	
	public void imprimir(){
		System.out.println("*****************************************");
		System.out.println("\tTABLA DE SIMBOLOS:");
		System.out.println("*****************************************");
		for(EClase c: clases){
			System.out.println(c.toString());
			System.out.println("***************************");
		}
	}
	
	public boolean isConsolidado(){
		return consolidado;
	}

	public HashSet<EClase> getClases() {
		return clases;
	}
	
}
