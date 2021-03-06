
public class TipoNull extends TipoReferencia{
	
	public TipoNull(Token tk){
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
		//null es compatible con los tipo clase, strings, o arreglos
		if((t.esTipo(Utl.TT_IDCLASE))||(t.esTipo(Utl.TPC_STRING))||(t.isArreglo()))
			return true;
		else
			return false;
	}
	@Override
	public Tipo clonarTipoBase() {
		return null;
	}
}
