import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class EntradaSalida {

    private String nombreArchivo;    
    private FileReader fileReader;
    private BufferedReader bufferedReader;
    private boolean finArchivo;
    private int caracterActual;
    private int nroLinea;
    private int nroColumna;
    
    
    public EntradaSalida(String archivo){
    	nombreArchivo=archivo;
    	nroLinea=0;
    	nroColumna=0;
    	finArchivo=true;
    	caracterActual=-1;
    	try {
    		// FileReader reads text files in the default encoding.
    		fileReader = new FileReader(nombreArchivo);
    		// Always wrap FileReader in BufferedReader.
    		bufferedReader = new BufferedReader(fileReader);
    		finArchivo=false;
    		nroLinea=1;
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
    
    public boolean finArchivo(){
    	return this.finArchivo;
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
        
    public char nextChar(){
    	int caracter;
    	char toReturn = 0;
    	if (!finArchivo){
    		try {
    			caracter = bufferedReader.read();
    			caracterActual=caracter;    			
    			if(caracter=='\n'){
    				//salto de linea
    				nroLinea++;
    				//System.out.print("<"+caracter+">");
    				nroColumna=0;
    			}else if (caracter=='\t'){
    				nroColumna+=4;
    			}else{
    				nroColumna++;
    			}
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