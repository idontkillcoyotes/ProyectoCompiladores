
public class Minijavac {
	
	public static void main(String[] args) {	
		if (args.length==1){
			EntradaSalida io= new EntradaSalida(args[0],"out.txt");
			AnalizadorLexico alex=new AnalizadorLexico(io);
			AnalizadorSintactico asin=new AnalizadorSintactico(alex);
			Utl.setES(io);
			try {	
				asin.start();				
				//Utl.ts.imprimir();
				System.out.println("Compilación exitosa.\nArchivo de salida \"out.txt\" creado con éxito.");
			} catch (Exception e) {
				System.out.println(e.getMessage());
				//e.printStackTrace();
			}
			
		}
		else if (args.length==2){
			EntradaSalida io= new EntradaSalida(args[0],args[1]);
			AnalizadorLexico alex=new AnalizadorLexico(io);
			AnalizadorSintactico asin=new AnalizadorSintactico(alex);
			Utl.setES(io);
			try {	
				asin.start();				
				//Utl.ts.imprimir();
				System.out.println("Compilación exitosa.\nArchivo de salida \""+args[1]+"\" creado con éxito.");
			} catch (Exception e) {
				System.out.println(e.getMessage());
				//e.printStackTrace();
			}			
		}
		else{
			System.out.println("Ingrese el path del archivo de entrada como primer parametro y opcionalmente"
					+ " ingrese el path del archivo de salida como segundo parametro");
		}
		
	}

}
