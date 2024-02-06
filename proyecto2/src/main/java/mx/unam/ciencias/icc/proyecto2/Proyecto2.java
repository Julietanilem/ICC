package mx.unam.ciencias.icc.proyecto2;
/**
 * Proyecto 2: Ordenador léxicográfico.
 */
public class Proyecto2 {
    public static void main (String[] args){
	try{
		Aplicacion a = new Aplicacion(args);
		a.ejecuta();
	}catch(ExcepcionBanderaInvalida e){
		System.err.printf("Usa una bandera adecuada [ -o | -r ]\n");
	}catch(ExcepcionIdentificadorInvalido e){
		System.err.printf("Usa un identificador de archivo válido\n");
	}catch(ExcepcionArchivoInvalido e){
		System.err.printf("Ingresa archivos de texto válidos <archivo>.txt \n");
	}	
    }
}
