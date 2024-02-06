package mx.unam.ciencias.icc.proyecto2;

/**
 * Clase para excepciones  para indicar que una bandera pasada es inválida.
 */

public class ExcepcionBanderaInvalida extends RuntimeException {
    public ExcepcionBanderaInvalida() {}
    
    /**
     * Constructor que recibe un mensaje para el usuario.
     * @param mensaje un mensaje que verá el usuario cuando ocurra la excepción.
     */

    public ExcepcionBanderaInvalida(String mensaje) {
        super(mensaje);
    }
}
