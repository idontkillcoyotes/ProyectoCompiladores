import java.util.ArrayList;

public class EMetodo extends EMiembro{
	
	
	private FormaMetodo forma;
	private Tipo tipoRetorno;
	
	public EMetodo(EClase clase,Token tn,FormaMetodo f,Tipo tiporet,Bloque b){
		this.clase=clase;
		this.tokenNombre=tn;
		this.forma=f;
		this.tipoRetorno=tiporet;		
		this.parametros=new ArrayList<EParametro>();
		this.bloque=b;
		this.consolidado=false;
	}	

	public FormaMetodo getForma() {
		return forma;
	}
	
	public Tipo getTipoRetorno() {
		return tipoRetorno;
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
	public boolean isMain(){
		if( (this.forma==FormaMetodo.fStatic)&&
			(this.tipoRetorno.esTipo(Utl.TPC_VOID))&&
			(this.getNombre().equals("main"))&&
			(this.getAridad()==0) ){
			return true;
		}
		else return false;
	}
	
	
	public String toString(){
		String s="\n";
		s+="______________________________________\n";
		s+="\t| Metodo "+this.getNombre()+" |\n";
		s+="Forma:\t\t\t"+this.forma+"\n";
		s+="Tipo retorno:\t\t"+this.tipoRetorno.toString()+"\n";
		s+="Aridad:\t\t\t"+this.getAridad()+"\n";
		s+="Parametros:\n"+this.parametros.toString()+"\n";		
		s+="Bloque:\n"+this.bloque.getContenido()+"\n";
		s+="______________________________________\n";
		return s;
	}

	public void consolidar() throws SemanticException {
		for(EParametro p: parametros){
			p.consolidar();
		}
		consolidado=true;		
	}

}
