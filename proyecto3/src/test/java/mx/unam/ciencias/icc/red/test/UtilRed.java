package mx.unam.ciencias.icc.red.test;

import mx.unam.ciencias.icc.BaseDeDatosSismos;
import mx.unam.ciencias.icc.Sismo;
import mx.unam.ciencias.icc.test.TestSismo;

/**
 * Clase con métodos estáticos utilitarios para las pruebas unitarias de red.
 */
public class UtilRed {

    /* Evitamos instanciación. */
    private UtilRed() {}

    /* Número de heridos inicial. */
    public static final int HERIDOS_INICIAL = 1000000;

    /* Contador de números de heridos. */
    private static int contador;

    /**
     * Espera el número de milisegundos de forma segura.
     * @param milisegundos el número de milisegundos a esperar.
     */
    public static void espera(int milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException ie) {}
    }

    /**
     * Llena una base de datos de sismos.
     * @param bdd la base de datos a llenar.
     * @param total el total de sismos.
     */
    public static void llenaBaseDeDatos(BaseDeDatosSismos bdd, int total) {
        for (int i = 0; i < total; i++) {
            int c = HERIDOS_INICIAL + i;
            bdd.agregaRegistro(TestSismo.sismoAleatorio(c));
        }
    }

    /**
     * Crea un sismo aleatorio.
     * @param total el total de sismos.
     * @return un sismo aleatorio con un número de heridos único.
     */
    public static Sismo sismoAleatorio(int total) {
        int nc = HERIDOS_INICIAL + total*2 + contador++;
        return TestSismo.sismoAleatorio(nc);
    }

}
