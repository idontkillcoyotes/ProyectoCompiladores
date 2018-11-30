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
		this.contifs=0;
		this.contwhiles=0;
		this.contvarlocales=0;
		this.generado=false;
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
		s+="Cant var locales:\t"+this.contvarlocales+"\n";
		//s+="Cant ifs:\t\t"+this.contifs+"\n";
		//s+="Cant whiles:\t\t"+this.contwhiles+"\n";
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
	
	@Override
	public String getLabel(){
		String s=this.clase.getNombre();
		s+="const"+this.getAridad();
		return s; 
	}


	@Override
	public boolean esEstatico() {
		return false;
	}


	@Override
	public void generar() {
		String s=this.clase.getNombre()+this.getAridad();
		if(!generado){			
			Utl.gen(this.getLabel()+":"); //genero etiqueta			
			Utl.gen("loadfp\t\t\t;inicio de ra (econst "+s+")");
			Utl.gen("loadsp\t\t\t; (econst "+s+")");
			Utl.gen("storefp\t\t\t;fin de ra (econst "+s+")");
			Utl.gen("rmem "+this.contvarlocales+"\t\t\t;reservo espacio para var locales (econst "+s+")");
			
			Utl.gen(";comienzo a inicializar atributos (econst "+s+")");
			for(EAtributo e: this.clase.getAtributos()){
				if(e.getValor()!=null){
					//si tiene un valor:
					e.getValor().generar();
					//luego de generar valor queda en el tope de la pila
					Utl.gen("load 3\t\t\t;cargo this (econst "+s+")");
					Utl.gen("swap\t\t\t;(econst "+s+")");
					Utl.gen("storeref "+e.getOffset()+"\t\t\t;guardo en cir con offset (econst "+s+")");
				}
			}
			Utl.gen(";finalizo inicializacion de atributos (econst "+s+")");
			if (this.bloque!=null) this.bloque.generar();
			Utl.gen("fmem "+this.contvarlocales+"\t\t\t;libero espacio para var locales (econst "+s+")");
			Utl.gen("storefp\t\t\t;retorno (econst "+s+")");
			Utl.gen("ret "+(this.parametros.size()+1)+"\t\t\t;retorno con cant parametros + 1 (econst "+s+")\n");
			this.generado=true;
		}
	}
}
