import java.util.ArrayList;
import java.util.HashMap;

public class NodoBloque extends NodoSentencia{
	
	private ArrayList<NodoSentencia> sentencias;
	private HashMap<String,EParametro> varslocales;
	private NodoBloque padre;
		
	

	public NodoBloque(NodoBloque padre) {
		super();
		this.sentencias = new ArrayList<NodoSentencia>();
		this.varslocales = new HashMap<String,EParametro>();
		this.padre = padre;
	}

	public ArrayList<NodoSentencia> getSentencias() {
		return sentencias;
	}

	public NodoBloque getPadre() {
		return padre;
	}

	public boolean addSentencia(NodoSentencia s){
		return this.sentencias.add(s);
	}
	
	@Override
	public void check() throws SemanticException {
		Utl.ts.setBloqueActual(this);
		for(NodoSentencia s: sentencias){
			s.check();
		}
	}
	
	@Override
	public String toString(){
		String s="\t{\n";
		for(NodoSentencia sen: sentencias){
			s+="\t"+sen.toString();
		}
		s+="\t}\n";
		return s;
	}

	public boolean addVarLocal(EParametro var) {
		if (varslocales.containsKey(var.getNombre()))
			return false;
		else{
			varslocales.put(var.getNombre(),var);
			return true;
		}
	}
	
	public EParametro getVarLocal(String n){
		return this.varslocales.get(n);
	}
}
