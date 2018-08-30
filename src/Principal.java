
public class Principal {	
	
	public static void main(String[] args) {	
		if (args.length==1){
			EntradaSalida io= new EntradaSalida(args[0]);
			AnalizadorLexico alex=new AnalizadorLexico(io);
			Token t;
			System.out.println("LIN\tCOL\tTIPO\tLEXEMA");
			try {
				while (!alex.finArchivo()){
					t=alex.nextToken();
					System.out.println(t.toString());			
				}
				//imprimo el token fin de archivo
				t=alex.nextToken();
				System.out.println(t.toString());
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		else if (args.length==2){
			EntradaSalida io= new EntradaSalida(args[0],args[1]);
			AnalizadorLexico alex=new AnalizadorLexico(io);
			Token t;
			io.imprimirLinea("LIN\tCOL\tTIPO\tLEXEMA");
			try {
				while (!alex.finArchivo()){
					t=alex.nextToken();
					io.imprimirLinea(t.toString());			
				}
				//imprimo el token fin de archivo
				t=alex.nextToken();
				io.imprimirLinea(t.toString());
				io.cerrarArchivoSalida();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}			
		}
		else{
			System.out.println("Ingrese el path del archivo de entrada como primer parametro y opcionalmente"
					+ " ingrese el path del archivo de salida como segundo parametro");
		}
		
	}

}
