import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class EntradaSalida {

    private String nombreArchivo;    
    private FileReader fileReader;
    private BufferedReader bufferedReader;
    private boolean finArchivo;
    private int ultimoChar;
    
    public EntradaSalida(String archivo){
    	nombreArchivo=archivo;
    	finArchivo=true;
    	ultimoChar=-1;
    	try {
    		// FileReader reads text files in the default encoding.
    		fileReader = new FileReader(nombreArchivo);
    		// Always wrap FileReader in BufferedReader.
    		bufferedReader = new BufferedReader(fileReader);
    		finArchivo=false;
    	}
    	catch(FileNotFoundException ex) {
    		System.out.println("No se ha podido abrir el archivo '"+nombreArchivo+"'");                
    	}
    }
    
    private void cerrarArchivo(){
    	try {
			bufferedReader.close();			
		} catch (IOException e) {
			System.out.println("Error al intentar cerrar el archivo '"+nombreArchivo+ "'");
			e.printStackTrace();
		}
    	finArchivo=true;
    }
    
    public int ultimoChar(){
    	return this.ultimoChar;
    }    

    public char nextChar(){
    	int caracter;
    	char toReturn = 0;
    	if (!finArchivo){
    		try {
    			caracter = bufferedReader.read();
    			ultimoChar=caracter;
    			if (caracter==-1){
    				cerrarArchivo();		
    			}
    			else{
    				toReturn=(char)caracter;
    			}
    		}	
    		catch(IOException ex) {
    			System.out.println("Error al leer el archivo '"+nombreArchivo+ "'");
    		}
    	}
    	return toReturn;
    }

}