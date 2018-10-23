import java.util.ArrayList;

public class NodoLlamadaEncad extends Encadenado{
	
	private Token id;
	private ArrayList<NodoExpresion> argsactuales;
	
	public NodoLlamadaEncad(Token id) {
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
	public Tipo check(Tipo t) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String toString(){
		String s="."+id.getLexema()+"(";
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
