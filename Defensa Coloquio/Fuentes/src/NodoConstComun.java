import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class NodoConstComun extends NodoConst {
	
	private EClase clase;
	private EConstructor con;
	private ArrayList<NodoExpresion> argsactuales;	
	
	public NodoConstComun(Token t) {
		this.tokenNombre=t;
		this.clase=null;
		this.con=null;
		this.argsactuales=new ArrayList<NodoExpresion>();
	}

	public ArrayList<NodoExpresion> getArgsactuales() {
		return argsactuales;
	}
	

	@Override
	public void setValorAtributo(boolean b) {
		this.valorAtributo=b;
		if (this.encadenado!=null) this.encadenado.setValorAtributo(b);
	}

	@Override
	public Tipo check() throws SemanticException {
		//TODO simplificar este codigo, dos constructores no pueden tener la misma aridad
		
		//deberia chequear que existe la clase, y que su constructor 
		//es llamado correctamente
		this.clase=Utl.ts.getClase(this.tokenNombre.getLexema());
		if(this.clase!=null){
			//clase esta definida			
			//si hay argumentos entonces se usa el constructor por defecto o
			//un constructor definido, pero siempre hay uno. NO SIEMPRE!!!
			int args=this.argsactuales.size();			
			if(args>0){
				//si el llamado al constructor tiene mas de un argumento, debo chequear que este definido

				//primero chequeo todos los argumentos y armo lista de tipos
				Tipo targ,tpar;
				ArrayList<Tipo> ltipos=new ArrayList<Tipo>();
				for(NodoExpresion exp: argsactuales){
					targ=exp.check();
					ltipos.add(targ);
				}
				targ=null;				
				boolean hayconst=false;
				boolean incompatibles=false;
				EConstructor cactual;
				int i=0;
				//obtengo iterador de constructores de aridad ARGS
				LinkedList<EConstructor> lconst=this.clase.getConstAridad(args);

				Iterator<EConstructor> itconst=lconst.iterator();
				while((!hayconst)&&(itconst.hasNext())){
					//mientras haya constructores y no haya encontrado uno

					//obtengo el primer constructor
					cactual=itconst.next();

					//obtengo iterador de tipos
					Iterator<Tipo> ittipos=ltipos.iterator();
					//obtengo iterador de parametros de constructor
					Iterator<EParametro> itpars=cactual.getParametros().iterator();

					while((!incompatibles)&&(i<args)){
						i++;

						//mientras haya parametros, y no sean incompatibles
						//obtengo el primer tipo de arg
						targ=ittipos.next();
						//obtengo el primer tipo de par
						tpar=itpars.next().getTipo();						

						if(!targ.esCompatible(tpar)){
							//si no son compatibles							
							incompatibles=true;
							//corta el while interior
						}
					}
					if((i==args)&&(!incompatibles)){
						//si el while anterior llego al final entonces
						//hay un constructor valido
						hayconst=true;
						//seteo constructor
						this.con=cactual;

					}
				}
				//fuera de while exterior
				if(!hayconst){
					//llamada invalida
					throw new SemanticException(tokenNombre.getNroLinea(),tokenNombre.getNroColumna(),
							"Parametros invalidos.\nNo existe un constructor con esa cantidad y "
									+ "tipo de parametros.");
				}
				else{
					//llamada valida
					if(this.encadenado!=null){
						return this.encadenado.check(this.con.getTipoRetorno(), tokenNombre);
					}
					else{
						return this.con.getTipoRetorno();
					}					
				}
			}
			else{
				//si el constructor tiene 0 parametros entonces siempre es valido? NO!!
				this.con=this.clase.getConstDefault();
				if(con!=null){
					if(this.encadenado!=null){
						return this.encadenado.check(this.con.getTipoRetorno(), tokenNombre);
					}
					else{
						return this.con.getTipoRetorno();
					}
				}
				else{
					throw new SemanticException(tokenNombre.getNroLinea(),tokenNombre.getNroColumna(),
							"Parametros invalidos.\nNo existe un constructor con esa cantidad y "
									+ "tipo de parametros.");

				}
			}
		}
		else
			throw new SemanticException(tokenNombre.getNroLinea(),tokenNombre.getNroColumna(),"Clase no definida.\n"
					+ "La clase "+tokenNombre.getLexema()+" no esta definida.");

	}
	@Override
	public String toString(){
		String s="new "+tokenNombre.getLexema()+"(";
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
	public void generar() {
		//primero creo CIR
		Utl.gen("rmem 1\t\t\t;reservo espacio para retorno de malloc (nodoconst)");
		int tama�ocir=this.clase.getTama�oCIR();
		Utl.gen("push "+tama�ocir+"\t\t\t;apilo tama�o de cir (nodoconst)");
		Utl.gen("push malloc\t\t\t;cargo dir (nodoconst)");
		Utl.gen("call\t\t\t;hago llamada (nodoconst)");
		//en este punto se reservo memoria y tengo la direccion del cir en el tope de la pila		
		String labelvt=this.clase.getLabelVT();
		//aca deberia controlar que la vt existe, (que la clase tiene al menos un metodo dinamico)
		if(clase.tieneVT()){
			Utl.gen("dup\t\t\t;duplico direccion de cir (nodoconst)");
			Utl.gen("push "+labelvt+"\t\t\t;cargo direccion de vt (nodoconst)");
			Utl.gen("storeref 0\t\t\t;guardo en la dir 0 del CIR, la dir de la VT (nodoconst)");
		}
		//en este punto ya tengo el cir creado con la vt en la primera direccion
		Utl.gen("dup\t\t\t;duplico dir cir? (nodoconst)");
		
		//proceso parametros para llamar al constructor
		for(NodoExpresion e:this.argsactuales){
			e.generar();
			Utl.gen("swap\t\t\t;(nodoconst)");	
		}
		String labelctor=this.con.getLabel();
		Utl.gen("push "+labelctor+"\t\t\t;cargo dir de const (nodoconst)");
		Utl.gen("call\t\t\t;llamo const (nodoconst)");
		
		//genero encadenado si hay
		if(this.encadenado!=null) this.encadenado.generar();
	}
	
}
