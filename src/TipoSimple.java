
public class TipoSimple extends Tipo{

	public TipoSimple(Token tn){
		this.tokenNombre=tn;
		this.arreglo=false;
	}
	
	@Override
	public boolean compatible(Tipo t) {
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

}
