import java.util.ArrayList;

public class NodoDeclaracionVars extends NodoSentencia{
	
	private ArrayList<EParametro> varlocales;
	private NodoExpresion valor;
	private Tipo tipo;
	private Token igual;
	
	public NodoDeclaracionVars(ArrayList<EParametro> varlocales) {
		this.varlocales = varlocales;
		this.valor=null;
		this.tipo=null;
		this.igual=null;
	}	

	public NodoExpresion getValor() {
		return valor;
	}
	public void setValor(NodoExpresion valor) {
		this.valor = valor;
	}
	
	public Token getTokenIgual() {
		return igual;
	}

	public void setTokenIgual(Token igual) {
		this.igual = igual;
	}

	public Tipo getTipo(){
		return this.tipo;
	}

	public ArrayList<EParametro> getVarlocales() {
		return varlocales;
	}

	@Override
	public void check() throws SemanticException{
		//seteo el tipo con la primer variable 
		//como minimo siempre hay una variable declarada
		this.tipo=this.varlocales.get(0).getTipo();
		
		//si tiene expresion para inicializar el valor
		if(valor!=null){
			//chequeo expresion y obtengo su tipo
			Tipo exp=valor.check();
			//si los tipos son incompatibles hay error
			if(!exp.esCompatible(tipo))
				throw new SemanticException(igual.getNroLinea(),igual.getNroColumna(),"Tipos incompatibles");
		}		
		for(EParametro var: varlocales){
			if (!Utl.ts.getBloqueAct().addVarLocal(var))
				throw new SemanticException(var.getToken().getNroLinea(),var.getToken().getNroColumna(),
						"Nombre de variable duplicado.\nEl nombre de la variable ya está en uso.");
		}
		
	}
	
	@Override
	public String toString(){
		String s="";
		for(EParametro var: this.varlocales){
			s+=var.getTipo().getTipo()+" "+var.getNombre();
			s+=",";
		}
		if(this.valor!=null){
			s+="= "+this.valor.toString();
		}
		s+="\n";
		return s;
	}
	

}
