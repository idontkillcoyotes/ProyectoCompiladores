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
	
	public String toString(){
		String s="\n";
		s+="Metodo:\t"+this.getNombre()+"\n";
		s+="Forma:\t"+this.forma+"\n";
		s+="Tipo retorno:\t"+this.tipoRetorno.toString()+"\n";
		s+="Aridad:\t"+this.getAridad()+"\n";
		s+="Parametros:\n"+this.parametros.toString()+"\n";
		s+="______________________________________\n";
		s+="Bloque:\t"+this.bloque.getContenido()+"\n";
		return s;
	}

	public void consolidar() throws SemanticException {
		System.out.println("consolidando metodo...");
		for(EParametro p: parametros){
			p.consolidar();
		}
		consolidado=true;		
	}

}
