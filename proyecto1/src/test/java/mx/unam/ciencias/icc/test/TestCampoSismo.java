package mx.unam.ciencias.icc.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Random;
import mx.unam.ciencias.icc.CampoSismo;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * Clase para pruebas unitarias de la enumeración {@link CampoSismo}.
 */
public class TestCampoSismo {

    /** Expiración para que ninguna prueba tarde más de 5 segundos. */
    @Rule public Timeout expiracion = Timeout.seconds(5);

    /**
     * Prueba unitaria para {@link CampoSismo#toString}.
     */
    @Test public void testToString() {
        String s = CampoSismo.FECHA.toString();
        Assert.assertTrue(s.equals("Fecha"));
        s = CampoSismo.MUERTES.toString();
        Assert.assertTrue(s.equals("Muertes"));
        s = CampoSismo.PROFUNDIDAD.toString();
        Assert.assertTrue(s.equals("Profundidad"));
        s = CampoSismo.REPLICAS.toString();
        Assert.assertTrue(s.equals("Réplicas"));
        s = CampoSismo.MAGNITUD.toString();
        Assert.assertTrue(s.equals("Magnitud"));
        s = CampoSismo.ENTIDADES.toString();
        Assert.assertTrue(s.equals("Entidades"));
    }
}
