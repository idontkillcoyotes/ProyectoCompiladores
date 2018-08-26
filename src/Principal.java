
public class Principal {

	public static void main(String[] args) {
		
		EntradaSalida io= new EntradaSalida("C:/Testing/Etapa1/Correctos/Test1.txt");
		char c;
		String palabra;

		System.out.println("***************************");
		System.out.println("Test de palabras claves:");
		System.out.println("***************************");
		
		palabra="hola";		
		System.out.println("\t Es "+palabra+" una palabra clave?: "+Utilidades.esPalabraClave(palabra));
		palabra="int";
		System.out.println("\t Es "+palabra+" una palabra clave?: "+Utilidades.esPalabraClave(palabra));
		palabra="extends";
		System.out.println("\t Es "+palabra+" una palabra clave?: "+Utilidades.esPalabraClave(palabra));
		palabra="retUrn";
		System.out.println("\t Es "+palabra+" una palabra clave?: "+Utilidades.esPalabraClave(palabra));
		
		
		System.out.println("***************************");
		int max=100;		
		System.out.println("Primeros "+max+" caracteres:");	
		for(int i=1;i<=max;i++){		
			c=io.nextChar();
			System.out.println("Linea: "+io.getNroLinea()+" Col:"+io.getNroColumna()+"\t"+c);
		}
	}

}
