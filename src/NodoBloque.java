import java.util.ArrayList;

public class NodoBloque extends NodoSentencia{
	
	private ArrayList<NodoSentencia> sentencias;
	private ArrayList<EParametro> varslocales;
	private NodoBloque padre;
	private boolean checked;
		
	

	public NodoBloque(NodoBloque padre) {
		this.sentencias = new ArrayList<NodoSentencia>();
		this.varslocales = new ArrayList<EParametro>();
		this.padre = padre;
		this.checked=false;
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
		if(!checked){
			Utl.ts.setBloqueActual(this);
			for(NodoSentencia s: sentencias){
				s.check();
			}
			//saco las var locales de este bloque del miembro act
			int vars=this.varslocales.size();

			if(vars>0) Utl.ts.getMiembroAct().popVarLocales(vars);

			//seteo al padre de este bloque como bloque actual
			Utl.ts.setBloqueActual(this.padre);
			this.checked=true;
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
		return this.varslocales.add(var);
	}

}
