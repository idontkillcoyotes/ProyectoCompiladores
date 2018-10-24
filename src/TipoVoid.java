
public class TipoVoid extends Tipo{
	
	public TipoVoid(Token t){
		this.tokenNombre=t;
	}
	
	@Override
	public boolean mismoTipo(Tipo t) {
		if(t.esTipo(this.tokenNombre.getTipo())){
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	public boolean isArreglo() {
		return false;
	}
	
	@Override
	public boolean esTipo(int tipo) {
		return (this.tokenNombre.getTipo()==tipo);
	}

	@Override
	public void setArreglo() {}

	@Override
	public boolean estaDefinido() {
		return true;
	}

	@Override
	public boolean esCompatible(Tipo t){
		return false;
	}
}
