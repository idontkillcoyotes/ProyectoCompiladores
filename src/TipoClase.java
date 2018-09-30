
public class TipoClase extends Tipo{

	private Token tkTipo;
	
	public TipoClase(Token tk){
		this.tkTipo=tk;
		this.nombre=tk.getLexema();
		this.arreglo=false;
	}
	
	public Token getToken(){
		return this.tkTipo;
	}
	
	public boolean compatible(Tipo t) {
		return true;
	}

}
