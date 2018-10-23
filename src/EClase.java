import java.util.Iterator;
import java.util.LinkedList;

public class EClase extends EntradaTS{
	
	private Token tokenNombre;
	private Token tokenPadre;
	private EClase clasePadre;
	private LinkedList<EAtributo> atributos;
	private LinkedList<EMetodo> metodos;
	private LinkedList<EConstructor> constructores;
	private EMetodo main;
	
	public EClase(Token tn,Token padre){
		this.tokenNombre=tn;
		this.tokenPadre=padre;
		this.clasePadre=null;
		this.atributos=new LinkedList<EAtributo>();
		this.metodos=new LinkedList<EMetodo>();
		this.constructores=new LinkedList<EConstructor>();
		this.main=null;
		consolidado=false;		
	}
	public void setConsolidado(){
		this.consolidado=true;
	}
	
	public boolean addMetodo(EMetodo m){
		if (!metodos.contains(m)){
			return this.metodos.add(m);			
		}
		else{
			return false;
		}
	}	

	private void addMetodoHeredado(EMetodo m) throws SemanticException{
		//usando soft equals
		int index=this.metodos.indexOf(m);
		//voy a recibir metodos de la clase padre en orden inverso
		if(index==-1){
			//agrego al principio
			this.metodos.addFirst(m);
		}
		else{
			//index ahora apunta al indice del metodo posiblemente redefinido en esta clase
			//debo chequear que sea redefinicion con hard equals
			EMetodo met=this.metodos.get(index);
			if(met.hardEquals(m)){
				//es redefinicion
				//ahora deberia cambiar la posicion del metodo redefinido al principio de la lista
				
				//elimino de su posicion original
				this.metodos.remove(index);
				//agrego al principio
				this.metodos.addFirst(met);
			}
			else{
				//no es redefinicion
				//sobrecarga!
				//this.metodos.addFirst(m);
				throw new SemanticException(met.getToken().getNroLinea(),met.getToken().getNroColumna(),
						"Hay conflictos con los metodos de la clase padre: "+clasePadre.getNombre()+".\n"
						+ "El metodo "+met.getNombre()+" de la clase "+this.getNombre()+" esta duplicado.");
			}
			
			
			/*
			//metodo duplicado
			
			*/
		}
	}
	
	public boolean addAtributo(EAtributo a){
		if (!atributos.contains(a)){
			return this.atributos.add(a);			
		}
		else{
			return false;
		}
	}
	private void addAtributoHeredado(EAtributo a) throws SemanticException{
		//usando soft equals
		int index=this.atributos.indexOf(a);
		//voy a recibir atributos de la clase padre en orden inverso
		if(index==-1){
			//agrego al principio
			this.atributos.addFirst(a);
		}
		else{
			//atributo duplicado
			Token t=this.atributos.get(index).getToken();
			throw new SemanticException(t.getNroLinea(),t.getNroColumna(),
					"Hay conflictos de nombres con atibutos de la clase padre "+clasePadre.getNombre()+".\n"
					+ "El atributo "+a.getNombre()+" de la clase "+this.getNombre()+" esta duplicado.");
		}
	}
	
	public boolean addConstructor(EConstructor c){
		if(!constructores.contains(c)){
			return this.constructores.add(c);
		}
		else{
			return false;
		}
	}
	
	public void setPadre(EClase padre) {
		this.clasePadre = padre;
	}
	
	@Override
	public String getNombre() {
		return tokenNombre.getLexema();
	}

	public Token getTokenPadre() {
		return this.tokenPadre;
	}

	public EClase getPadre(){
		return this.clasePadre;
	}

	public LinkedList<EAtributo> getAtributos() {
		return atributos;
	}

	public LinkedList<EMetodo> getMetodos() {
		return metodos;
	}

	public LinkedList<EConstructor> getConstructores() {
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
					this.main=m;
					return true;
				}
			}
			return false;
		}
	}
	
	public EMetodo getMain(){
		return this.main;
	}
	
	public String toString(){
		String s="\n";
		s+="______________________________________\n";
		s+="\t| Clase "+this.getNombre()+" |\n";
		if(this.tokenPadre!=null) s+="Hereda de:\t\t"+this.tokenPadre.getLexema()+"\n";
		s+="Consolidada?\t\t"+this.consolidado+"\n";
		s+="Tiene main?:\t\t"+(this.main!=null)+"\n";
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
		else return false;
	}
	
	
	private boolean herenciaCircular(EClase padre, EClase clase){
		if(padre.equals(clase)){
			return true;
		}
		else if(padre.getPadre()!=null){
			return herenciaCircular(padre.getPadre(),clase);
		}
		else{
			return false;
		}
	}
	
	public void consolidar() throws SemanticException{
		if(!this.consolidado){
			
			//primero chequeo que el padre este definido
			if(this.tokenPadre!=null){
				//obtengo el padre
				EClase cpadre=Utl.ts.getClase(this.tokenPadre.getLexema());
				
				if(cpadre!=null){
					this.clasePadre=cpadre;
					
					//chequeo herencia circular
					if (this.herenciaCircular(cpadre,this)){					
						throw new SemanticException(tokenNombre.getNroLinea(),tokenNombre.getNroColumna(),
								"Esta clase hereda de si misma. No puede existir herencia circular.");
					}					
					
					if(!cpadre.isConsolidado()){
						//si el padre no esta consolidado, consolido
						cpadre.consolidar();
					}
					
				
					if(this.constructores.isEmpty()){
						//si no hay constructor definido, genero uno sin parametros
						Token tn=new Token(Utl.TT_IDCLASE,this.tokenNombre.getLexema(),0);
						EConstructor c=new EConstructor(this,tn,"constructor generado automaticamente");
						this.constructores.add(c);
					}					
					
					
					//para cada atributo y metodo del padre, los agrego, en orden					
					//recorro las listas de miembros del padre en orden inverso para poder agregarlos
					//en el orden correcto a esta clase (al principio)
					//para mantener un offset que representaria el orden de las vtables
					Iterator<EAtributo> atributospadre=cpadre.getAtributos().descendingIterator();
					while(atributospadre.hasNext()){
						this.addAtributoHeredado(atributospadre.next());
					}
					
					Iterator<EMetodo> metodospadre=cpadre.getMetodos().descendingIterator();
					while(metodospadre.hasNext()){
						this.addMetodoHeredado(metodospadre.next());
					}
					
					
					//luego de agregar miembros heredados, consolido todos los miembros
					for(EAtributo e: this.atributos){
						e.consolidar();
					}					
					for(EMetodo e: this.metodos){
						e.consolidar();
					}
					for(EConstructor e: this.constructores){
						e.consolidar();
					}
					
				}
				else{
					//cpadre es nulo, no esta definido
					throw new SemanticException(tokenPadre.getNroLinea(),tokenPadre.getNroColumna(),
							"La clase "+tokenPadre.getLexema()+" no est� definida.");
				}
			}
			this.consolidado=true;
		}
	}
	@Override
	public void check() throws SemanticException {
		//Seteo clase actual
		Utl.ts.setClaseAct(this);
		for(EMetodo m:metodos){
			m.check();
		}		
		for(EConstructor c:constructores){
			c.check();
		}
	}
	
	public void setValorAtributos(NodoExpresion val) {
		for(EAtributo at: atributos){
			at.setValor(val);
		}
	}
	
}

