import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class EMetodo extends EMiembro{
	
	
	private FormaMetodo forma;
	private Tipo tipoRetorno;
	
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
	public String toString(){
		String s="\n";
		s+="______________________________________\n";
		s+="\t| Metodo "+this.getNombre()+" |\n";
		s+="Forma:\t\t\t"+this.forma+"\n";
		s+="Tipo retorno:\t\t"+this.tipoRetorno.toString()+"\n";
		//s+="Aridad:\t\t\t"+this.getAridad()+"\n";
		s+="Parametros:\t\t"+this.parametros.toString()+"\n";		
		//s+="Descripcion:\n"+this.texto+"\n";
		if (bloque!=null) s+="\n********************************\n"+this.bloque.toString();
		s+="********************************\n";
		s+="______________________________________\n";
		return s;
	}


}
