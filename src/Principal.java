
public class Principal {	
	
	public static void main(String[] args) {	
		if (args.length==1){
			EntradaSalida io= new EntradaSalida(args[0]);
			AnalizadorLexico alex=new AnalizadorLexico(io);
			AnalizadorSintactico asin=new AnalizadorSintactico(alex);
			try {	
				asin.start();
				System.out.println("Analisis sintactico exitoso! :)");
				Utl.ts.imprimir();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			
		}
		else{
			System.out.println("Ingrese el path del archivo de entrada como primer parametro.");
		}
		
	}

}
