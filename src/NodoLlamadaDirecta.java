import java.util.ArrayList;

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
	public Tipo check() {
		return null;
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
