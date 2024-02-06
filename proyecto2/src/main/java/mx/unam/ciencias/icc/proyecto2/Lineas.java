package mx.unam.ciencias.icc.proyecto2;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.Collator;
import mx.unam.ciencias.icc.Lista;
/**
 * Proyecto 2: Ordenador léxicográfico.
 */

public class Lineas {
    
    public  Lista<String> lineas= new Lista<String>();

    //Constructor de la entrada éstandar
    public Lineas (BufferedReader in){
		try {
			String linea;
			while(( linea = in.readLine()) != null)
				lineas.agregaFinal(linea);
			ordena();
		}catch (IOException e){
			System.err.printf("No se pudo leer de la entrada éstandar");
		}
    }
    
    //Constructor para entradas de archivos
    public Lineas (Lista<String> rutas){

		for (String ruta : rutas)
			getLineas(ruta);
		ordena();
	
    }
    
    /*
     *Método para obterner las líneas desde archivos
     *@param ruta de donde se pretenden obtener las líneas
     */
    private void getLineas(String ruta){
		try {
            BufferedReader in =
                new BufferedReader(
                    new InputStreamReader(
                        new FileInputStream(ruta)));
			String l;
			while((l = in.readLine()) != null )
				lineas.agregaFinal(l);
            in.close();
        }catch (IOException ioe) {
			System.err.printf("No pude cargar del archivo \"%s\".\n",  ruta);
        }
    }

    // Método para ordenar la lista con mergeSort de la clase lista.
    public void ordena(){
		Collator comparador = Collator.getInstance();
		comparador.setStrength(Collator.PRIMARY);
		lineas = lineas.mergeSort((a, b) -> {
			if(a.equals(b)) return 0;
			if(a.equals(b.toUpperCase()))  return 1;
			if(a.equals(b.toLowerCase())) return -1;
			if(a.isEmpty()) return -1;
			if(b.isEmpty()) return 1;
			String aN = remplazar(a);
			String bN = remplazar(b);
			return 	comparador.compare(aN, bN) ;
		});
    }

    /*
     *Método para remplazar los carácteres necesarios para que no afecten el orden léxicográfico
     *@param str una cadena con el texto original de la línea
     *@return una cadena sin carácteres no alfanúmericos y simplificando cáractere acentuados a las vocales sin acentuar y la ñ a la n
     */
	private String remplazar(String str){
		str = str.toLowerCase()
					.replaceAll("[áäâàå]", "a")
					.replaceAll("[éëêè]", "e")
					.replaceAll("[íïîì]", "i")
					.replaceAll("[óöôòõø]", "o")
					.replaceAll("[úüûù]", "u")
					.replace("ñ", "n")
					.replaceAll(" ", "")
					.replaceAll("[^\\p{L}\\p{N}]", "");
		return str;
    }
}
