
public class TipoString extends TipoReferencia{

	public TipoString(Token tk){
		this.tokenNombre=tk;
		this.arreglo=false;
	}

	public boolean estaDefinido() {
		return true;
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
		if(t.esTipo(Utl.TPC_STRING))
			return true;
		else
			return false;
	}
	
	@Override
	public Tipo clonarTipoBase() {
		return new TipoString(this.tokenNombre);
	}
	
}
