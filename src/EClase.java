import java.util.ArrayList;
import java.util.HashSet;

public class EClase {
	
	private Token tokenNombre;
	private String padre;
	private HashSet<EAtributo> atributos;
	private HashSet<EMetodo> metodos;
	private HashSet<EConstructor> constructores;
	private boolean consolidado;
	
	public EClase(Token tn){
		this.tokenNombre=tn;
		this.padre=null;
		this.atributos=new HashSet<EAtributo>();
		this.metodos=new HashSet<EMetodo>();
		this.constructores=new HashSet<EConstructor>();
		this.consolidado=false;		
	}
	
	/*
	 * Redefino equals para que considere dos clases iguales
	 * cuando tienen el mismo nombre
	 */
	public boolean equals(EClase otraClase){
		return this.getNombre().equals(otraClase.getNombre());
	}
	/*
	 * Retorna:
	 * True - si se pudo agregar el elemento
	 * False - si ya existia un elemento igual
	 */
	public boolean addMetodo(EMetodo m){
		return this.metodos.add(m);
	}
	
	/*
	 * Retorna:
	 * True - si se pudo agregar el elemento
	 * False - si ya existia un elemento igual
	 */
	public boolean addAtributo(EAtributo a){
		return this.atributos.add(a);
	}
	public boolean addAtributos(ArrayList<EAtributo> atrs) {
		boolean error=false;
		while(!error){
			for(EAtributo a: atrs){
				error=this.addAtributo(a);
			}
		}
		return error;
	}
	
	/*
	 * Retorna:
	 * True - si se pudo agregar el elemento
	 * False - si ya existia un elemento igual
	 */
	public boolean addConstructor(EConstructor c){
		return this.constructores.add(c);
	}
	
	public String getNombre() {
		return tokenNombre.getLexema();
	}

	public String getPadre() {
		return padre;
	}

	public void setPadre(String padre) {
		this.padre = padre;
	}

	public HashSet<EAtributo> getAtributos() {
		return atributos;
	}

	public HashSet<EMetodo> getMetodos() {
		return metodos;
	}

	public HashSet<EConstructor> getConstructores() {
		return constructores;
	}
	
	public boolean isConsolidado() {
		return consolidado;
	}
	public Token getToken() {
		return tokenNombre;
	}
	
	
	
}

