import java.util.HashSet;

public class EClase extends EntradaTS{
	
	private Token tokenNombre;
	private String padre;
	private HashSet<EAtributo> atributos;
	private HashSet<EMetodo> metodos;
	private HashSet<EConstructor> constructores;
	
	public EClase(Token tn,String padre){
		this.tokenNombre=tn;
		this.padre=padre;
		this.atributos=new HashSet<EAtributo>();
		this.metodos=new HashSet<EMetodo>();
		this.constructores=new HashSet<EConstructor>();
		consolidado=false;		
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

	public Token getToken() {
		return tokenNombre;
	}
	
	@Override
	public boolean equals(Object e){
		EClase c=(EClase) e;
		return this.getNombre().equals(c.getNombre());
	}
	
	@Override
	public int hashCode() {
		int hash=this.getNombre().hashCode();
		return hash;
	}
	
	
	public String toString(){
		String s="\n";
		s+="Clase:\t"+this.getNombre()+"\n";
		s+="Hereda de:\t"+this.padre+"\n";
		s+="Consolidada? "+this.consolidado+"\n";
		s+="-------------------------------------\n";
		s+="Atributos:\n"+this.atributos.toString()+"\n";
		s+="-------------------------------------\n";
		s+="Constructores:\n"+this.constructores.toString()+"\n";
		s+="-------------------------------------\n";
		s+="Metodos:\n"+this.metodos.toString()+"\n";		
		return s;
	}
	public void consolidar() {
		System.out.println("consolidando clase...");
		for (EAtributo a: atributos){
			a.consolidar();
		}
		for (EConstructor a: constructores){
			a.consolidar();
		}
		for (EMetodo a: metodos){
			a.consolidar();
		}
		consolidado=true;
	}
}

