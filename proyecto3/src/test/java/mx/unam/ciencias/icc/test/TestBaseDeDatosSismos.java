package mx.unam.ciencias.icc.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Random;
import mx.unam.ciencias.icc.BaseDeDatos;
import mx.unam.ciencias.icc.BaseDeDatosSismos;
import mx.unam.ciencias.icc.CampoSismo;
import mx.unam.ciencias.icc.Sismo;
import mx.unam.ciencias.icc.EscuchaBaseDeDatos;
import mx.unam.ciencias.icc.EventoBaseDeDatos;
import mx.unam.ciencias.icc.Lista;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * Clase para pruebas unitarias de la clase {@link BaseDeDatosSismos}.
 */
public class TestBaseDeDatosSismos {

    /** Expiración para que ninguna prueba tarde más de 5 segundos. */
    @Rule public Timeout expiracion = Timeout.seconds(5);

    /* Generador de números aleatorios. */
    private Random random;
    /* Base de datos de sismos. */
    private BaseDeDatosSismos bdd;
    /* Número total de sismos. */
    private int total;

    /**
     * Crea un generador de números aleatorios para cada prueba y una base de
     * datos de sismos.
     */
    public TestBaseDeDatosSismos() {
        random = new Random();
        bdd = new BaseDeDatosSismos();
        total = 2 + random.nextInt(100);
    }

