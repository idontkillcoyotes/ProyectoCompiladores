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
	private int cantmetdin;
	
	public EClase(Token tn,Token padre){
		this.tokenNombre=tn;
		this.tokenPadre=padre;
		this.clasePadre=null;
		this.atributos=new LinkedList<EAtributo>();
		this.metodos=new LinkedList<EMetodo>();
		this.constructores=new LinkedList<EConstructor>();
		this.main=null;
		this.consolidado=false;
		this.cantmetdin=0;
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
			if(this.equals(ancestro)){
				return true;
			}
			else{
				if (this.clasePadre!=null){
					return this.clasePadre.esSubtipo(ancestro);
				}
				else{
					return false;
				}
			}
		}
		else{
			return false;
		}
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
							"La clase "+tokenPadre.getLexema()+" no está definida.");
				}
			}
			this.consolidado=true;
		}
	}
	private void calcularOffsets(){
		int i=0;
		for(EMetodo e: metodos){
			e.calcularOffsets();
			if(!e.esEstatico()){
				e.setOffset(i);				
				i++;
			}			
		}
		for(EConstructor e: constructores){
			e.calcularOffsets();
		}
		this.cantmetdin=i;
		i=1;
		for(EAtributo e: atributos){
			e.setOffset(i);
			i++;
		}
	}
	
	@Override
	public void check() throws SemanticException {
		//Seteo clase actual
		Utl.ts.setClaseAct(this);
		calcularOffsets();
		for(EAtributo a:atributos){
			a.check();
		}		
		for(EMetodo m:metodos){
			m.check();
		}		
		for(EConstructor c:constructores){
			c.check();
		}
	}
	
	public void setValorAtributos(NodoExpresion val) {
		System.out.println("seteando valor: "+val.toString());
		for(EAtributo at: atributos){
			System.out.println("a atributo: "+at.tokenNombre.getLexema());
			at.setValor(val);
		}
	}
	
	/*
	 * Metodo usado para chequear que un atributo sea visible desde la clase actual
	 * (de la misma clase o heredado y publico)
	 */
	public EAtributo getAtributoVisible(String n) {
		EAtributo a=this.getAtributo(n);		
		if (a!=null){
			if(a.esPublico()){
				//a es publico				
				return a;
			}			
			else if(a.getClase().equals(this)){
				//esta clase es la clase en donde fue declarado el atributo
				//a es privado pero no heredado
				return a;
			}
			else{
				//esta clase no es la clase en donde fue declarado el atributo
				//a es privado y heredado
				return null;
			}
		}
		else {
			return null;			
		}
	}
	public EAtributo getAtributoPublico(String n) {
		for(EAtributo a:atributos){
			if((a.getNombre().equals(n))&&(a.esPublico())){
				return a;
			}					
		}		
		return null;	
	}	
	
	public EAtributo getAtributo(String n) {
		for(EAtributo a:atributos){
			if(a.getNombre().equals(n)){
				return a;
			}					
		}		
		return null;	
	}
	
	public EMetodo getMetodo(String n) {
		for(EMetodo m:metodos){
			if(m.getNombre().equals(n)){
				return m;
			}					
		}		
		return null;
	}
	public EMetodo getMetodoAridad(String n, int a) {
		for(EMetodo m:metodos){
			if( (m.getNombre().equals(n)) && (m.getAridad()==a)){
				return m;
			}					
		}		
		return null;
	}	
	
	public EMetodo getMetodoEstatico(String n) {
		EMetodo m=getMetodo(n);
		if ((m!=null)&&(m.esEstatico())){
			return m;
		}
		return null;
	}
	public LinkedList<EConstructor> getConstAridad(int ar) {
		LinkedList<EConstructor> lista=new LinkedList<EConstructor>();
		for(EConstructor c: constructores){
			if(c.getAridad()==ar){
				lista.add(c);
			}
		}		
		return lista;
	}
	public EConstructor getConstDefault() {
		EConstructor con=null;
		for(EConstructor c: constructores){
			if(c.getAridad()==0){
				con=c;
			}
		}
		return con;
	}

	
	public String imprimirBloques() {
		String s="\n";
		s+="______________________________________\n";
		s+="\t| Clase "+this.getNombre()+" |\n";
		if(this.tokenPadre!=null) s+="Hereda de:\t\t"+this.tokenPadre.getLexema()+"\n";		
		s+="Atributos:\n"+this.atributos.toString()+"\n";
		s+="Constructores:\n"+this.constructores.toString()+"\n";
		s+="Metodos:\n"+this.metodos.toString()+"\n";		
		s+="______________________________________\n";
		return s;
	}
	
	public int getTamañoCIR() {		
		return (this.atributos.size()+1);
	}
	public String getLabelVT() {
		String s="VT_";
		s+=this.tokenNombre.getLexema();
		return s;
	}
	
	public void generar() {
		if(this.tokenNombre.getLexema().equals("System")){
			//genero codigo de clase system
			Utl.gen("System_read_0:\nloadfp\nloadsp\nstorefp\nread\nstore 4\nstorefp\nret 1\n");
			Utl.gen("System_readI_0:\nloadfp\nloadsp\nstorefp\nread\npush 48\nsub\nstore 4\nstorefp\nret 1\n");
			Utl.gen("System_println_0:\nloadfp\nloadsp\nstorefp\nprnln\nstorefp\nret 1\n");
			Utl.gen("System_printI_1:\nloadfp\nloadsp\nstorefp\nload 4\niprint\nstorefp\nret 2\n");
			Utl.gen("System_printB_1:\nloadfp\nloadsp\nstorefp\nload 4\nbprint\nstorefp\nret 2\n");			
			Utl.gen("System_printC_1:\nloadfp\nloadsp\nstorefp\nload 4\ncprint\nstorefp\nret 2\n");			
			Utl.gen("System_printS_1:\nloadfp\nloadsp\nstorefp\nload 4\nsprint\nstorefp\nret 2\n");
			Utl.gen("System_printIln_1:\nloadfp\nloadsp\nstorefp\nload 4\niprint\nprnln\nstorefp\nret 2\n");
			Utl.gen("System_printBln_1:\nloadfp\nloadsp\nstorefp\nload 4\nbprint\nprnln\nstorefp\nret 2\n");
			Utl.gen("System_printCln_1:\nloadfp\nloadsp\nstorefp\nload 4\ncprint\nprnln\nstorefp\nret 2\n");
			Utl.gen("System_printSln_1:\nloadfp\nloadsp\nstorefp\nload 4\nsprint\nprnln\nstorefp\nret 2\n");			
			Utl.gen("\n");
		}
		else{
			if(!this.tokenNombre.getLexema().equals("Object")){
				//genero codigo VT
				Utl.gen("\n\n");
				if(this.cantmetdin>0){
					//si la clase tiene al menos un metodo dinamico:
					Utl.gen(".data");
					String s="";				
					for(EMetodo e:metodos){
						if(!e.esEstatico()){
							//System.out.println("met: "+e.nuevaEtiqueta()+" offset: "+e.getOffset());
							s+="DW "+e.getLabel()+"\n";
						}
					}
					Utl.gen(this.getLabelVT()+": "+s);
					Utl.gen("\n");
				}
				Utl.gen(".code");
				Utl.gen("\n");
				//genero codigo de metodos y constructores
				for(EConstructor e:constructores){
					e.generar();
				}
				for(EMetodo e:metodos){
					e.generar();
				}
				Utl.gen("\n");
			}
		}
	}
	public boolean tieneVT() {
		return (cantmetdin>0);
	}



}

