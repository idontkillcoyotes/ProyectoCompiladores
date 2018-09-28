
public final class Utl {
	
	public static final int TT_IDMETVAR=0;
	public static final int TT_IDCLASE=1;	
	public static final int TT_PUNPUNTOCOMA=2;
	public static final int TT_PUNPUNTO=3;
	public static final int TT_PUNCOMA=4;
	public static final int TT_PUNPARENT_A=5;
	public static final int TT_PUNPARENT_C=6;
	public static final int TT_PUNCORCH_A=7;
	public static final int TT_PUNCORCH_C=8;
	public static final int TT_PUNLLAVE_A=9;
	public static final int TT_PUNLLAVE_C=10;	
	public static final int TT_ASIGIGUAL=11;	
	public static final int TT_LITENTERO=12;
	public static final int TT_LITCARACTER=13;
	public static final int TT_LITSTRING=14;	
	public static final int TT_OPMAYOR=15;
	public static final int TT_OPMENOR=16;
	public static final int TT_OPNEGBOOL=17;
	public static final int TT_OPDOBLEIGUAL=18;
	public static final int TT_OPMAYORIG=19;
	public static final int TT_OPMENORIG=20;
	public static final int TT_OPDESIGUAL=21;
	public static final int TT_OPSUMA=22;
	public static final int TT_OPRESTA=23;
	public static final int TT_OPMULT=24;
	public static final int TT_OPDIV=25;
	public static final int TT_OPANDDOBLE=26;
	public static final int TT_OPORDOBLE=27;
	
	public static final int TT_FINARCHIVO=-1;
	
	public static final int TPC_CLASS=100;
	public static final int TPC_EXTENDS=101;
	public static final int TPC_STATIC=102;
	public static final int TPC_DYNAMIC=103;
	public static final int TPC_STRING=104;
	public static final int TPC_BOOLEAN=105;
	public static final int TPC_CHAR=106;
	public static final int TPC_INT=107;
	public static final int TPC_PUBLIC=108;
	public static final int TPC_PRIVATE=109;
	public static final int TPC_VOID=110;
	public static final int TPC_NULL=111;
	public static final int TPC_IF=112;
	public static final int TPC_ELSE=113;
	public static final int TPC_WHILE=114;
	public static final int TPC_RETURN=115;
	public static final int TPC_THIS=116;
	public static final int TPC_NEW=117;
	public static final int TPC_TRUE=118;
	public static final int TPC_FALSE=119;	
	
	public static TablaSimbolos ts;

	private static final String[] PALABRASCLAVE = {"class","extends","static","dynamic","String","boolean","char","int",
			"public","private","void","null","if","else","while","return","this","new","true","false"};	
	private static final String[] TOKEN={"idMetVar","idClase",";",".",",","(",")","[","]","{","}",
			"=","litEntero","litCaracter","litString",">","<","!","==",">=","<=","!=","+","-",
			"*","/","&&","||"};
	
	//Este metodo es utilizado para imprimir los tokens
	public static String getTipoID(int id){
		if ((id>=0)&&(id<100)){
			return TOKEN[id];					
		}else if (id==-1){
			return "FIN_ARCHIVO";
		}else{
			return PALABRASCLAVE[id-100];
		}
	}
	//Este metodo lo utiliza el analizador lexico para obtener el
	//tipo de un token que puede ser idMetVar o una palabra clave
	public static int getIDMetVarOPalabraClave(String lexema){
		int id=0;
		int largo=PALABRASCLAVE.length;
		int i=0;
		//IDMETVAR = 0, IDPALABRACLAVE = 100 + indice en arreglo
		boolean espc=false;
		while ((!espc) && (i<largo)){			
			if (PALABRASCLAVE[i].equals(lexema)){
				//es palabra clave
				espc=true;
				id=100+i;
			}
			i++;
		}
		//si no es palabra clave id=0=IDMETVAR
		return id;
	}
		
}