    /**
     * Prueba unitaria para {@link
     * BaseDeDatosSismos#BaseDeDatosSismos}.
     */
    @Test public void testConstructor() {
        Lista<Sismo> sismos = bdd.getRegistros();
        Assert.assertFalse(sismos == null);
        Assert.assertTrue(sismos.getLongitud() == 0);
        Assert.assertTrue(bdd.getNumRegistros() == 0);
        bdd.agregaEscucha((e, r1, r2) -> {});
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#getNumRegistros}.
     */
    @Test public void testGetNumRegistros() {
        Assert.assertTrue(bdd.getNumRegistros() == 0);
        for (int i = 0; i < total; i++) {
            Sismo e = TestSismo.sismoAleatorio();
            bdd.agregaRegistro(e);
            Assert.assertTrue(bdd.getNumRegistros() == i+1);
        }
        Assert.assertTrue(bdd.getNumRegistros() == total);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#getRegistros}.
     */
    @Test public void testGetRegistros() {
        Lista<Sismo> l = bdd.getRegistros();
        Lista<Sismo> r = bdd.getRegistros();
        Assert.assertTrue(l.equals(r));
        Assert.assertFalse(l == r);
        Sismo[] sismos = new Sismo[total];
        for (int i = 0; i < total; i++) {
            sismos[i] = TestSismo.sismoAleatorio();
            bdd.agregaRegistro(sismos[i]);
        }
        l = bdd.getRegistros();
        int c = 0;
        for (Sismo e : l)
            Assert.assertTrue(sismos[c++].equals(e));
        l.elimina(sismos[0]);
        Assert.assertFalse(l.equals(bdd.getRegistros()));
        Assert.assertFalse(l.getLongitud() == bdd.getNumRegistros());
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#agregaRegistro}.
     */
    @Test public void testAgregaRegistro() {
        for (int i = 0; i < total; i++) {
            Sismo e = TestSismo.sismoAleatorio();
            Assert.assertFalse(bdd.getRegistros().contiene(e));
            bdd.agregaRegistro(e);
            Assert.assertTrue(bdd.getRegistros().contiene(e));
            Lista<Sismo> l = bdd.getRegistros();
            Assert.assertTrue(l.get(l.getLongitud() - 1).equals(e));
        }
        boolean[] llamado =  { false };
        bdd.agregaEscucha((e, r1, r2) -> {
                Assert.assertTrue(e == EventoBaseDeDatos.REGISTRO_AGREGADO);
                Assert.assertTrue(r1.equals(new Sismo("A", 1, 1, 1, 1, "A")));
                Assert.assertTrue(r2 == null);
                llamado[0] = true;
            });
        bdd.agregaRegistro(new Sismo("A", 1, 1, 1, 1, "A"));
        Assert.assertTrue(llamado[0]);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#eliminaRegistro}.
     */
    @Test public void testEliminaRegistro() {
        int ini = random.nextInt(1000000);
        for (int i = 0; i < total; i++) {
            Sismo e = TestSismo.sismoAleatorio(ini + i);
            bdd.agregaRegistro(e);
        }
        while (bdd.getNumRegistros() > 0) {
            int i = random.nextInt(bdd.getNumRegistros());
            Sismo e = bdd.getRegistros().get(i);
            Assert.assertTrue(bdd.getRegistros().contiene(e));
            bdd.eliminaRegistro(e);
            Assert.assertFalse(bdd.getRegistros().contiene(e));
        }
        boolean[] llamado = { false };
        Sismo sismo = new Sismo("A", 1, 1, 1, 1, "A");
        bdd.agregaRegistro(sismo);
        bdd.agregaEscucha((e, r1, r2) -> {
                Assert.assertTrue(e == EventoBaseDeDatos.REGISTRO_ELIMINADO);
                Assert.assertTrue(r1.equals(new Sismo("A", 1, 1, 1, 1, "A")));
                Assert.assertTrue(r2 == null);
                llamado[0] = true;
            });
        bdd.eliminaRegistro(sismo);
        Assert.assertTrue(llamado[0]);
        bdd = new BaseDeDatosSismos();
        llamado[0] = false;
        bdd.agregaEscucha((e, r1, r2) -> {
                Assert.assertTrue(e == EventoBaseDeDatos.REGISTRO_ELIMINADO);
                Assert.assertTrue(r1.equals(new Sismo("A", 1, 1, 1, 1, "A")));
                Assert.assertTrue(r2 == null);
                llamado[0] = true;
            });
        bdd.eliminaRegistro(sismo);
        Assert.assertTrue(llamado[0]);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#modificaRegistro}.
     */
    @Test public void testModificaRegistro() {
        for (int i = 0; i < total; i++) {
            Sismo e = TestSismo.sismoAleatorio(total + i);
            Assert.assertFalse(bdd.getRegistros().contiene(e));
            bdd.agregaRegistro(e);
            Assert.assertTrue(bdd.getRegistros().contiene(e));
            Lista<Sismo> l = bdd.getRegistros();
            Assert.assertTrue(l.get(l.getLongitud() - 1).equals(e));
        }
        Sismo a = new Sismo("A", 1, 1, 1, 1, "A");
        Sismo b = new Sismo("B", 2, 2, 2, 2, "B");
        bdd.agregaRegistro(a);
        boolean[] llamado = { false };
        bdd.agregaEscucha((e, r1, r2) -> {
                Assert.assertTrue(e == EventoBaseDeDatos.REGISTRO_MODIFICADO);
                Assert.assertTrue(r1.equals(new Sismo("A", 1, 1, 1, 1, "A")));
                Assert.assertTrue(r2.equals(new Sismo("B", 2, 2, 2, 2, "B")));
                llamado[0] = true;
            });
        Sismo c = new Sismo("A", 1, 1, 1, 1, "A");
        bdd.modificaRegistro(c, b);
        Assert.assertTrue(llamado[0]);
        Assert.assertTrue(c.equals(new Sismo("A", 1, 1, 1, 1, "A")));
        Assert.assertTrue(b.equals(new Sismo("B", 2, 2, 2, 2, "B")));
        int ca = 0, cb = 0;
        for (Sismo e : bdd.getRegistros()) {
            ca += e.equals(c) ? 1 : 0;
            cb += e.equals(b) ? 1 : 0;
        }
        Assert.assertTrue(ca == 0);
        Assert.assertTrue(cb == 1);
        bdd = new BaseDeDatosSismos();
        a = new Sismo("A", 1, 1, 1, 1, "A");
        b = new Sismo("B", 2, 2, 2, 2, "B");
        bdd.agregaRegistro(a);
        bdd.agregaEscucha((e, r1, r2) -> {
                Assert.assertTrue(e == EventoBaseDeDatos.REGISTRO_MODIFICADO);
                Assert.assertTrue(r1.equals(new Sismo("A", 1, 1, 1, 1, "A")));
                Assert.assertTrue(r2.equals(new Sismo("B", 2, 2, 2, 2, "B")));
                llamado[0] = true;
            });
        bdd.modificaRegistro(a, b);
        Assert.assertTrue(a.equals(b));
        Assert.assertTrue(llamado[0]);
        bdd = new BaseDeDatosSismos();
        llamado[0] = false;
        bdd.agregaEscucha((e, r1, r2) -> {
                Assert.assertTrue(e == EventoBaseDeDatos.REGISTRO_MODIFICADO);
                llamado[0] = true;
            });
        bdd.modificaRegistro(new Sismo("A", 1, 1, 1, 1, "A"),
                            new Sismo("B", 2, 2, 2, 2, "B"));
        Assert.assertFalse(llamado[0]);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#limpia}.
     */
    @Test public void testLimpia() {
        for (int i = 0; i < total; i++) {
            Sismo e = TestSismo.sismoAleatorio();
            bdd.agregaRegistro(e);
        }
        boolean[] llamado = { false };
        bdd.agregaEscucha((e, r1, r2) -> {
                Assert.assertTrue(e == EventoBaseDeDatos.BASE_LIMPIADA);
                Assert.assertTrue(r1 == null);
                Assert.assertTrue(r2 == null);
                llamado[0] = true;
            });
        Lista<Sismo> registros = bdd.getRegistros();
        Assert.assertFalse(registros.esVacia());
        Assert.assertFalse(registros.getLongitud() == 0);
        bdd.limpia();
        registros = bdd.getRegistros();
        Assert.assertTrue(registros.esVacia());
        Assert.assertTrue(registros.getLongitud() == 0);
        Assert.assertTrue(llamado[0]);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#guarda}.
     */
    @Test public void testGuarda() {
        for (int i = 0; i < total; i++) {
            Sismo e = TestSismo.sismoAleatorio();
            bdd.agregaRegistro(e);
        }
        String guardado = "";
        try {
            StringWriter swOut = new StringWriter();
            BufferedWriter out = new BufferedWriter(swOut);
            bdd.guarda(out);
            out.close();
            guardado = swOut.toString();
        } catch (IOException ioe) {
            Assert.fail();
        }
        String[] lineas = guardado.split("\n");
        Assert.assertTrue(lineas.length == total);
        Lista<Sismo> l = bdd.getRegistros();
        int c = 0;
        for (Sismo e : l) {
            String el = String.format("%s\t%d\t%4.1f\t%d\t%2.1f\t%s",
                                        e.getFecha(),
                                        e.getHeridos(),
                                        e.getProfundidad(),
                                        e.getReplicas(), 
                                        e.getMagnitud(), 
                                        e.getLocalidades());
            Assert.assertTrue(lineas[c++].equals(el));
        }
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#carga}.
     */
    @Test public void testCarga() {
        int ini = random.nextInt(1000000);
        String entrada = "";
        Sismo[] sismos = new Sismo[total];
        for (int i = 0; i < total; i++) {
            sismos[i] = TestSismo.sismoAleatorio(ini + i);
            entrada += String.format("%s\t%d\t%4.1f\t%d\t%2.1f\t%s\n",
                                    sismos[i].getFecha(),
                                    sismos[i].getHeridos(),
                                    sismos[i].getProfundidad(),
                                    sismos[i].getReplicas(), 
                                    sismos[i].getMagnitud(), 
                                    sismos[i].getLocalidades());
            bdd.agregaRegistro(sismos[i]);
        }
        int[] contador = { 0 };
        boolean[] llamado = { false };
        bdd.agregaEscucha((e, r1, r2) -> {
                if (e == EventoBaseDeDatos.BASE_LIMPIADA)
                    llamado[0] = true;
                if (e == EventoBaseDeDatos.REGISTRO_AGREGADO) {
                    contador[0] ++;
                    Assert.assertTrue(r1 != null);
                    Assert.assertTrue(r2 == null);
                }
            });
        try {
            StringReader srInt = new StringReader(entrada);
            BufferedReader in = new BufferedReader(srInt, 8192);
            bdd.carga(in);
            in.close();
        } catch (IOException ioe) {
            Assert.fail();
        }
        Lista<Sismo> l = bdd.getRegistros();
        Assert.assertTrue(l.getLongitud() == total);
        int c = 0;
        for (Sismo e : l)
            Assert.assertTrue(sismos[c++].equals(e));
        Assert.assertTrue(llamado[0]);
        Assert.assertTrue(contador[0] == total);
        contador[0] = 0;
        llamado[0] = false;
        entrada = String.format("%s\t%d\t%4.1f\t%d\t%2.1f\t%s\n",
                                sismos[0].getFecha(),
                                sismos[0].getHeridos(),
                                sismos[0].getProfundidad(),
                                sismos[0].getReplicas(), 
                                sismos[0].getMagnitud(), 
                                sismos[0].getLocalidades());
        entrada += " \n";
        entrada += String.format("%s\t%d\t%4.1f\t%d\t%2.1f\t%s\n",
                                sismos[1].getFecha(),
                                sismos[1].getHeridos(),
                                sismos[1].getProfundidad(),
                                sismos[1].getReplicas(), 
                                sismos[1].getMagnitud(), 
                                sismos[1].getLocalidades());
        try {
            StringReader srInt = new StringReader(entrada);
            BufferedReader in = new BufferedReader(srInt, 8192);
            bdd.carga(in);
            in.close();
        } catch (IOException ioe) {
            Assert.fail();
        }
        Assert.assertTrue(bdd.getNumRegistros() == 1);
        Assert.assertTrue(llamado[0]);
        Assert.assertTrue(contador[0] == 1);
        entrada = "";
        try {
            StringReader srInt = new StringReader(entrada);
            BufferedReader in = new BufferedReader(srInt, 8192);
            bdd.carga(in);
            in.close();
        } catch (IOException ioe) {
            Assert.fail();
        }
        Assert.assertTrue(bdd.getNumRegistros() == 0);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatosSismos#creaRegistro}.
     */
    @Test public void testCreaRegistro() {
        Sismo e = bdd.creaRegistro();
        Assert.assertTrue(e.getFecha() == null);
        Assert.assertTrue(e.getHeridos() == 0);
        Assert.assertTrue(e.getProfundidad() == 0.0);
        Assert.assertTrue(e.getReplicas() == 0);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatosSismos#buscaRegistros}.
     */
    @Test public void testBuscaRegistros() {
        Sismo[] sismos = new Sismo[total];
        int ini = 1000000 + random.nextInt(999999);
        for (int i = 0; i < total; i++) {
            Sismo e =  new Sismo(String.valueOf(ini+i),
                                           ini+i, (i * 10.0) / total, i, i, String.valueOf(ini+i));
            sismos[i] = e;
            bdd.agregaRegistro(e);
        }

        Sismo sismo;
        Lista<Sismo> l;
        int i;

        for (int k = 0; k < total/10 + 3; k++) {
            i = random.nextInt(total);
            sismo = sismos[i];

            String fecha = sismo.getFecha();
            l = bdd.buscaRegistros(CampoSismo.FECHA, fecha);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(sismo));
            for (Sismo e : l)
                Assert.assertTrue(e.getFecha().indexOf(fecha) > -1);
            int n = fecha.length();
            String bn = fecha.substring(random.nextInt(2),
                                        2 + random.nextInt(n-2));
            l = bdd.buscaRegistros(CampoSismo.FECHA, bn);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(sismo));
            for (Sismo e : l)
                Assert.assertTrue(e.getFecha().indexOf(bn) > -1);

            int heridos = sismo.getHeridos();
            l = bdd.buscaRegistros(CampoSismo.HERIDOS, heridos);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(sismo));
            for (Sismo e : l)
                Assert.assertTrue(e.getHeridos() >= heridos);
            int bc = heridos - 10;
            l = bdd.buscaRegistros(CampoSismo.HERIDOS, bc);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(sismo));
            for (Sismo e : l)
                Assert.assertTrue(e.getHeridos() >= bc);

            double profundidad = sismo.getProfundidad();
            l = bdd.buscaRegistros(CampoSismo.PROFUNDIDAD, profundidad);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(sismo));
            for (Sismo e : l)
                Assert.assertTrue(e.getProfundidad() >= profundidad);
            double bp = profundidad - 5.0;
            l = bdd.buscaRegistros(CampoSismo.PROFUNDIDAD, bp);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(sismo));
            for (Sismo e : l)
                Assert.assertTrue(e.getProfundidad() >= bp);

            int replicas = sismo.getReplicas();
            l = bdd.buscaRegistros(CampoSismo.REPLICAS, replicas);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(sismo));
            for (Sismo e : l)
                Assert.assertTrue(e.getReplicas() >= replicas);
            int be = replicas - 10;
            l = bdd.buscaRegistros(CampoSismo.REPLICAS, be);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(sismo));
            for (Sismo e : l)
                Assert.assertTrue(e.getReplicas() >= be);
        }

        l = bdd.buscaRegistros(CampoSismo.FECHA,
                            "xxx-fecha");
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoSismo.HERIDOS, 9123456);
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoSismo.PROFUNDIDAD, 97.12);
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoSismo.REPLICAS, 127);
        Assert.assertTrue(l.esVacia());

