package mx.unam.ciencias.icc;

/**
 * Clase para bases de datos de sismos.
 */
public class BaseDeDatosSismos
    extends BaseDeDatos<Sismo, CampoSismo> {

    /**
     * Crea un sismo en blanco.
     * @return un sismo en blanco.
     */
    @Override public Sismo creaRegistro() {
        return new Sismo(null, 0, 0, 0, 0, null );
    }
}
