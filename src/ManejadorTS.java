import java.util.ArrayList;

public class ManejadorTS {
	
	private EClase claseActual;
	private EMiembro ambienteActual;
	
	public ManejadorTS(){
		Utl.ts=new TablaSimbolos();
		this.ambienteActual=null;
		this.claseActual=null;
	}
	
	public void crearClase(Token tn,Token padre) throws SemanticException{
		//checkeo que el nombre de la clase no sea object ni system
		if ( (!tn.getLexema().equals("Object")) && (!tn.getLexema().equals("System")) ) {
			EClase c=new EClase(tn,padre);
			boolean b=Utl.ts.addClase(c);
			if (b) {
				Utl.ts.setClaseAct(c);
				this.claseActual=Utl.ts.getClaseAct();
			}
			else throw new SemanticException(tn.getNroLinea(),tn.getNroColumna(),"El nombre de la clase esta duplicado.");
		}
		else throw new SemanticException(tn.getNroLinea(),tn.getNroColumna(),"El nombre de la clase es un nombre de clase predeterminada.");
	}
	
	
	public void crearMetodo(Token tn,FormaMetodo f,Tipo tipo) throws SemanticException{
		if (this.claseActual!=null){
			Bloque b=new Bloque("Este metodo pertenece a la clase: "+this.claseActual.getNombre());
			EMetodo m=new EMetodo(claseActual,tn,f,tipo,b);
			this.ambienteActual=m;			
		}
	}
	public void agregarMetodo() throws SemanticException{
		boolean b=this.claseActual.addMetodo((EMetodo)this.ambienteActual);
		if (!b){
			throw new SemanticException(ambienteActual.getToken().getNroLinea(),ambienteActual.getToken().getNroColumna(),"Ya existe un metodo igual.");
		}
	}
	
	public void crearConstructor(Token tn) throws SemanticException{		
		if (this.claseActual!=null){
			if (tn.getLexema().equals(claseActual.getNombre())){				
				EConstructor c=new EConstructor(claseActual,tn,new Bloque("constructor"));
				this.ambienteActual=c;
			}
			else throw new SemanticException(tn.getNroLinea(),tn.getNroColumna(),"El nombre del constructor no es el mismo que el nombre de la clase.");
		}
	}
	public void agregarConstructor() throws SemanticException{
		boolean b=this.claseActual.addConstructor((EConstructor)this.ambienteActual);
		if (!b){
			throw new SemanticException(ambienteActual.getToken().getNroLinea(),ambienteActual.getToken().getNroColumna(),"Ya existe un constructor con la misma aridad.");
		}
	}
	
	public void crearAtributo(Token tn,Tipo tipo,Visibilidad vis) throws SemanticException{
		if (this.claseActual!=null){
			EAtributo a=new EAtributo(claseActual,tn,tipo,vis);
			boolean b=this.claseActual.addAtributo(a);
			if (!b){
				throw new SemanticException(tn.getNroLinea(),tn.getNroColumna(),"El nombre del atributo esta duplicado.");
			}
		}
	}	
	
	public void crearAtributos(ArrayList<EParametro> lista,Visibilidad vis) throws SemanticException {
		for(EParametro p: lista){
			crearAtributo(p.getToken(),p.getTipo(),vis);
		}
	}
	
	public void crearParametro(Token tn, Tipo tipo) throws SemanticException{
		if(this.ambienteActual!=null){
			EParametro p=new EParametro(tn,tipo);
			boolean b=this.ambienteActual.addParametro(p);
			if (!b){
				throw new SemanticException(tn.getNroLinea(),tn.getNroColumna(),"El nombre del parametro esta duplicado.");
			}
			
		}
	}
	public void consolidarTS() throws SemanticException{
		Utl.ts.consolidar();
	}
	public void imprimirTS(){
		Utl.ts.imprimir();
	}
	
	public EClase claseAct(){
		return this.claseActual;
	}	
	public EMiembro ambienteAct(){
		return this.ambienteActual;
	}

}
