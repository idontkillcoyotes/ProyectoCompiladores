
public class NodoThis extends NodoAcceso{
	
	private EClase thisclase;

	public NodoThis(EClase thisclase) {
		super();
		this.thisclase = thisclase;
	}

	public EClase getThisclase() {
		return thisclase;
	}

	@Override
	public Tipo check() {
		Tipo t=new TipoClase(thisclase.getToken());
		return t;
	}
	@Override
	public String toString(){
		String s="this";
		if(encadenado!=null){
			s+=encadenado.toString();
		}
		return s;
	}

}
