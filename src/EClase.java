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
	 * True - si se pudo agregar el elemento, en caso de redefinicion se
	 * queda con el metodo de la subclase
	 * False - si ya existia un elemento igual
	 *
	private boolean addMetodoHeredado(EMetodo m) {
		boolean b=this.metodos.add(m);
		if(!b){
			//no se pudo agregar el metodo
			//ver si es una redefinicion
			
			//TODO funcionaria con solo el addMetodo?
		}
		return b;
	}
	*/	
	
	public boolean addAtributo(EAtributo a){
		return this.atributos.add(a);
	}
	
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
	
	/*
	 * Retorna true si esta clase es subtipo de ancestro
	 * false en caso contrario
	 */
	public boolean esSubtipo(EClase ancestro){
		if(consolidado){
			//Si clase padre es ancestro -> true
			if(this.clasePadre.equals(ancestro)){
				return true;
			}
			//Si clase padre es nula (object) -> false
			else if (this.clasePadre==null){
				return false;
			}
			else{
				// -> clase padre es descendiente de ancestro?
				return this.clasePadre.esSubtipo(ancestro);
			}
		}
		else{
			return false;
		}
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
				
				for(EAtributo e: atributos){
					e.consolidar();
				}
				for(EMetodo e: metodos){
					e.consolidar();
				}
				for(EConstructor e: constructores){
					e.consolidar();
				}
				
			}
			else throw new SemanticException(tokenPadre.getNroLinea(),tokenPadre.getNroColumna(),"La clase "+tokenPadre.getLexema()+" no está definida.");		
		}
		this.consolidado=true;
	}	
}

