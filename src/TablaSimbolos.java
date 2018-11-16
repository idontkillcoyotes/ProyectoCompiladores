import java.util.HashSet;

public class TablaSimbolos {
	
	private HashSet<EClase> clases;
	private EClase claseActual;
	private EMiembro miembroActual;
	private NodoBloque bloqueActual;
	private EMetodo main;
	private boolean consolidado;
	
	public TablaSimbolos(){
		this.clases=new HashSet<EClase>();
		this.consolidado=false;
		this.claseActual=null;
		this.miembroActual=null;
		this.bloqueActual=null;
		this.main=null;		
	}

	public boolean addClase(EClase c){
		return this.clases.add(c);
	}
	
	public EMetodo getMain(){
		return this.main;
	}
	
	public void setClaseAct(EClase c){
		this.claseActual=c;
	}
	
	public EClase getClaseAct(){
		return claseActual;
	}	

	public EMiembro getMiembroAct() {
		return miembroActual;
	}

	public void setMiembroActual(EMiembro m) {
		this.miembroActual = m;
	}

	public NodoBloque getBloqueAct() {
		return bloqueActual;
	}

	public void setBloqueActual(NodoBloque b) {
		this.bloqueActual = b;
	}

	public void consolidar() throws SemanticException{
		//Primero consolido declaraciones
		boolean main=false;
		for(EClase c: clases){
			c.consolidar();
			main=(main||c.hasMain());
			if (main){
				this.main=c.getMain();
			}
		}
		if(!main){
			throw new SemanticException(0,0,"Falta un metodo main.");
		}
		this.consolidado=true;
		
		//Luego controlo sentencias
		for(EClase c: clases){
			//checkeo clase por clase
			c.check();	
		}
		
		//Y por ultimo genero codigo
		generarCodPred();
		
		for(EClase c: clases){
			c.generar();
		}
		
		//cierro archivo de salida
		Utl.getES().cerrarArchivoSalida();
		
	}
	private void generarCodPred() {
		//genero codigo principal
		Utl.gen(".code \npush main ;cargo main\ncall ;llamo a main\nhalt ;termino\n");
		//genero codigo malloc		
		Utl.gen("malloc:");
		Utl.gen("loadfp ;Inicialización unidad \nloadsp \nstorefp ;Finaliza inicialización del RA");
		Utl.gen("LOADHL ;hl\nDUP ;hl\nPUSH 1 ;1\nADD ;hl+1\nSTORE 4 ;Guarda el resultado (un puntero a la primer celda de la región de memoria)");
		Utl.gen("LOAD 3 ;Carga la cantidad de celdas a alojar (parámetro que debe ser positivo)");
		Utl.gen("ADD\nSTOREHL ;Mueve el heap limit (hl). Expande el heap\nSTOREFP\nRET 1	;Retorna eliminando el parámetro");
		Utl.gen("\n\n");
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
		System.out.println("Total de clases en la tabla: "+this.clases.size());
		for(EClase c: clases){
			if ((!c.getNombre().equals("Object"))&&(!c.getNombre().equals("System"))){
				System.out.println(c.toString());
				System.out.println("*****************************************");
			}			
		}
	}
	
	public boolean isConsolidado(){
		return consolidado;
	}

	public HashSet<EClase> getClases() {
		return clases;
	}

	public void imprimirBloques() {
		System.out.println("*********************");
		System.out.println("\tClases:");
		System.out.println("*********************");
		for(EClase c: clases){
			if ((!c.getNombre().equals("Object"))&&(!c.getNombre().equals("System"))){
				System.out.println(c.imprimirBloques());
				System.out.println("*****************************************");
			}			
		}
		
	}
	
	
	
}
