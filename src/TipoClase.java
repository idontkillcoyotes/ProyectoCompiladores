
public class TipoClase extends TipoReferencia{
	
	public TipoClase(Token tk){
		this.tokenNombre=tk;
		this.arreglo=false;
	}

	public boolean estaDefinido() {
		return Utl.ts.estaDefinida(this.tokenNombre.getLexema());
	}

	@Override
	public boolean mismoTipo(Tipo t) {
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

	@Override
	public boolean esCompatible(Tipo t) {
		// TODO Auto-generated method stub
		return false;
	}
}
