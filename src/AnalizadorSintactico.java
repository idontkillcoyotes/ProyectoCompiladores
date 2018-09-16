
public class AnalizadorSintactico {
	
	private AnalizadorLexico alex;
	private Token tokenAct;
	
	public AnalizadorSintactico (AnalizadorLexico al){
		this.alex=al;
		try {
			this.tokenAct = alex.nextToken();
		} catch (LexicoException e) {
			System.out.println(e.getMessage());
		}		
	}
	
	private void match(int tipoToken){
		if(tokenAct.esTipo(tipoToken))
			try {
				tokenAct = alex.nextToken();
			} catch (LexicoException e) {
				System.out.println(e.getMessage());
			}
		else{
			//TODO: error
		}
	}
	
	public void start(){
		inicio();
	}
	
	private void inicio(){
		clase();
		clases();
		match(Utils.TT_FINARCHIVO);		
	}
	
	private void clases(){
		if (tokenAct.esTipo(Utils.TPC_CLASS)){
			clase();
			clases();
		}
		else if (tokenAct.esTipo(Utils.TT_FINARCHIVO)){
			//vacio
		}
		else{
			//TODO: error
		}		
	}
	
	private void clase(){
		match(Utils.TPC_CLASS);
		match(Utils.TT_IDCLASE);
		herencia();
		match(Utils.TT_PUNLLAVE_A);
		miembros();
		match(Utils.TT_PUNLLAVE_C);		
	}
	
	private void herencia(){
		if (tokenAct.esTipo(Utils.TPC_EXTENDS)){
			match(Utils.TPC_EXTENDS);
			match(Utils.TT_IDCLASE);
		}
		else if (tokenAct.esTipo(Utils.TT_PUNLLAVE_A)){
			//vacio
		}else{
			//TODO: error
		}			
	}
	private void miembros(){
		if ( tokenAct.esTipo(new int[]{Utils.TPC_PUBLIC,Utils.TPC_PRIVATE,Utils.TPC_STATIC,Utils.TPC_DYNAMIC,Utils.TT_IDCLASE}) ){
			miembro();
			miembros();
		}
		else if (tokenAct.esTipo(Utils.TT_PUNLLAVE_C)){
			//vacio
		}else{
			//TODO: error
		}			
	}
	private void miembro(){
		if ( tokenAct.esTipo(new int[]{Utils.TPC_PUBLIC,Utils.TPC_PRIVATE}) ){
			atributo();
		}
		else if (tokenAct.esTipo(Utils.TT_IDCLASE)){
			ctor();
		}
		else if (tokenAct.esTipo(new int[]{Utils.TPC_STATIC,Utils.TPC_DYNAMIC})){
			metodo();
		}
		else{
			//TODO: error
		}
	}
	private void atributo(){
		visibilidad();
		tipo();
		listaDecVars();
	}
	private void listaDecVars() {
		//idMetVar ListaDV
		match(Utils.TT_IDMETVAR);
		listaDV();
	}
	private void listaDV() {
		// TODO Auto-generated method stub
		
	}
	private void tipo() {
		// TipoPrimitivo | TipoReferencia
	}
	private void visibilidad() {
		if (tokenAct.esTipo(Utils.TPC_PUBLIC)){
			match(Utils.TPC_PUBLIC);
		}
		else if (tokenAct.esTipo(Utils.TPC_PRIVATE)){
			match(Utils.TPC_PRIVATE);
		}
		else{
			//TODO error
		}			
	}
	private void metodo(){
		formaMetodo();
		tipoMetodo();
		match(Utils.TT_IDMETVAR);
		argsFormales();
		bloque();
	}
	private void ctor(){
		match(Utils.TT_IDCLASE);
		argsFormales();
		bloque();
	}
 
	private void argsFormales() {
		//TODO 
	}
	private void listaArg() {
		//TODO 
	}
	private void argFormales() {
		//TODO 
	} 
	private void arg() {
		//TODO 
	}
	private void formaMetodo() {
		//TODO 
	}
	private void tipoMetodo() {
		//TODO 
	}
	private void posibleArreglo() {
		//TODO 
	}
	private void tipoPrimitivo() {
		//TODO 
	}
	private void tipoReferencia() {
		//TODO 
	}
	private void bloque() {
		//TODO 
	}
	private void sentencias() {
		//TODO 
	}
	private void sentencia() {
		//TODO 
	}
	private void sentenciaElse() {
		//TODO 
	}
	private void expresiones() {
		//TODO 
	} 
	private void asignacion() {
		//TODO 
	} 
	private void sentenciaLlamada() {
		//TODO 
	}
	private void expresion() {
		//TODO 
	}
	private void expOr() {
		//TODO 
	}
	private void expOrR() {
		//TODO 
	}
	private void expAnd() {
		//TODO 
	}
	private void expAndR() {
		//TODO 
	}
	private void expIg() {
		//TODO 
	} 
	private void expIgR() {
		//TODO 
	}
	private void expComp() {
		//TODO 
	}
	private void expCompR() {
		//TODO 
	}
	private void expAd() {
		//TODO 
	} 
	private void expAdR() {
		//TODO 
	}
	private void expMul() {
		//TODO 
	}
	private void expMulR() {
		//TODO 
	}
	private void expUn() {
		//TODO 
	} 
	private void opIgual() {
		//TODO 
	}
	private void opComp() {
		//TODO 
	}
	private void opAd() {
		//TODO 
	} 
	private void opUn() {
		//TODO 
	} 
	private void opMul() {
		//TODO 
	} 
	private void operando() {
		//TODO 
	}
	private void literal() {
		//TODO 
	} 
	private void primario() {
		//TODO 
	}
	private void metodoVariable() {
		//TODO 
	}
	private void expresionParentizada() {
		//TODO 
	}
	private void encadenado() {
		//TODO 
	}
	private void acceso() {
		//TODO 
	}
	private void accesoThis() {
		//TODO 
	}
	private void accesoVar() {
		//TODO 
	}
	private void llamadaMetodo() {
		//TODO 
	}
	private void llamadaMetodoEstatico() {
		//TODO 
	}
	private void llamadaCtor() {
		//TODO 
	}
	private void llamadaCtorR() {
		//TODO 
	}
	private void argsActuales() {
		//TODO 
	}
	private void listaExpresiones() {
		//TODO 
	}
	private void listaExp() {
		//TODO 
	}
	private void llamadaMetodoEncadenado() {
		//TODO 
	}
	private void accesoVarEncadenado() {
		//TODO 
	}
	private void accesoArregloEncadenado() {
		//TODO 
	}	
}
