
public class TipoClase extends Tipo{
	
	public TipoClase(Token tk){
		this.tokenNombre=tk;
		this.arreglo=false;
	}	
	
	public boolean compatible(Tipo t) {
		return true;
	}

	public boolean estaDefinido() {
		return Utl.ts.estaDefinida(this.tokenNombre.getLexema());
	}

}