        l = bdd.buscaRegistros(CampoSismo.FECHA, "");
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoSismo.HERIDOS, Integer.MAX_VALUE);
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoSismo.PROFUNDIDAD, Double.MAX_VALUE);
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoSismo.REPLICAS, Integer.MAX_VALUE);
        Assert.assertTrue(l.esVacia());

        l = bdd.buscaRegistros(CampoSismo.FECHA, null);
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoSismo.HERIDOS, null);
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoSismo.PROFUNDIDAD, null);
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoSismo.REPLICAS, null);
        Assert.assertTrue(l.esVacia());

        try {
            l = bdd.buscaRegistros(null, null);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#agregaEscucha}.
     */
    @Test public void testAgregaEscucha() {
        int[] c = new int[total];
        for (int i = 0; i < total; i++) {
            final int j = i;
            bdd.agregaEscucha((e, r1, r2) -> c[j]++);
        }
        bdd.agregaRegistro(new Sismo("A", 1, 1, 1, 1, "A"));
        for (int i = 0; i < total; i++)
            Assert.assertTrue(c[i] == 1);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#eliminaEscucha}.
     */
    @Test public void testEliminaEscucha() {
        int[] c = new int[total];
        Lista<EscuchaBaseDeDatos<Sismo>> escuchas =
            new Lista<EscuchaBaseDeDatos<Sismo>>();
        for (int i = 0; i < total; i++) {
            final int j = i;
            EscuchaBaseDeDatos<Sismo> escucha = (e, r1, r2) -> c[j]++;
            bdd.agregaEscucha(escucha);
            escuchas.agregaFinal(escucha);
        }
        int i = 0;
        while (!escuchas.esVacia()) {
            bdd.agregaRegistro(TestSismo.sismoAleatorio(i++));
            EscuchaBaseDeDatos<Sismo> escucha = escuchas.eliminaPrimero();
            bdd.eliminaEscucha(escucha);
        }
        for (i = 0; i < total; i++)
            Assert.assertTrue(c[i] == i + 1);
    }
}
