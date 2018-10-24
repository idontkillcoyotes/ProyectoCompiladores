
public class TipoBool extends TipoGenerico{

	public TipoBool(Token tn){
		this.tokenNombre=tn;
		this.arreglo=false;
	}
	
	@Override
	public boolean mismoTipo(Tipo t) {
		if(arreglo==t.isArreglo()){
			if(t.esTipo(this.tokenNombre.getTipo())){
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
	public boolean estaDefinido() {
		return true;
	}

	@Override
	public boolean esCompatible(Tipo t) {
		if(t.esTipo(Utl.TPC_BOOLEAN))
			return true;
		else
			return false;
	}

}
