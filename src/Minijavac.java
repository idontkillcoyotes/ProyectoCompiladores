
public class Minijavac {
	
	public static void main(String[] args) {	
		if (args.length==1){
			EntradaSalida io= new EntradaSalida(args[0]);
			AnalizadorLexico alex=new AnalizadorLexico(io);
			AnalizadorSintactico asin=new AnalizadorSintactico(alex);
			try {	
				asin.start();				
				//Utl.ts.imprimir();
				System.out.println("Compilación exitosa.");
			} catch (Exception e) {
				System.out.println(e.getMessage());				
			}
			
		}
		else{
			System.out.println("Ingrese el path del archivo de entrada como parametro.");
		}
		
	}

}
