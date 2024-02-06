package mx.unam.ciencias.icc.proyecto2;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import mx.unam.ciencias.icc.IteradorLista;

/**
 * Clase de aplicación 
 */

public class Aplicacion {

    //Clase con la información proveniente de la línea de comandos
    private LineaDeComandos lc;
    //Clase con las lineas que se van a ordenar
    private Lineas lineas;
    
    //Construye la aplicación a partir de los argumentos pasados
    public Aplicacion(String[] args){
        lc = new LineaDeComandos(args);
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(System.in));
        lineas = (lc.rutas.esVacia()) ? new Lineas (in) : new Lineas(lc.rutas);   
    }
    
    //Llama al método correspondiente al modo
    public void ejecuta(){
        switch(lc.modo.getModo()){
            case GUARDA_REVERSA:
                guardaReversa(lc.modo.getArchivo());
                break;
            case REVERSA :
                reversa();
                break;
            case GUARDAR :
                guarda(lc.modo.getArchivo());
                break;
            case NORMAL:
                normal();
                break;
        }
    }
    
    /*
     *Accion a ejecutar si no se pasaron banderas
     *Se imprimen las líneas en orden
    */
    private void normal() {
        for(String linea: lineas.lineas )
            System.out.println(linea);
    }
    
    /*
     *Accion a ejecutar con el modo -r
      * Se imprimen las líneas en orden inverso
    */
    private void reversa() {
        IteradorLista<String>  i = lineas.lineas.iteradorLista();
        i.end();
        while (i.hasPrevious())
            System.out.println(i.previous());
    }
    
    /*
     *Accion a ejecutar con el modo -o
     *Se guardan las lineas en orden en el documento especificado
     *@param archivo donde se quieren guardar las lineas ordenadas
    */
    private void guarda(String ruta) {
        try{
            BufferedWriter out =
                new BufferedWriter(
                    new OutputStreamWriter(
                    new FileOutputStream(ruta )));
            for(String linea:lineas.lineas)
                out.write(linea+"\n");
            out.close();
        }catch(IOException ioe){
            System.err.printf("No pude guardar en el archivo \"%s\".\n", ruta);
        }
    }
    
    /*
     *Accion a ejecutar con el modo -r
     *Se guardan las lineas en orden inverso en el documento especificado
     *@param archivo donde se quieren guardar las lineas ordenadas en reversa
    */
    private void guardaReversa(String ruta) {
        try {
            BufferedWriter out =
                new BufferedWriter(
                    new OutputStreamWriter(
                        new FileOutputStream(ruta)));
        IteradorLista<String>  i = lineas.lineas.iteradorLista();
        i.end();
        while (i.hasPrevious())
            out.write(i.previous()+"\n");
        out.close();
        }catch(IOException ioe) {
            System.err.printf("No pude guardar en el archivo \"%s\".\n", ruta);
        }
    }
}
