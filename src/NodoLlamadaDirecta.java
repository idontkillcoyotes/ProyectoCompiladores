import java.util.ArrayList;
import java.util.Iterator;

public class NodoLlamadaDirecta extends NodoPrimario{
	
	private Token id;
	private ArrayList<NodoExpresion> argsactuales;
	
	public NodoLlamadaDirecta(Token id) {
		this.id = id;
		this.argsactuales = new ArrayList<NodoExpresion>();
	}
	
	public Token getId() {
		return id;
	}

	public ArrayList<NodoExpresion> getArgsactuales() {
		return argsactuales;
	}

	@Override
	public Tipo check() throws SemanticException{
		//controlo que el metodo sea visible de la clase actual
		EMetodo met=Utl.ts.getClaseAct().getMetodo(id.getLexema());
		if(met!=null){
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
				Tipo retorno=met.getTipoRetorno();
				//aca ya chequee que es una llamada correcta
				if(this.encadenado!=null){
					return this.encadenado.check(retorno);
				}
				else{
					return retorno;
				}
			}
			else{
				throw new SemanticException(id.getNroLinea(),id.getNroColumna(),"Numero de parametros invalido.");
			}
		}
		else{
			throw new SemanticException(id.getNroLinea(),id.getNroColumna(),"Metodo no visible.");
		}
		
	}
	@Override
	public String toString(){
		String s=id.getLexema()+"(";
		for(NodoExpresion e:argsactuales){
			s+=e.toString()+", ";
		}
		s+=")";
		if(encadenado!=null){
			s+=encadenado.toString();
		}
		return s;
	}

}
