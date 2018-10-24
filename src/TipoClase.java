
public class TipoClase extends TipoReferencia{
	
	public EClase clase;
	
	public TipoClase(Token tk){
		this.tokenNombre=tk;
		this.clase=null;
		this.arreglo=false;
	}

	public boolean estaDefinido() {
		if(Utl.ts.estaDefinida(this.tokenNombre.getLexema())){
			this.clase=Utl.ts.getClase(tokenNombre.getLexema());
			return true;
		}
		else{
			return false;
		}
	}
	
	public EClase getClase(){
		return this.clase;
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
