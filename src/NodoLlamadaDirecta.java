import java.util.ArrayList;
import java.util.Iterator;

public class NodoLlamadaDirecta extends NodoPrimario{
	
	private Token id;
	private ArrayList<NodoExpresion> argsactuales;
	
	public NodoLlamadaDirecta(Token id) {
		this.id = id;
		this.argsactuales = new ArrayList<NodoExpresion>();
	}
	
	public Token getToken() {
		return id;
	}
	
	@Override
	public void setValorAtributo(boolean b) {
		this.valorAtributo=b;
		if (this.encadenado!=null) this.encadenado.setValorAtributo(b);
	}

	public ArrayList<NodoExpresion> getArgsactuales() {
		return argsactuales;
	}
	
	private Tipo checkArgumentos(EMetodo met) throws SemanticException{
		int npars=met.getParametros().size();
		int nargs=this.argsactuales.size();
		if(npars==nargs){
			//comienzo a chequear tipo por tipo
			Iterator<NodoExpresion> itargs=this.argsactuales.iterator();
			Iterator<EParametro> itpars=met.getParametros().iterator();
			while(itargs.hasNext()){
				Tipo targ=itargs.next().check();
				Tipo tpar=itpars.next().getTipo();
				if(!targ.esCompatible(tpar)){
					throw new SemanticException(id.getNroLinea(),id.getNroColumna(),"Tipo de parametro invalido.");
				}
			}
			Tipo tret=met.getTipoRetorno();
			//aca ya chequee que es una llamada correcta
			return tret;
		}
		else{
			throw new SemanticException(id.getNroLinea(),id.getNroColumna(),"Numero de parametros invalido.");
		}
	}

	@Override
	public Tipo check() throws SemanticException{
		//controlo que el metodo sea visible de la clase actual
		EMetodo met=Utl.ts.getClaseAct().getMetodo(id.getLexema());
		if(met!=null){
			boolean staticmet=met.esEstatico();			
			if(!valorAtributo){
				boolean staticmiembro=Utl.ts.getMiembroAct().esEstatico();
				//si no estoy en una inicializacion de atributo 
				if( (staticmet) || (!staticmiembro) ){
					//si el miembro no es estatico O si el metodo es estatico
					Tipo retorno=checkArgumentos(met);
					if(this.encadenado!=null){
						return this.encadenado.check(retorno,id);
					}
					else{
						return retorno;
					}
				}
				else{
					throw new SemanticException(id.getNroLinea(),id.getNroColumna(),"Acceso invalido.\n"
							+"No es posible acceder a un metodo dinamico dentro de un metodo estatico.");
				}
			}
			else{
				//estoy en la inicializacion de un atributo, controlo que el metodo sea estatico
				if(staticmet){
					//si el metodo es estatico no hay problemas
					Tipo retorno=checkArgumentos(met);
					if(this.encadenado!=null){
						return this.encadenado.check(retorno,id);
					}
					else{
						return retorno;
					}
				}
				else{
					throw new SemanticException(id.getNroLinea(),id.getNroColumna(),
							"Acceso invalido.\nNo es posible acceder a un metodo dinamico en la inicializacion"
							+ " de un atributo de instancia.");
				}
			}
		}
		else{
			throw new SemanticException(id.getNroLinea(),id.getNroColumna(),"Metodo inexistente.\n"
					+ "El metodo "+this.id.getLexema()+" no esta definido.");
		}
	}


	@Override
	public String toString(){
		String s=id.getLexema()+"(";
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

}
