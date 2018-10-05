import java.util.ArrayList;

public class EConstructor extends EMiembro {
	
	public EConstructor(EClase clase,Token t,Bloque b){
		this.clase=clase;
		this.parametros=new ArrayList<EParametro>();
		this.tokenNombre=t;		
		this.bloque=b;
		consolidado=false;
	}	

	
	@Override
	public boolean equals(Object e){
		EConstructor cons=(EConstructor) e;
		return (this.getAridad()==cons.getAridad());
	}
	@Override
	public int hashCode() {
		int hash=this.getNombre().hashCode();
		return hash;	
	}
	
	public String toString(){
		String s="\n";
		s+="______________________________________\n";
		s+="\t| Constructor |\n";
		s+="Aridad:\t\t\t"+this.getAridad()+"\n";
		s+="Parametros:\n"+this.parametros.toString()+"\n";
		s+="Bloque:\n"+this.bloque.getContenido()+"\n";
		s+="______________________________________\n";
		return s;
	}
}
