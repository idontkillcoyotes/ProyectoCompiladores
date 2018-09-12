
public final class Utils {
	
	private static final String[] PALABRASCLAVE = {"class","extends","static","dynamic","String","boolean","char","int",
			"public","private","void","null","if","else","while","return","this","new","true","false"};
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
	//Estos son necesarios?
	//public static final int TT_COMMULTIL_A=30;
	//public static final int TT_COMMULTIL_C=31;
	//public static final int TT_COMSIMPLE=32;	
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
	public static final int TPC_VOID=120;
	public static final int TPC_NULL=121;
	public static final int TPC_IF=122;
	public static final int TPC_ELSE=123;
	public static final int TPC_WHILE=124;
	public static final int TPC_RETURN=125;
	public static final int TPC_THIS=126;
	public static final int TPC_NEW=127;
	public static final int TPC_TRUE=128;
	public static final int TPC_FALSE=129;
	
	private static String[] tokenn={"IDMETVAR","IDCLASE","PUNPUNTOCOMA","PUNPUNTO","PUNCOMA","PUNPARENT_A","PUNPARENT_C","PUNCORCH_A","PUNCORCH_C","PUNLLAVE_A","PUNLLAVE_C",
			"ASIGIGUAL","LITENTERO","LITCARACTER","LITSTRING","OPMAYOR","OPMENOR","OPNEGBOOL","OPDOBLEIGUAL","OPMAYORIG","OPMENORIG","OPDESIGUAL","OPSUMA","OPRESTA",
			"OPMULT","OPDIV","OPANDDOBLE","OPORDOBLE"};
	private static String[] tokenpc={"PC_CLASS","PC_EXTENDS","PC_STATIC","PC_DYNAMIC","PC_STRING","PC_BOOLEAN","PC_CHAR","PC_INT","PC_PUBLIC","PC_PRIVATE","PC_VOID","PC_NULL","PC_IF","PC_ELSE",
			"PC_WHILE","PC_RETURN","PC_THIS","PC_NEW","PC_TRUE","PC_FALSE"};
	
	public static String getTipoID(int id){
		if ((id>=0)&&(id<100)){
			return tokenn[id];					
		}else if (id==-1){
			return "FINARCHIVO";
		}else{
			return tokenpc[id-100];
		}
	}
	
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
