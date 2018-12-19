import java.util.ArrayList;
import java.util.Iterator;

public class NodoLlamadaEstatica extends NodoPrimario{
	
	private Token idmet;
	private Token idclase;
	private EClase clase;
	private EMetodo met;
	private ArrayList<NodoExpresion> argsactuales;
	
	
	public NodoLlamadaEstatica(Token idclase,Token idmet) {
		super();
		this.idclase = idclase;
		this.idmet = idmet;
		this.clase = null;
		this.met = null;
		this.argsactuales = new ArrayList<NodoExpresion>();
	}

	public EMetodo getMet() {
		return met;
	}


	public Token getIdMet() {
		return idmet;
	}
	
	public Token getToken(){
		return idmet;
	}
	
	@Override
	public void setValorAtributo(boolean b) {
		this.valorAtributo=b;
		if (this.encadenado!=null) this.encadenado.setValorAtributo(b);
	}

	public Token getIdClase() {
		return idclase;
	}

	public EClase getClase() {
		return clase;
	}

	public ArrayList<NodoExpresion> getArgsactuales() {
		return argsactuales;
	}


	@Override
	public Tipo check() throws SemanticException {
		//primero controlo que la clase este definida y la guardo
		this.clase=Utl.ts.getClase(this.idclase.getLexema());
		if(this.clase!=null){
			//obtengo el metodo estatico
			EMetodo met=clase.getMetodoEstatico(idmet.getLexema());
			if(met!=null){
				int npars=met.getParametros().size();
				int nargs=this.argsactuales.size();
				if(npars==nargs){
					//comienzo a chequear tipo por tipo
					Iterator<NodoExpresion> itargs=this.argsactuales.iterator();
					Iterator<EParametro> itpars=met.getParametros().iterator();
					Tipo targ,tpar;
					NodoExpresion ne;
					while(itargs.hasNext()){
						ne=itargs.next();
						targ=ne.check();
						tpar=itpars.next().getTipo();
						if(!targ.esCompatible(tpar)){
							throw new SemanticException(ne.getToken().getNroLinea(),ne.getToken().getNroColumna(),
									"Tipo de parametro invalido.");
						}
					}
					Tipo retorno=met.getTipoRetorno();
					//aca ya chequee que es una llamada correcta
					//guardo el metodo
					this.met=met;
					if(this.encadenado!=null){
						return this.encadenado.check(retorno,idmet);
					}
					else{
						return retorno;
					}
				}
				else
					throw new SemanticException(idmet.getNroLinea(),idmet.getNroColumna(),"Numero de parametros invalido.");				
			}
			else
				throw new SemanticException(idmet.getNroLinea(),idmet.getNroColumna(),"Metodo inexistente.\n"
						+ "El metodo "+idmet.getLexema()+" no esta definido o no es un metodo estatico.");			
		}
		else
			//clase no definida
			throw new SemanticException(idclase.getNroLinea(),idclase.getNroColumna(),"Clase no definida.\n"
					+ "La clase "+idclase.getLexema()+" no está definida.");
	}
	
	@Override
	public String toString(){
		String s=idclase.getLexema()+"."+idmet.getLexema()+"(";
		int total=this.argsactuales.size();
		int i=1;
		Iterator<NodoExpresion> itargs=this.argsactuales.iterator();		
		while(i<total){
			s+=itargs.next().toString()+", ";
			i++;
		}
		if(total>0) s+=itargs.next().toString();
		s+=")";
		if(encadenado!=null){
			s+=encadenado.toString();
		}
		return s;
	}

	@Override
	public void generar() {
		if (this.met.tieneRetorno()){
			Utl.gen("rmem 1\t\t\t;guardo espacio para retorno (nodollamest)");
		}
		Utl.gen("push 9999\t\t\t;apilo el this ficticio (nodollamest)");
		for(NodoExpresion arg:this.argsactuales){
			arg.generar(); //genero cod de argumentos
			Utl.gen("swap\t\t\t;swapeo this con argumento (nodollamest)");
		}		
		Utl.gen("push "+this.met.getLabel()+"\t\t\t;apilo direccion (nodollamest)");
		Utl.gen("call\t\t\t;hago llamada (nodollamest)");
		if (encadenado!=null) encadenado.generar();
	}

}
