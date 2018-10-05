
public class TipoClase extends Tipo{
	
	public TipoClase(Token tk){
		this.tokenNombre=tk;
		this.arreglo=false;
	}

	public boolean estaDefinido() {
		return Utl.ts.estaDefinida(this.tokenNombre.getLexema());
	}

	@Override
	public boolean compatible(Tipo t) {
		if(this.arreglo==t.isArreglo()){
			if(this.tokenNombre.getLexema().equals(t.getTipo())){
				return true;
			}
			else{
				return false;
			}
		}
		else{
			return false;
		}
	}

}
