import java.util.ArrayList;
import java.util.Iterator;

public class NodoLlamadaEncad extends Encadenado{
	
	private Token id;
	private EMetodo met;
	private ArrayList<NodoExpresion> argsactuales;
	
	public NodoLlamadaEncad(Token id) {
		this.id = id;
		this.met = null;
		this.argsactuales = new ArrayList<NodoExpresion>();
		this.enthis=false;
	}
	
	public Token getId() {
		return id;
	}

	public EMetodo getMetodo() {
		return met;
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
	public Tipo check(Tipo t,Token tid) throws SemanticException {
		//primero controlo que el tipo t sea de tipo clase
		if(t.esTipo(Utl.TT_IDCLASE)){
			//casteo a tipoclase
			TipoClase tc=(TipoClase)t;
			int cantargs = this.argsactuales.size();
			EMetodo met=tc.getClase().getMetodoAridad(id.getLexema(),cantargs);			
			if(met!=null){			
					//chequeo arguementos
					Tipo retorno=checkArgumentos(met);
					//guardo metodo
					this.met=met;
					if(this.encadenado!=null){
						return this.encadenado.check(retorno,this.id);
					}
					else{
						//si no hay encadenado y estoy del lado izquierdo hay error
						if(!ladoIzquierdo){
							return retorno;
						}
						else{
							throw new SemanticException(id.getNroLinea(),id.getNroColumna(),
									"Asignacion invalida.\nEl lado izquierdo de una asignacion solo puede "
									+ "ser un acceso a una variable.");
						}
					}				
			}
			else
				throw new SemanticException(id.getNroLinea(),id.getNroColumna(),"Metodo inexistente.");			
		}
		else			
			throw new SemanticException(tid.getNroLinea(),tid.getNroColumna(),"Tipo invalido.\n"
					+ "El tipo resultado de la expresion no es un tipo clase, no es posible acceder a sus miembros.");
	}
	
	@Override
	public String toString(){
		String s="."+id.getLexema()+"(";
		int total=this.argsactuales.size();
		int i=1;
		Iterator<NodoExpresion> itargs=this.argsactuales.iterator();
		while(i<total){
			s+=itargs.next().toString()+", ";
			i++;
		}
		if (total>0) s+=itargs.next().toString();
		s+=")";
		if(encadenado!=null){
			s+=encadenado.toString();
		}
		return s;
	}

	@Override
	public Token getToken(Token t) {
		if(this.encadenado!=null)
			return this.encadenado.getToken(id);		
		else
			return id;	
	}

	@Override
	public void generar() {
		if(met.tieneRetorno()) Utl.gen("rmem 1\t\t\t;reservo espacio para retorno (nodollamencad)");		
		for (NodoExpresion par: this.argsactuales){
			par.generar();
			Utl.gen("swap\t\t\t;(nodollamencad)");			
		}
		Utl.gen("dup\t\t\t;duplico this (nodollamencad)");
		Utl.gen("loadref 0\t\t\t;cargo vt (nodollamencad)");
		Utl.gen("loadref "+met.getOffset()+"\t\t\t;cargo dir de metodo con el offset (nodollamencad)");
		Utl.gen("call\t\t\t;llamo (nodollamencad)");
		if(this.encadenado!=null) this.encadenado.generar();
	}

}
