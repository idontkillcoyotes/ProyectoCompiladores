
public class Minijavac {
	
	public static void main(String[] args) {	
		if (args.length==1){
			EntradaSalida io= new EntradaSalida(args[0],"c:/testing/out.txt");
			AnalizadorLexico alex=new AnalizadorLexico(io);
			AnalizadorSintactico asin=new AnalizadorSintactico(alex);
			Utl.setES(io);
			try {	
				asin.start();				
				//Utl.ts.imprimir();
				System.out.println("Compilación exitosa.");
			} catch (Exception e) {
				System.out.println(e.getMessage());
				//e.printStackTrace();
			}
			
		}
		else{
			System.out.println("Ingrese el path del archivo de entrada como parametro.");
		}
		
	}

}
