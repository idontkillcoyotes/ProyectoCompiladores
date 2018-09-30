import java.util.HashSet;

public class EClase extends EntradaTS{
	
	private Token tokenNombre;
	private Token tokenPadre;
	private EClase clasePadre;
	private HashSet<EAtributo> atributos;
	private HashSet<EMetodo> metodos;
	private HashSet<EConstructor> constructores;
	
	public EClase(Token tn,Token padre){
		this.tokenNombre=tn;
		this.tokenPadre=padre;
		this.clasePadre=null;
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
	
	public void setPadre(EClase padre) {
		this.clasePadre = padre;
	}
	
	public String getNombre() {
		return tokenNombre.getLexema();
	}

	public Token getTokenPadre() {
		return this.tokenPadre;
	}

	public EClase getPadre(){
		return this.clasePadre;
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
		s+="Hereda de:\t"+this.tokenPadre.getLexema()+"\n";
		s+="Consolidada? "+this.consolidado+"\n";
		s+="-------------------------------------\n";
		s+="Atributos:\n"+this.atributos.toString()+"\n";
		s+="-------------------------------------\n";
		s+="Constructores:\n"+this.constructores.toString()+"\n";
		s+="-------------------------------------\n";
		s+="Metodos:\n"+this.metodos.toString()+"\n";		
		return s;
	}
	
	public void consolidar() throws SemanticException{
		boolean b;
		if (!tokenPadre.getLexema().equals("Object")){					
			//Checkear que la clase padre esta definida
			EClase cpadre=Utl.ts.getClase(this.tokenPadre.getLexema());
			if (cpadre!=null){
				this.clasePadre=cpadre;
				//Agregar miembros de padre			
				for (EAtributo atr: cpadre.getAtributos()){
					b=this.addAtributo(atr);
					if (!b) throw new SemanticException(tokenNombre.getNroLinea(),tokenNombre.getNroColumna(),
							"Hay conflictos de nombres con atibutos de la clase padre:"+cpadre.getNombre()+".\n"
							+ "El atributo:"+atr.getNombre()+", esta duplicado.");
				}
				for (EMetodo met: cpadre.getMetodos()){
					b=this.addMetodo(met);
					if (!b) throw new SemanticException(tokenNombre.getNroLinea(),tokenNombre.getNroColumna(),
							"Hay conflictos con los metodos de la clase padre: "+cpadre.getNombre()+".\n"
							+ "El metodo: "+met.getNombre()+", esta duplicado.");
				}
				
			}
			else throw new SemanticException(tokenPadre.getNroLinea(),tokenPadre.getNroColumna(),"La clase "+tokenPadre.getLexema()+" no está definida.");
		
		}	
	}

	
}

