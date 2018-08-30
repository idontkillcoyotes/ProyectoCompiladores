
public class Principal {	
	
	public static void main(String[] args) {
		
		EntradaSalida io= new EntradaSalida("C:/Testing/Etapa1/Correctos/Test11.txt");
		
		AnalizadorLexico alex=new AnalizadorLexico(io);
		
		String q="cadena";
		q+="\n";
		q+=" resto";
		//System.out.println(s);
		Token t;
		System.out.println("LIN\tCOL\tTIPO\tLEXEMA");
		try {
			while (!alex.finArchivo()){
				t=alex.nextToken();
				System.out.println(t.toString());			
			}
			t=alex.nextToken();
			System.out.println(t.toString());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}			
	}

}
