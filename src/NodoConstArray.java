
public class NodoConstArray extends NodoConst{
	
	private NodoExpresion tamaño;
	
	public NodoConstArray(Token t) {
		this.tokenNombre=t;
	}	

	public void setTamaño(NodoExpresion tamaño) {
		this.tamaño = tamaño;
	}

	public NodoExpresion getTamaño() {
		return tamaño;
	}
	
	@Override
	public void setValorAtributo(boolean b) {
		this.valorAtributo=b;
		if (this.encadenado!=null) this.encadenado.setValorAtributo(b);
	}

	@Override
	public Tipo check() throws SemanticException {
		Tipo tam=this.tamaño.check();
		if((!tam.isArreglo())&&(tam.esTipo(Utl.TPC_INT))){
			//deberia chequear de que tipo va a ser el arreglo
			if(this.tokenNombre.esTipo(Utl.TT_IDCLASE)){
				//si es tipo clase debo chequear que la clase exista
				TipoClase t=new TipoClase(this.tokenNombre);
				if (t.estaDefinido()){					
					t.setArreglo(true);
					if(this.encadenado!=null){
						return this.encadenado.check(t, tokenNombre);
					}
					else{
						return t;
					}					
				}
				else{
					throw new SemanticException(tokenNombre.getNroLinea(),tokenNombre.getNroColumna(),
							"Clase no declarada.\nLa clase "+tokenNombre.getLexema()+" no está declarada");
				}				
			}
			else{
				//es tipo primitivo string,int,boolean,char
				Tipo t=null;
				switch(this.tokenNombre.getTipo()){
				case Utl.TPC_INT:{
					t=new TipoInt(this.tokenNombre);
					t.setArreglo(true);
					break;
				}
				case Utl.TPC_CHAR:{
					t=new TipoChar(this.tokenNombre);
					t.setArreglo(true);
					break;
				}
				case Utl.TPC_BOOLEAN:{
					t=new TipoBool(this.tokenNombre);
					t.setArreglo(true);
					break;
				}
				case Utl.TPC_STRING:{
					t=new TipoString(this.tokenNombre);
					t.setArreglo(true);
					break;
				}				
				}
				if(this.encadenado!=null){
					return this.encadenado.check(t, tokenNombre);
				}
				else{
					return t;
				}
			}		
		}
		else{
			throw new SemanticException(tamaño.getToken().getNroLinea(),tamaño.getToken().getNroColumna(),
					"Tipo invalido.\nLa expresion debe ser de tipo entero.");
		}		
	}
	@Override
	public String toString(){
		String s="new "+tokenNombre.getLexema()+"[";
		s+=tamaño.toString()+"]";
		if(encadenado!=null){
			s+=encadenado.toString();
		}
		return s;
	}

	@Override
	public void generar() {
		Utl.gen("rmem 1\t\t\t;reservo mem para puntero a arreglo (nodoconstarr)");
		this.tamaño.generar(); //genero el tamaño del arreglo
		Utl.gen("push malloc\t\t\t;cargo direccion (nodoconstarr)");
		Utl.gen("call\t\t\t;llamo a subrutina (nodoconstarr)");		
	}

}
