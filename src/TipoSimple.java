
public class TipoSimple extends Tipo{

	public TipoSimple(String t){
		this.nombre=t;
		this.arreglo=false;
	}
	
	public boolean compatible(Tipo t) {
		return true;
	}

}
