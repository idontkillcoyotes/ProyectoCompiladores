
public class NodoVar extends NodoAcceso{
	
	private Token id;
	
	public NodoVar(Token id) {
		this.id = id;
	}
	
	public Token getId() {
		return id;
	}

	@Override
	public Tipo check() throws SemanticException {
		//TODO ver que pasa cuando el metodo es estático
		EParametro var = Utl.ts.getBloqueAct().getVarLocal(id.getLexema());
		if(var==null){
			var = Utl.ts.getMiembroAct().getParametro(id.getLexema());
			if(var==null){
				var= Utl.ts.getClaseAct().getAtributoPublico(id.getLexema());
				if (var==null){
					throw new SemanticException(id.getNroLinea(),id.getNroColumna(),
							"Variable desconocida.\nLa variable '"+id.getLexema()+"' no ha sido declarada o no "
							+ "es visible desde este bloque.");
				}
			}
		}
		//ya tengo la var supuestamente.
		Tipo tvar=var.getTipo();
		if(this.encadenado!=null){
			return this.encadenado.check(tvar);
		}
		else{
			return tvar;
		}
	}
	@Override
	public String toString(){
		String s=id.getLexema();
		if(encadenado!=null){
			s+=encadenado.toString();
		}
		return s;
	}


}
