import java.util.ArrayList;

public class EConstructor extends EMiembro {
	
	public EConstructor(EClase clase,Token t,String texto){
		this.clase=clase;
		this.parametros=new ArrayList<EParametro>();
		this.tokenNombre=t;
		this.texto=texto;
		this.bloque=null;
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
	
	@Override
	public String toString(){
		String s="\n";
		s+="______________________________________\n";
		s+="\t| Constructor |\n";
		s+="Aridad:\t\t\t"+this.getAridad()+"\n";
		s+="Parametros:\n"+this.parametros.toString()+"\n";
		s+="Descripcion:\n"+this.texto+"\n";
		if (bloque!=null) s+="Bloque:\n********************************\n"+this.bloque.toString();
		s+="********************************\n";
		s+="______________________________________\n";
		return s;
	}	

	@Override
	public void check() throws SemanticException {
		// TODO Auto-generated method stub		
	}
}
