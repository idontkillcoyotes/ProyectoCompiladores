
public class NodoVarEncad extends Encadenado{
	
	private Token id;
	private EParametro var;

	public NodoVarEncad(Token id) {
		this.id = id;
		this.var=null;
		this.enthis=false;
	}
		
	public Token getId() {
		return id;
	}
	
	@Override
	public Token getToken(Token t) {
		if(this.encadenado!=null)
			return this.encadenado.getToken(id);		
		else
			return id;	
	}	

	@Override
	public Tipo check(Tipo t,Token tid) throws SemanticException {
		//TODO ver que pasa cuando el metodo es estático
		if (t.esTipo(Utl.TT_IDCLASE)){
			//si es tipo clase casteo
			//TODO dejar de usar el casteo?
			TipoClase tc=(TipoClase)t;
			if (tc.estaDefinido()){
				//si la clase esta definida
				//chequeo que tenga un atributo publico con nombre id
				EParametro var;
				if(!enthis){
					//si no estoy en un this, pido atributo publico
					var=tc.getClase().getAtributoPublico(id.getLexema());
				}
				else{
					//si estoy en un this, pido atributo visible
					var=tc.getClase().getAtributoVisible(id.getLexema());
				}
				if (var==null){
					//si no tiene error
					throw new SemanticException(id.getNroLinea(),id.getNroColumna(),
							"Variable desconocida.\nLa variable de instancia '"+id.getLexema()+"' no existe o no es visible "
									+ "desde este bloque.");
				}
				//aca ya tengo el atributo publico, obtengo su tipo
				//guardo la variable
				this.var=var;
				Tipo tvar=var.getTipo();
				if(this.encadenado!=null){
					//si hay encadenado retorno lo que retorne el encadenado
					return this.encadenado.check(tvar,id);
				}
				else{
					//sino retorno el tipo del atributo de la clase
					return tvar;
				}			
			}
			else{
				throw new SemanticException(tc.getToken().getNroLinea(),tc.getToken().getNroColumna(),
						"La clase "+tc.getClase().getNombre()+" no está definida.");
			}
		}
		else{
			throw new SemanticException(t.getToken().getNroLinea(),t.getToken().getNroColumna(),
					"Acceso invalido.\nNo se puede acceder a los miembros de un tipo"
							+ " que no es tipo clase.");
		}

	}
	@Override
	public String toString(){
		String s="."+id.getLexema();
		if(encadenado!=null){
			s+=encadenado.toString();
		}
		return s;
	}

}
