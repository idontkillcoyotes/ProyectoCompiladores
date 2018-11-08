import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class EntradaSalida {

    private String archivoEntrada;    
    private String archivoSalida;
    private FileReader fileReader;
    private BufferedReader bufferedReader;
    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;
    private boolean finArchivoEntrada;
    private int caracterActual;
    private int nroLinea;
    private int nroColumna;
    
    //TODO: eliminar escritura?
    public EntradaSalida(String archivo){
    	archivoEntrada=archivo;
    	nroLinea=0;
    	nroColumna=0;
    	finArchivoEntrada=true;
    	caracterActual=-1;
    	try {
    		// FileReader reads text files in the default encoding.
    		fileReader = new FileReader(archivoEntrada);
    		// Always wrap FileReader in BufferedReader.
    		bufferedReader = new BufferedReader(fileReader);
    		finArchivoEntrada=false;
    		nroLinea=1;
    	}
    	catch(FileNotFoundException ex) {
    		System.out.println("No se ha podido abrir el archivo '"+archivoEntrada+"'");                
    	}
    }
    
    public EntradaSalida(String entrada,String salida){
    	archivoEntrada=entrada;
    	archivoSalida=salida;
    	nroLinea=0;
    	nroColumna=0;
    	finArchivoEntrada=true;
    	caracterActual=-1;
    	try {    		
    		fileReader = new FileReader(archivoEntrada);
    		fileWriter = new FileWriter(archivoSalida);
    		bufferedReader = new BufferedReader(fileReader);
    		bufferedWriter = new BufferedWriter(fileWriter);
    		finArchivoEntrada=false;
    		nroLinea=1;
    	}catch(FileNotFoundException e) {
    		System.out.println(e.getMessage());
    		e.printStackTrace();
    	}catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
    }
    
    private void cerrarArchivoEntrada(){
    	try {
			bufferedReader.close();			
		} catch (IOException e) {
			System.out.println("Error al intentar cerrar el archivo '"+archivoEntrada+ "'");
			e.printStackTrace();
		}
    	finArchivoEntrada=true;
    }
    public void cerrarArchivoSalida(){
    	try {
			bufferedWriter.close();			
		} catch (IOException e) {
			System.out.println("Error al intentar cerrar el archivo '"+archivoSalida+ "'");
			e.printStackTrace();
		}
    	finArchivoEntrada=true;
    }
    
    public boolean finArchivo(){
    	return this.finArchivoEntrada;
    }
    
    public int ultimoChar(){
    	return this.caracterActual;
    }
    
    public int getNroLinea(){
    	return this.nroLinea;
    }

    public int getNroColumna(){
    	return this.nroColumna;
    }
    
    public void imprimirLinea(String cadena){
    	try {
			this.bufferedWriter.append(cadena);
			this.bufferedWriter.append("\n");
		} catch (IOException e) {
			System.out.println("Error al intentar escribir en el archivo: '"+archivoSalida+"'");
			e.printStackTrace();
		}
    	
    }
    
        
    public char nextChar(){
    	int caracter=-1;
    	char toReturn = 0;
    	if (!finArchivoEntrada){
    		try {
    			caracter = bufferedReader.read();
    			caracterActual=caracter;    			
    			if(caracter=='\n'){
    				//salto de linea
    				nroLinea++;
    				nroColumna=0;
    			}else if (caracter=='\t'){
    				//tab
    				nroColumna+=4;
    			}else{
    				nroColumna++;
    			}
    			if (caracter==-1){
    				cerrarArchivoEntrada();		
    			}
    			else{
    				toReturn=(char)caracter;
    			}
    		}	
    		catch(IOException ex) {
    			System.out.println("Error al leer el archivo '"+archivoEntrada+ "'");
    		}
    	}
    	return toReturn;
    }

}