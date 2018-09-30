import java.util.ArrayList;

public class EMetodo extends EMiembro{
	
	
	private FormaMetodo forma;
	private String tipoRetorno;
	
	public EMetodo(Token tn,FormaMetodo f,String tiporet){
		this.tokenNombre=tn;
		this.forma=f;
		this.tipoRetorno=tiporet;		
		this.parametros=new ArrayList<EParametro>();
		this.consolidado=false;
	}	

	public FormaMetodo getForma() {
		return forma;
	}
	
	public String getTipoRetorno() {
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
		s+="Tipo retorno:\t"+this.tipoRetorno+"\n";
		s+="Aridad:\t"+this.getAridad()+"\n";
		s+="Parametros:\n"+this.parametros.toString()+"\n";		
		return s;
	}

	public void consolidar() {
		System.out.println("consolidando metodo...");
		for(EParametro p: parametros){
			p.consolidar();
		}
		consolidado=true;		
	}

}
