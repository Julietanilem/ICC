package mx.unam.ciencias.icc.red;

import java.io.IOException;
import mx.unam.ciencias.icc.BaseDeDatos;
import mx.unam.ciencias.icc.BaseDeDatosSismos;
import mx.unam.ciencias.icc.CampoSismo;
import mx.unam.ciencias.icc.Sismo;

/**
 * Clase para servidores de bases de datos de sismos.
 */
public class ServidorBaseDeDatosSismos
    extends ServidorBaseDeDatos<Sismo> {

    /**
     * Construye un servidor de base de datos de sismos.
     * @param puerto el puerto dónde escuchar por conexiones.
     * @param archivo el archivo en el disco del cual cargar/guardar la base de
     *                datos.
     * @throws IOException si ocurre un error de entrada o salida.
     */
    public ServidorBaseDeDatosSismos(int puerto, String archivo)
    throws IOException {
        super(puerto, archivo);
	
    }

    /**
     * Crea una base de datos de sismos.
     * @return una base de datos de sismos.
     */
    @Override public
    BaseDeDatos<Sismo, CampoSismo> creaBaseDeDatos() {
        return new BaseDeDatosSismos();
    }
}
