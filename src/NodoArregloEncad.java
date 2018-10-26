
public class NodoArregloEncad extends Encadenado{
	
	private NodoExpresion expresion;	
	
	public NodoArregloEncad() {
		this.expresion = null;
	}
	
	@Override
	public Token getToken(Token t) {
		if(this.encadenado!=null)
			return this.encadenado.getToken(t);		
		else
			return t;	
	}	
	
	@Override
	public void setValorAtributo(boolean b) {
		this.valorAtributo=b;
		this.expresion.setValorAtributo(b);
		if (this.encadenado!=null) this.encadenado.setValorAtributo(b);
	}
	
	public NodoExpresion getExpresion() {
		return expresion;
	}
	public void setExpresion(NodoExpresion expresion) {
		this.expresion = expresion;
	}

	@Override
	public Tipo check(Tipo t, Token id) throws SemanticException {
		//chequeo que el tipo recibido sea arreglo
		if(t.isArreglo()){
			Tipo exp=this.expresion.check();
			if(exp.esTipo(Utl.TPC_INT)){
				//chequeo que el tipo de la expresion sea entero
				if(this.encadenado!=null){
					return this.encadenado.check(t,id);					
				}
				else{
					//TODO ver si esta mal que cambie el tipo pasado como parametro. MUY MALLLL!!!
					Tipo tret=t.clonarTipoBase();					
					return tret;
				}
			}
			else
				throw new SemanticException(expresion.getToken().getNroLinea(),expresion.getToken().getNroColumna(),
						"Tipo de expresion invalido.\nLa expresion debe ser de tipo entero.");
		}
		else
			throw new SemanticException(id.getNroLinea(),id.getNroColumna(),
					"Tipo invalido.\nEl tipo de la expresion no es un arreglo.");
		
	}
	@Override
	public String toString(){
		String s=".[";
		s+=expresion.toString()+"]";
		if(encadenado!=null){
			s+=encadenado.toString();
		}
		return s;
	}

}
