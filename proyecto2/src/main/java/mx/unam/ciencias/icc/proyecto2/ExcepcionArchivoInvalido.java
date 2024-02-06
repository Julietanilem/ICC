package mx.unam.ciencias.icc.proyecto2;

/**
 * Clase para excepciones de archivos de texto inválidos.
 */

public class ExcepcionArchivoInvalido  extends RuntimeException {
    
    public ExcepcionArchivoInvalido () {}
    
    /**
     * Constructor que recibe un mensaje para el usuario.
     * @param mensaje un mensaje que verá el usuario cuando ocurra la excepción.
     */
    public ExcepcionArchivoInvalido  (String mensaje) {
        super(mensaje);
    }
}
