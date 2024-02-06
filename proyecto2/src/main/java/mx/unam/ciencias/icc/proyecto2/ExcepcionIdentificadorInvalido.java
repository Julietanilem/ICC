package mx.unam.ciencias.icc.proyecto2;

/**
 * Clase para excepciones de archivos de texto inválidos que no servirán como identificadores.
 */

public class ExcepcionIdentificadorInvalido  extends RuntimeException {
    public ExcepcionIdentificadorInvalido () {}

    /**
     * Constructor que recibe un mensaje para el usuario.
     * @param mensaje un mensaje que verá el usuario cuando ocurra la excepción.
     */

    public ExcepcionIdentificadorInvalido (String mensaje) {
        super(mensaje);
    }
}
