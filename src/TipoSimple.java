
public class TipoSimple extends Tipo{

	public TipoSimple(Token tn){
		this.tokenNombre=tn;
		this.arreglo=false;
	}
	
	public boolean compatible(Tipo t) {
		return true;
	}

	public boolean estaDefinido() {
		return true;
	}

}
