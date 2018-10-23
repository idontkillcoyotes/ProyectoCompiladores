import java.util.ArrayList;

public class NodoConstComun extends NodoConst {

	private ArrayList<NodoExpresion> argsactuales;	
	
	public NodoConstComun(Token t) {
		this.tokenNombre=t;
		this.argsactuales=new ArrayList<NodoExpresion>();
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
		String s=tokenNombre.getLexema()+"(";
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
