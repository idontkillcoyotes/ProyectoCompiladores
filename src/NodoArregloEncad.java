
public class NodoArregloEncad extends Encadenado{
	
	private NodoExpresion expresion;	
	
	public NodoArregloEncad() {
		this.expresion = null;
		this.enthis=false;
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

	@Override
	public void generar() {
		//genero codigo de expresion
		this.expresion.generar();
		Utl.gen("add\t\t\t;sumo el offset del arreglo (de la expresion) para acceder (nodoarregloencad)");
		if ((this.ladoIzquierdo)&&(this.encadenado!=null)){
			//si estoy en lado izquierdo y no hay encadenado ent asigno
			Utl.gen("swap\t\t\t;(nodoarregloencad)");
			Utl.gen("storeref 4\t\t\t;guardo en el offset (nodoarregloencad)"); //TODO cual seria el offset?			
		}
		else{
			//sino cargo ref
			Utl.gen("loadref 0\t\t\t;(nodoarregloencad)");
		}
		if (this.encadenado!=null) this.encadenado.generar();
		
	}

}
