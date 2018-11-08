import java.util.ArrayList;
import java.util.LinkedList;

public class EConstructor extends EMiembro {
	
	public EConstructor(EClase clase,Token t,String texto){
		this.clase=clase;
		this.parametros=new ArrayList<EParametro>();
		this.varslocales=new LinkedList<EParametro>();
		this.tokenNombre=t;
		this.texto=texto;
		this.bloque=null;
		this.consolidado=false;
		this.esConstructor=true;
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
	
	@Override
	public String toString(){
		String s="\n";
		s+="______________________________________\n";
		s+="\t| Constructor |\n";
		//s+="Aridad:\t\t\t"+this.getAridad()+"\n";
		s+="Parametros:\t\t"+this.parametros.toString()+"\n";
		//s+="Descripcion:\n"+this.texto+"\n";
		if (bloque!=null) s+="\n********************************\n"+this.bloque.toString();
		s+="********************************\n";
		s+="______________________________________\n";
		return s;
	}

	@Override
	public Tipo getTipoRetorno() {
		TipoClase tc=new TipoClase(this.clase.getToken(),this.clase);
		return tc;
	}
	
	public String nuevaEtiqueta(){
		String s=this.clase.getNombre();
		s+="const"+this.getAridad();
		return s; 
	}


	@Override
	public boolean esEstatico() {
		return false;
	}
}
