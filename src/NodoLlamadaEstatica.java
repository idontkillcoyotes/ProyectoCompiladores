import java.util.ArrayList;

public class NodoLlamadaEstatica extends NodoPrimario{
	
	private Token idmet;
	private Token idclase;
	private EClase clase;
	private ArrayList<NodoExpresion> argsactuales;
	
	
	public NodoLlamadaEstatica(Token idclase,Token idmet) {
		super();
		this.idclase = idclase;
		this.idmet = idmet;
		this.clase = null;
		this.argsactuales = new ArrayList<NodoExpresion>();
	}


	public Token getIdMet() {
		return idmet;
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
	public Tipo check() {
		return null;
	}
	
	@Override
	public String toString(){
		String s=idclase.getLexema()+"."+idmet.getLexema()+"(";
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
