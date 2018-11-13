import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class EMetodo extends EMiembro{
	
	
	private FormaMetodo forma;
	private Tipo tipoRetorno;
	private int offset;
	
	public EMetodo(EClase clase,Token tn,FormaMetodo f,Tipo tiporet,String texto){
		this.clase=clase;
		this.tokenNombre=tn;
		this.forma=f;
		this.tipoRetorno=tiporet;		
		this.parametros=new ArrayList<EParametro>();
		this.varslocales=new LinkedList<EParametro>();
		this.texto=texto;
		this.bloque=null;
		this.consolidado=false;
		this.esConstructor=false;
		this.offset=-1;
		this.contifs=0;
		this.contwhiles=0;
		this.contvarlocales=0;
		this.generado=false;
	}	

	public FormaMetodo getForma() {
		return forma;
	}
	
	public Tipo getTipoRetorno() {	
		return tipoRetorno;
	}
	
	@Override
	public boolean esEstatico() {
		return (this.forma==FormaMetodo.fStatic);
	}
	
	@Override
	public boolean equals(Object e){
		EMetodo met=(EMetodo) e;
		if(this.getNombre().equals(met.getNombre())){
			if(this.getAridad()==met.getAridad()){
				//Mismo nombre misma aridad	
				return true;
			}
			else{
				//Mismo nombre distinta aridad
				return false;
			}
		}
		else{
			//Distinto nombre
			return false;
		}
	}
	
	@Override
	public String getLabel(){
		String s="";
		if (this.isMain()){
			s="main";
		}
		else{
			s+=this.clase.getNombre()+"_";
			s+=this.tokenNombre.getLexema()+"_"+this.getAridad();
		}
		return s; 
	}
	
	@Override
	public int hashCode() {		
		int hash=this.getNombre().hashCode();
		return hash;
	}
	
	public boolean hardEquals(EMetodo m) {
		//el nombre y la aridad siempre van a ser iguales
		if( ( this.forma.equals(m.getForma()) ) &&
			( this.tipoRetorno.mismoTipo(m.getTipoRetorno()) ) &&			
			( this.parametrosIguales(m.getParametros()) )){
			return true;			
		}
		else{ 
			return false;
		}		
	}
	
	
	private boolean parametrosIguales(ArrayList<EParametro> par) {
		boolean igual=true;
		//no es necesario comparar la cantidad de elementos porque siempre seran iguales
		Iterator<EParametro> params=par.iterator();
		for(EParametro p: this.parametros){
			if(params.hasNext()){					
				//solo me importa el orden y los tipos de los parametros
				igual=(igual && (p.getTipo().mismoTipo(params.next().getTipo()))) ;
			}
		}
		return igual;
	}
	
	public boolean tieneRetorno(){
		if(this.tipoRetorno.esTipo(Utl.TPC_VOID))
			return false;
		else
			return true;		
	}
	
	public boolean isMain(){
		if( (this.forma==FormaMetodo.fStatic)&&
			(this.tipoRetorno.esTipo(Utl.TPC_VOID))&&
			(this.getNombre().equals("main"))&&
			(this.getAridad()==0) ){
			return true;
		}
		else return false;
	}
	
	@Override
	public void consolidar() throws SemanticException{
		if(!consolidado){
			if (!this.tipoRetorno.estaDefinido()){
				throw new SemanticException(tipoRetorno.getToken().getNroLinea(),tipoRetorno.getToken().getNroColumna(),
						"El tipo "+this.tipoRetorno.getTipo()+" no está definido.");
			}
			for(EParametro p: parametros){
				p.consolidar();
			}
			this.consolidado=true;
		}
	}
	@Override
	public void check() throws SemanticException {
		//seteo miembro actual
		Utl.ts.setMiembroActual(this);
		Utl.ts.setBloqueActual(null);
		//agrego parametros a las varslocales
		this.copiarParametros();
		if (bloque!=null) bloque.check();
		//chequeo que tenga al menos un retorno si no es void
		if (!this.tipoRetorno.esTipo(Utl.TPC_VOID)){
			//si no es retorno void
			if(this.bloque!=null){
				if(!this.bloque.tieneRetorno()){
					throw new SemanticException(tokenNombre.getNroLinea(),tokenNombre.getNroColumna(),
							"Metodo sin retorno.\nEste metodo debe retornar un valor de tipo "+tipoRetorno.toString()+".");
				}
			}
		}
	}
	
	
	@Override
	public String toString(){
		String s="\n";
		s+="______________________________________\n";
		s+="\t| Metodo "+this.getNombre()+" |\n";
		s+="Forma:\t\t\t"+this.forma+"\n";
		s+="Tipo retorno:\t\t"+this.tipoRetorno.toString()+"\n";
		//s+="Aridad:\t\t\t"+this.getAridad()+"\n";
		s+="Parametros:\t\t"+this.parametros.toString()+"\n";
		s+="Cant var locales:\t"+this.contvarlocales+"\n";
		//s+="Cant ifs:\t\t"+this.contifs+"\n";
		//s+="Cant whiles:\t\t"+this.contwhiles+"\n";
		//s+="Descripcion:\n"+this.texto+"\n";
		if (bloque!=null) s+="\n********************************\n"+this.bloque.toString();
		s+="********************************\n";
		s+="______________________________________\n";
		return s;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}
	


	@Override
	public void generar() {
		String s=this.getNombre();
		if(!generado){
			Utl.gen(this.getLabel()+":"); //genero etiqueta
			Utl.gen("loadfp\t\t\t;inicio de ra (emetodo "+s+")");
			Utl.gen("loadsp\t\t\t;(emetodo "+s+")");
			Utl.gen("storefp\t\t\t;fin de ra (emetodo "+s+")");
			Utl.gen("rmem "+this.contvarlocales+"\t\t\t;reservo espacio para var locales (emetodo "+s+")");
			if (this.bloque!=null) 
				this.bloque.generar();
			if (!this.bloque.tieneRetorno()){
				//si el bloque no tiene retorno debo hacerlo aca
				Utl.gen("fmem "+this.contvarlocales+"\t\t\t;libero espacio de var locales (emetodo "+s+")");
				Utl.gen("storefp\t\t\t;retorno (emetodo "+s+")");
				Utl.gen("ret "+(this.parametros.size()+1)+"\t\t\t;retorno (emetodo "+s+")\n");
			}
			this.generado=true;
		}
	}


}
