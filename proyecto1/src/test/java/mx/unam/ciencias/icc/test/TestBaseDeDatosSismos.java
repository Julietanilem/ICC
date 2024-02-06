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
        Lista<Sismo>.Nodo nodo = l.getCabeza();
        while (nodo != null) {
            Assert.assertTrue(sismos[c++].equals(nodo.get()));
            nodo = nodo.getSiguiente();
        }
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
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#limpia}.
     */
    @Test public void testLimpia() {
        for (int i = 0; i < total; i++) {
            Sismo e = TestSismo.sismoAleatorio();
            bdd.agregaRegistro(e);
        }
        Lista<Sismo> registros = bdd.getRegistros();
        Assert.assertFalse(registros.esVacia());
        Assert.assertFalse(registros.getLongitud() == 0);
        bdd.limpia();
        registros = bdd.getRegistros();
        Assert.assertTrue(registros.esVacia());
        Assert.assertTrue(registros.getLongitud() == 0);
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
        Lista<Sismo>.Nodo nodo = l.getCabeza();
        while (nodo != null) {
            Sismo e = nodo.get();
            String el =  String.format("%s\t%d\t%2.2f\t%d\t%2.2f\t%s", 
                            e.getFecha(), e.getMuertes(), e.getProfundidad(), 
                            e.getReplicas(), e.getMagnitud(), TestSismo.formatoEntidades(e.getEntidades()));
            Assert.assertTrue(lineas[c++].equals(el));
            nodo = nodo.getSiguiente();
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
            entrada +=  String.format("%s\t%d\t%2.2f\t%d\t%2.2f\t%s\n", 
                            sismos[i].getFecha(), sismos[i].getMuertes(), sismos[i].getProfundidad(), 
                            sismos[i].getReplicas(), sismos[i].getMagnitud(), TestSismo.formatoEntidades(sismos[i].getEntidades()));
            bdd.agregaRegistro(sismos[i]);
        }
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
        Lista<Sismo>.Nodo nodo = l.getCabeza();
        while (nodo != null) {
            Assert.assertTrue(sismos[c++].equals(nodo.get()));
            nodo = nodo.getSiguiente();
        }
        entrada = String.format("%s\t%d\t%2.2f\t%d\t%2.2f\t%s\n",
                                sismos[0].getFecha(),
                                sismos[0].getMuertes(),
                                sismos[0].getProfundidad(),
                                sismos[0].getReplicas(),
                                sismos[0].getMagnitud(),
                                TestSismo.formatoEntidades(sismos[0].getEntidades()));
        entrada += " \n";
        entrada += String.format("%s\t%d\t%2.2f\t%d\t%2.2f\t%s\n",
                                sismos[1].getFecha(),
                                sismos[1].getMuertes(),
                                sismos[1].getProfundidad(),
                                sismos[1].getReplicas(),
                                sismos[1].getMagnitud(),
                                TestSismo.formatoEntidades(sismos[1].getEntidades()));
        try {
            StringReader srInt = new StringReader(entrada);
            BufferedReader in = new BufferedReader(srInt, 8192);
            bdd.carga(in);
            in.close();
        } catch (IOException ioe) {
            Assert.fail();
        }
        Assert.assertTrue(bdd.getNumRegistros() == 1);
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
        Assert.assertTrue(e.getMuertes() == 0);
        Assert.assertTrue(e.getProfundidad() == 0.0);
        Assert.assertTrue(e.getReplicas() == 0);
        Assert.assertTrue(e.getMagnitud() == 0.0 );
        Assert.assertTrue(e.getEntidades().esVacia());
	
    }

    /**
     * Prueba unitaria para {@link BaseDeDatosSismos#buscaRegistros}.
     */
    @Test public void testBuscaRegistros() {
        Sismo[] sismos = new Sismo[total];
        int ini = 1000000 + random.nextInt(999999);
        for (int i = 0; i < total; i++) {
            Sismo e =  new Sismo(String.valueOf(ini+i),
				 ini+i, (i * 10.0) / total, i, (i * 10.0) / total, TestSismo.entidadesAleatorias() );
            sismos[i] = e;
            bdd.agregaRegistro(e);
        }

        Sismo sismo;
        Lista<Sismo> l;
        Lista<Sismo>.Nodo nodo;
        int i;

        for (int k = 0; k < total/10 + 3; k++) {
            i = random.nextInt(total);
            sismo = sismos[i];

            String fecha = sismo.getFecha();
            l = bdd.buscaRegistros(CampoSismo.FECHA, fecha);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(sismo));
            nodo = l.getCabeza();
            while (nodo != null) {
                Sismo e = nodo.get();
                Assert.assertTrue(e.getFecha().indexOf(fecha) > -1);
                nodo = nodo.getSiguiente();
            }
            int n = fecha.length();
            String bn = fecha.substring(random.nextInt(2),
                                        2 + random.nextInt(n-2));
            l = bdd.buscaRegistros(CampoSismo.FECHA, bn);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(sismo));
            nodo = l.getCabeza();
            while (nodo != null) {
                Sismo e = nodo.get();
                Assert.assertTrue(e.getFecha().indexOf(bn) > -1);
                nodo = nodo.getSiguiente();
            }

            int muertes = sismo.getMuertes();
            l = bdd.buscaRegistros(CampoSismo.MUERTES, muertes);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(sismo));
            nodo = l.getCabeza();
            while (nodo != null) {
                Sismo e = nodo.get();
                Assert.assertTrue(e.getMuertes() >= muertes);
                nodo = nodo.getSiguiente();
            }
            int bc = muertes - 10;
            l = bdd.buscaRegistros(CampoSismo.MUERTES, bc);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(sismo));
            nodo = l.getCabeza();
            while (nodo != null) {
                Sismo e = nodo.get();
                Assert.assertTrue(e.getMuertes() >= bc);
                nodo = nodo.getSiguiente();
            }

            double profundidad = sismo.getProfundidad();
            l = bdd.buscaRegistros(CampoSismo.PROFUNDIDAD, profundidad);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(sismo));
            nodo = l.getCabeza();
            while (nodo != null) {
                Sismo e = nodo.get();
                Assert.assertTrue(e.getProfundidad() >= profundidad);
                nodo = nodo.getSiguiente();
            }
            double bp = profundidad - 5.0;
            l = bdd.buscaRegistros(CampoSismo.PROFUNDIDAD, bp);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(sismo));
            nodo = l.getCabeza();
            while (nodo != null) {
                Sismo e = nodo.get();
                Assert.assertTrue(e.getProfundidad() >= bp);
                nodo = nodo.getSiguiente();
            }

            int replicas = sismo.getReplicas();
            l = bdd.buscaRegistros(CampoSismo.REPLICAS, replicas);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(sismo));
            nodo = l.getCabeza();
            while (nodo != null) {
                Sismo e = nodo.get();
                Assert.assertTrue(e.getReplicas() >= replicas);
                nodo = nodo.getSiguiente();
            }
            int be = replicas - 10;
            l = bdd.buscaRegistros(CampoSismo.REPLICAS, be);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(sismo));
            nodo = l.getCabeza();
            while (nodo != null) {
                Sismo e = nodo.get();
                Assert.assertTrue(e.getReplicas() >= be);
                nodo = nodo.getSiguiente();
            }
            double magnitud = sismo.getMagnitud();
            l = bdd.buscaRegistros(CampoSismo.MAGNITUD, magnitud);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(sismo));
            nodo = l.getCabeza();
            while (nodo != null) {
                Sismo e = nodo.get();
                Assert.assertTrue(e.getMagnitud() >= magnitud);
                nodo = nodo.getSiguiente();
            }
            double bm = magnitud - 5.0;
            l = bdd.buscaRegistros(CampoSismo.MAGNITUD, bm);
            Assert.assertTrue(l.getLongitud() > 0);
            Assert.assertTrue(l.contiene(sismo));
            nodo = l.getCabeza();
            while (nodo != null) {
                Sismo e = nodo.get();
                Assert.assertTrue(e.getMagnitud() >= bm);
                nodo = nodo.getSiguiente();
            }

            //Probar que funciones con todas las entidades en la lista
            Lista<String> entidades = sismo.getEntidades();
            Lista<String>.Nodo nodoE = entidades.getCabeza();
            while(nodoE!=null){
                l = bdd.buscaRegistros(CampoSismo.ENTIDADES, nodoE.get());
                Assert.assertTrue(l.getLongitud() > 0);
                Assert.assertTrue(l.contiene(sismo));
                nodoE = nodoE.getSiguiente();
            }
            //Probar que todo en la lista contenga una de las entidades al menos
            nodo = l.getCabeza();
            Boolean contiene = false;
            while (nodo != null) {
                Sismo e = nodo.get();
                nodoE = e.getEntidades().getCabeza();
                while(nodoE != null){
                    if (e.getEntidades().contiene(nodoE.get())) 
                        contiene = true;
                    nodoE = nodoE.getSiguiente();
                }
                Assert.assertTrue(contiene);
                nodo = nodo.getSiguiente();
                contiene = false;
            }
        }
        l = bdd.buscaRegistros(CampoSismo.FECHA,"Proyecto");
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoSismo.MUERTES, 9123456);
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoSismo.PROFUNDIDAD, 100000.12);
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoSismo.REPLICAS, 1344);
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoSismo.MAGNITUD, 20000.123);
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoSismo.ENTIDADES, "ICC");
        Assert.assertTrue(l.esVacia());

        l = bdd.buscaRegistros(CampoSismo.FECHA, "");
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoSismo.MUERTES, Integer.MAX_VALUE);
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoSismo.PROFUNDIDAD, Double.MAX_VALUE);
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoSismo.REPLICAS, Integer.MAX_VALUE);
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoSismo.MAGNITUD, Double.MAX_VALUE);
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoSismo.ENTIDADES, "");
        Assert.assertTrue(l.esVacia());
	
        l = bdd.buscaRegistros(CampoSismo.FECHA, null);
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoSismo.MUERTES, null);
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoSismo.PROFUNDIDAD, null);
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoSismo.REPLICAS, null);
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoSismo.MAGNITUD,null);
        Assert.assertTrue(l.esVacia());
        l = bdd.buscaRegistros(CampoSismo.ENTIDADES, null);
        Assert.assertTrue(l.esVacia());
        try {
            l = bdd.buscaRegistros(null, null);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
    }
}
