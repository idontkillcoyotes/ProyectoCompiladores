
public class TipoClase extends TipoReferencia{
	
	public EClase clase;
	
	public TipoClase(Token tk){
		this.tokenNombre=tk;
		this.arreglo=false;
		this.clase=null;
	}
	
	public TipoClase(Token t,EClase c){
		this.tokenNombre=t;
		this.clase=c;
		this.arreglo=false;
	}
	
	public boolean estaDefinido() {
		this.clase=Utl.ts.getClase(tokenNombre.getLexema());
		if(this.clase!=null){
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

	public boolean esCompatible(Tipo t) {		
		if(t.esTipo(Utl.TT_IDCLASE)){
			TipoClase tc=(TipoClase)t;
			//System.out.println("t izq: "+tc.getClase().getNombre());
			//System.out.println("this der: "+this.clase.getNombre());
			if(this.clase.esSubtipo(tc.getClase())){
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
	public Tipo clonarTipoBase() {
		TipoClase t=new TipoClase(this.tokenNombre,this.clase);
		return t;
	}
}

