
public class Principal {	
	
	public static void main(String[] args) {
		
		EntradaSalida io= new EntradaSalida("C:/Testing/Etapa1/Correctos/Test4.txt");
		
		AnalizadorLexico alex=new AnalizadorLexico(io);
		
		Token t;
		System.out.println("LIN\tCOL\tTIPO\tLEXEMA");
		try {
			while (!alex.finArchivo()){
				t=alex.nextToken();
				System.out.println(t.toString());			
			}
		} catch (CaracterDesconocidoException e) {
			System.out.println(e.getMessage());
		}			
	}

}
