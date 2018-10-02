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

	public boolean addMetodo(EMetodo m){
		return this.metodos.add(m);
	}	

	private boolean addMetodoHeredado(EMetodo m) {		
		if (!this.metodos.contains(m)){
			System.out.println("\nMetodo: "+m.getNombreAridad()+" no contenido en metodos.\n");
			this.metodos.add(m);
		}
		return true;
	}
	
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
	public boolean hasMain(){
		if(this.metodos.isEmpty()){
			return false;
		}
		else{
			for(EMetodo m: metodos){
				if(m.isMain()){
					return true;
				}
			}
			return false;
		}
	}
	
	
	
	public String toString(){
		String s="\n";
		s+="______________________________________\n";
		s+="\t| Clase "+this.getNombre()+" |\n";
		s+="Hereda de:\t\t"+this.tokenPadre.getLexema()+"\n";
		s+="Consolidada?\t\t"+this.consolidado+"\n";
		s+="Atributos:\n"+this.atributos.toString()+"\n";
		s+="Constructores:\n"+this.constructores.toString()+"\n";
		s+="Metodos:\n"+this.metodos.toString()+"\n";
		s+="______________________________________\n";
		return s;
	}
	
	/*
	 * Retorna true si esta clase es subtipo de ancestro
	 * false en caso contrario
	 */
	public boolean esSubtipo(EClase ancestro){
		if(consolidado){
			if(this.clasePadre!=null){
				if(this.clasePadre.equals(ancestro)){
					//clase padre es ancestro
					return true;
				}
				else{
					//> clase padre es descendiente de ancestro?
					return this.clasePadre.esSubtipo(ancestro);
				}
			}			
			else{
				//clase padre null -> false
				return false;				
			}
		}
		else{
			return false;
		}
	}
	

	
	public void consolidar() throws SemanticException{
		boolean b;
		if (!tokenPadre.getLexema().equals("Object")){					
			//Chequear que la clase padre esta definida
			EClase cpadre=Utl.ts.getClase(this.tokenPadre.getLexema());
			if (cpadre!=null){
				this.clasePadre=cpadre;
								
				
				//Agrego miembros de padre			
				for (EAtributo atr: cpadre.getAtributos()){
					b=this.addAtributo(atr);
					if (!b) throw new SemanticException(tokenNombre.getNroLinea(),tokenNombre.getNroColumna(),
							"Hay conflictos de nombres con atibutos de la clase padre:"+cpadre.getNombre()+".\n"
							+ "El atributo:"+atr.getNombre()+", esta duplicado.");					
				}
				for (EMetodo met: cpadre.getMetodos()){
					b=this.addMetodoHeredado(met);
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
		if(this.constructores.isEmpty()){
			//Si no hay constructor definido, genero uno sin parametros
			Token tn=new Token(Utl.TT_IDCLASE,this.tokenNombre.getLexema(),0);
			EConstructor c=new EConstructor(this,tn,new Bloque("constructor generado automaticamente"));
			this.constructores.add(c);
		}	
		
		this.consolidado=true;
		
		//Chequeo herencia circular:
		if (this.esSubtipo(this)){					
			throw new SemanticException(tokenNombre.getNroLinea(),tokenNombre.getNroColumna(),
					"Esta clase hereda de si misma. No puede existir herencia circular.");
		}
	}	
}

