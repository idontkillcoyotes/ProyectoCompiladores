import java.util.ArrayList;

public class ManejadorTS {
	
	private EClase claseActual;
	private EMiembro ambienteActual;
	
	public ManejadorTS(){
		Utl.ts=new TablaSimbolos();
		this.ambienteActual=null;
		this.claseActual=null;
	}
	
	public void crearClase(Token tn,String padre) throws SemanticException{
		EClase c=new EClase(tn,padre);
		boolean b=Utl.ts.addClase(c);
		if ( (b) && (!c.getNombre().equals("Object")) && (!c.getNombre().equals("System")) ) {
			Utl.ts.setClaseAct(c);
			this.claseActual=Utl.ts.getClaseAct();
		}
		else throw new SemanticException(tn.getNroLinea(),tn.getNroColumna(),"El nombre de la clase esta duplicado.");
	}
	
	
	public void crearMetodo(Token tn,FormaMetodo f,Tipo tipo) throws SemanticException{
		if (this.claseActual!=null){
			EMetodo m=new EMetodo(tn,f,tipo);
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
				EConstructor c=new EConstructor(tn);
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
	
	public void crearAtributo(Token tn,Tipo tipo,Visibilidad v) throws SemanticException{
		if (this.claseActual!=null){
			EAtributo a=new EAtributo(tn,tipo,v);
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
	public void consolidarTS(){
		Utl.ts.consolidar();
	}
	
	public EClase claseAct(){
		return this.claseActual;
	}	
	public EMiembro ambienteAct(){
		return this.ambienteActual;
	}

}
