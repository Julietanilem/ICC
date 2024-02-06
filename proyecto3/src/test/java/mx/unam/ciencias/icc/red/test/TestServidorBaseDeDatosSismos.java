package mx.unam.ciencias.icc.red.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Random;
import mx.unam.ciencias.icc.BaseDeDatos;
import mx.unam.ciencias.icc.BaseDeDatosSismos;
import mx.unam.ciencias.icc.CampoSismo;
import mx.unam.ciencias.icc.Sismo;
import mx.unam.ciencias.icc.ExcepcionLineaInvalida;
import mx.unam.ciencias.icc.Lista;
import mx.unam.ciencias.icc.red.Mensaje;
import mx.unam.ciencias.icc.red.ServidorBaseDeDatos;
import mx.unam.ciencias.icc.red.ServidorBaseDeDatosSismos;
import mx.unam.ciencias.icc.test.TestSismo;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.Timeout;

/**
 * Clase para pruebas unitarias de la clase {@link
 * ServidorBaseDeDatosSismos}.
 */
public class TestServidorBaseDeDatosSismos {

    /** Expiración para que ninguna prueba tarde más de 5 segundos. */
    @Rule public Timeout expiracion = Timeout.seconds(5);
    /** Directorio para archivos temporales. */
    @Rule public TemporaryFolder directorio = new TemporaryFolder();

    /* Clase interna para manejar una conexión de pruebas. */
    private class Cliente {

        /* El enchufe. */
        private Socket enchufe;
        /* La entrada. */
        private BufferedReader in;
        /* La salida. */
        private BufferedWriter out;

        /* Construye una nueva conexión en el puerto. */
        private Cliente(int puerto) {
            try {
                enchufe = new Socket("localhost", puerto);
                in =
                    new BufferedReader(
                        new InputStreamReader(
                            enchufe.getInputStream()));
                out =
                    new BufferedWriter(
                        new OutputStreamWriter(
                            enchufe.getOutputStream()));
            } catch (IOException ioe) {
                Assert.fail();
            }
        }

        /* Envía un mensaje por la conexión. */
        private void enviaMensaje(Mensaje mensaje) {
            try {
                out.write(mensaje.toString());
                out.newLine();
                out.flush();
                UtilRed.espera(10);
            } catch (IOException ioe) {
                Assert.fail();
            }
        }

        /* Envía un sismo por la conexión. */
        public void enviaSismo(Sismo sismo) {
            try {
                out.write(sismo.seria());
                out.flush();
                UtilRed.espera(10);
            } catch (IOException ioe) {
                Assert.fail();
            }
        }

        /* Envía una cadena por la conexión. */
        public void enviaCadena(String cadena) {
            try {
                out.write(cadena);
                out.newLine();
                out.flush();
                UtilRed.espera(10);
            } catch (IOException ioe) {
                Assert.fail();
            }
        }

        /* Envía una nueva línea por la conexión. */
        public void enviaLinea() {
            try {
                out.newLine();
                out.flush();
                UtilRed.espera(10);
            } catch (IOException ioe) {
                Assert.fail();
            }
        }

        /* Recibe un mensaje por la conexión. */
        public Mensaje recibeMensaje() {
            return Mensaje.getMensaje(recibeCadena());
        }

        /* Recibe una cadena por la conexión. */
        public String recibeCadena() {
            try {
                return in.readLine();
            } catch (IOException ioe) {
                Assert.fail();
            }
            /* Inalcanzable. */
            return null;
        }

        /* Recibe una base de datos por la conexión. */
        public BaseDeDatosSismos recibeBaseDeDatos() {
            BaseDeDatosSismos bdd = new BaseDeDatosSismos();
            try {
                bdd.carga(in);
                return bdd;
            } catch (IOException ioe) {
                Assert.fail();
            }
            /* Inalcanzable. */
            return null;
        }

        /* Recibe un sismo por la conexión. */
        public Sismo recibeSismo() {
            Sismo e = new Sismo(null, 0, 0, 0, 0, null);
            try {
                e.deseria(in.readLine());
                return e;
            } catch (IOException | ExcepcionLineaInvalida ex) {
                Assert.fail();
            }
            /* Inalcanzable. */
            return null;
        }
    }

    /* Compara dos sismos. */
    private int compara(Sismo e1, Sismo e2) {
        if (!e1.getFecha().equals(e2.getFecha()))
            return e1.getFecha().compareTo(e2.getFecha());
        if (e1.getHeridos() != e2.getHeridos())
            return e1.getHeridos() - e2.getHeridos();
        if (e1.getProfundidad() != e2.getProfundidad())
            return (e1.getProfundidad() < e2.getProfundidad()) ? -1 : 1;
        return e1.getReplicas() - e2.getReplicas();
    }

    /* Valida el archivo de la base de datos. */
    private void validaArchivo(BaseDeDatosSismos bdd) {
        BaseDeDatosSismos bdd2 = new BaseDeDatosSismos();
        try {
            BufferedReader in =
                new BufferedReader(
                    new InputStreamReader(
                        new FileInputStream(archivo)));
            bdd2.carga(in);
            in.close();
        } catch (IOException ioe) {
            Assert.fail();
        }
        Lista<Sismo> l1 = bdd.getRegistros();
        l1 = l1.mergeSort((e1, e2) -> compara(e1, e2));
        Lista<Sismo> l2 = bdd2.getRegistros();
        l2 = l2.mergeSort((e1, e2) -> compara(e1, e2));
        Assert.assertTrue(l1.equals(l2));
    }

    /* Generador de números aleatorios. */
    private Random random;
    /* Servidor de base de datos. */
    private ServidorBaseDeDatosSismos sbdd;
    /* El total de sismos. */
    private int total;
    /* Los sismos. */
    private Sismo[] sismos;
    /* El archivo temporal de la base de datos. */
    private String archivo;
    /* El puerto. */
    private int puerto;

    /**
     * Crea un generador de números aleatorios para cada prueba y una base de
     * datos de sismos.
     */
    public TestServidorBaseDeDatosSismos() {
        random = new Random();
        total = 10 + random.nextInt(100);
        puerto = obtenPuerto();
    }

    /* Obtiene el puerto. */
    private int obtenPuerto() {
        int p = -1;
        while (p < 1024) {
            try {
                p = 1024 + random.nextInt(64500);
                ServerSocket s = new ServerSocket(p);
                s.close();
            } catch (BindException be) {
                p = -1;
                UtilRed.espera(10);
            } catch (IOException ioe) {
                Assert.fail();
            }
        }
        return p;
    }

    /**
     * Método que se ejecuta antes de cada prueba unitaria; crea el archivo de
     * la base de datos y hace servir el servidor.
     */
    @Before public void arma() {
        try {
            sismos = new Sismo[total];
            BaseDeDatosSismos bdd = new BaseDeDatosSismos();
            for (int i = 0; i < total; i++) {
                sismos[i] = TestSismo.sismoAleatorio();
                bdd.agregaRegistro(sismos[i]);
            }
            File f = null;
            String s = String.format("test-base-de-datos-%04d.db",
                                    random.nextInt(9999));
            f = directorio.newFile(s);
            archivo = f.getAbsolutePath();
            BufferedWriter out =
                new BufferedWriter(
                    new OutputStreamWriter(
                        new FileOutputStream(archivo)));
            bdd.guarda(out);
            out.close();
            sbdd = new ServidorBaseDeDatosSismos(puerto, archivo);
            new Thread(() -> sbdd.sirve()).start();
        } catch (IOException ioe) {
            Assert.fail();
        }
    }

    /**
     * Método que se ejecuta despué de cada prueba unitaria; elimina el archivo
     * de la base de datos y detiene el servidor. Esto hace un método
     * testSirveDetenerServidor innecesario.
     */
    @After public void desarma() {
        File f = new File(archivo);
        f.delete();
        Cliente c = new Cliente(puerto);
        c.enviaMensaje(Mensaje.DETENER_SERVICIO);
        Assert.assertTrue(c.recibeCadena() == null);
    }

    /* Crea una nueva conexión, enviando y recibiendo un eco para probarla de
     * inmediato. */
    private Cliente nuevoCliente() {
        Cliente c = new Cliente(puerto);
        c.enviaMensaje(Mensaje.ECO);
        Assert.assertTrue(c.recibeMensaje() == Mensaje.ECO);
        return c;
    }

    /**
     * Prueba unitaria para el mensaje {@link Mensaje#BASE_DE_DATOS} en el
     * método {@link ServidorBaseDeDatos#sirve}.
     */
    @Test public void testSirveBaseDeDatos() {
        Cliente c = nuevoCliente();
        c.enviaMensaje(Mensaje.BASE_DE_DATOS);
        Assert.assertTrue(c.recibeMensaje() == Mensaje.BASE_DE_DATOS);
        BaseDeDatosSismos bdd = c.recibeBaseDeDatos();
        Lista<Sismo> l = bdd.getRegistros();
        Assert.assertTrue(l.getLongitud() == total);
        int i = 0;
        for (Sismo e : l)
            Assert.assertTrue(e.equals(sismos[i++]));
    }

    /**
     * Prueba unitaria para el mensaje {@link Mensaje#REGISTRO_AGREGADO}
     * en el método {@link ServidorBaseDeDatos#sirve}.
     */
    @Test public void testSirveRegistroAgregado() {
        Cliente c1 = nuevoCliente();
        Cliente c2 = nuevoCliente();

        Sismo sismo = new Sismo("A", 1, 1, 1, 1, "A");
        c1.enviaMensaje(Mensaje.REGISTRO_AGREGADO);
        c1.enviaSismo(sismo);

        c1.enviaMensaje(Mensaje.BASE_DE_DATOS);
        Assert.assertTrue(c1.recibeMensaje() == Mensaje.BASE_DE_DATOS);
        BaseDeDatosSismos bdd = c1.recibeBaseDeDatos();
        Lista<Sismo> l = bdd.getRegistros();
        Assert.assertTrue(l.contiene(sismo));

        Assert.assertTrue(c2.recibeMensaje() == Mensaje.REGISTRO_AGREGADO);
        Sismo t = c2.recibeSismo();
        Assert.assertTrue(t.equals(sismo));
        validaArchivo(bdd);
    }

    /**
     * Prueba unitaria para el mensaje {@link Mensaje#REGISTRO_ELIMINADO}
     * en el método {@link ServidorBaseDeDatos#sirve}.
     */
    @Test public void testSirveRegistroEliminado() {
        Cliente c1 = nuevoCliente();
        Cliente c2 = nuevoCliente();
        Sismo sismo = sismos[random.nextInt(total)];
        c1.enviaMensaje(Mensaje.REGISTRO_ELIMINADO);
        c1.enviaSismo(sismo);

        c1.enviaMensaje(Mensaje.BASE_DE_DATOS);
        Assert.assertTrue(c1.recibeMensaje() == Mensaje.BASE_DE_DATOS);
        BaseDeDatosSismos bdd = c1.recibeBaseDeDatos();
        Lista<Sismo> l = bdd.getRegistros();
        Assert.assertFalse(l.contiene(sismo));
        Assert.assertTrue(c2.recibeMensaje() == Mensaje.REGISTRO_ELIMINADO);
        Sismo t = c2.recibeSismo();
        Assert.assertTrue(t.equals(sismo));
        validaArchivo(bdd);
    }

    /**
     * Prueba unitaria para el mensaje {@link Mensaje#REGISTRO_MODIFICADO}
     * en el método {@link ServidorBaseDeDatos#sirve}.
     */
    @Test public void testSirveRegistroModificado() {
        Cliente c1 = nuevoCliente();
        Cliente c2 = nuevoCliente();
        Sismo e = sismos[random.nextInt(total)];
        Sismo m = new Sismo(null, 0, 0, 0, 0, null);
        m.actualiza(e);
        m.setFecha("A");
        c1.enviaMensaje(Mensaje.REGISTRO_MODIFICADO);
        c1.enviaSismo(e);
        c1.enviaSismo(m);
        c1.enviaMensaje(Mensaje.BASE_DE_DATOS);
        Assert.assertTrue(c1.recibeMensaje() == Mensaje.BASE_DE_DATOS);
        BaseDeDatosSismos bdd = c1.recibeBaseDeDatos();
        Lista<Sismo> l = bdd.getRegistros();
        Assert.assertFalse(l.contiene(e));
        Assert.assertTrue(l.contiene(m));
        Assert.assertTrue(c2.recibeMensaje() == Mensaje.REGISTRO_MODIFICADO);
        Sismo t = new Sismo(null, 0, 0, 0, 0, null);
        t = c2.recibeSismo();
        Assert.assertTrue(t.equals(e));
        t = c2.recibeSismo();
        Assert.assertTrue(t.equals(m));
        UtilRed.espera(10);
        validaArchivo(bdd);
    }

    /**
     * Prueba unitaria para el mensaje {@link Mensaje#DESCONECTAR}
     * en el método {@link ServidorBaseDeDatos#sirve}.
     */
    @Test public void testSirveDesconectar() {
        Cliente c = nuevoCliente();
        c.enviaMensaje(Mensaje.DESCONECTAR);
        Assert.assertTrue(c.recibeCadena() == null);
        c = nuevoCliente();
        c.enviaMensaje(Mensaje.ECO);
        Assert.assertTrue(c.recibeMensaje() == Mensaje.ECO);
    }

    /**
     * Prueba unitaria para el mensaje {@link Mensaje#ECO} en el método
     * {@link ServidorBaseDeDatos#sirve}.
     */
    @Test public void testSirveEco() {
        Cliente c = nuevoCliente();
        c.enviaMensaje(Mensaje.ECO);
        Assert.assertTrue(c.recibeMensaje() == Mensaje.ECO);
    }

    /**
     * Prueba unitaria para el mensaje {@link Mensaje#INVALIDO} en
     * el método {@link ServidorBaseDeDatos#sirve}.
     */
    @Test public void testSirveMensajeInvalido() {
        Cliente c = nuevoCliente();
        c.enviaMensaje(Mensaje.INVALIDO);
        Assert.assertTrue(c.recibeCadena() == null);
    }

    /**
     * Prueba unitaria para {@link ServidorBaseDeDatosSismos#agregaEscucha}
     * al realizar conexiones.
     */
    @Test public void testAgregaEscucha() {
        UtilRed.espera(10);
        Lista<String> mensajes = new Lista<String>();
        sbdd.agregaEscucha((f, a) -> {
                String s = a.length > 0 ? String.format(f, a) : f;
                mensajes.agregaFinal(s);
            });
        Cliente c = nuevoCliente();
        c.enviaMensaje(Mensaje.BASE_DE_DATOS);
        c.recibeMensaje();
        BaseDeDatosSismos bd = c.recibeBaseDeDatos();
        c.enviaMensaje(Mensaje.REGISTRO_AGREGADO);
        Sismo e = UtilRed.sismoAleatorio(total);
        c.enviaSismo(e);
        c.enviaMensaje(Mensaje.REGISTRO_ELIMINADO);
        c.enviaSismo(e);
        c.enviaMensaje(Mensaje.INVALIDO);
        UtilRed.espera(10);
        Iterator<String> i = mensajes.iterator();
        Assert.assertTrue(i.hasNext());
        String m = i.next();
        Assert.assertTrue(m.equals("Conexión recibida de: localhost."));
        Assert.assertTrue(i.hasNext());
        m = i.next();
        Assert.assertTrue(m.startsWith("Serie de conexión: "));
        m = m.replace("Serie de conexión: ", "").replace(".", "");
        int serie = Integer.parseInt(m);
        Assert.assertTrue(serie > 0);
        Assert.assertTrue(i.hasNext());
        m = i.next();
        String r = String.format("Solicitud de eco de %d.", serie);
        Assert.assertTrue(m.equals(r));
        Assert.assertTrue(i.hasNext());
        m = i.next();
        r = String.format("Base de datos pedida por %d.", serie);
        Assert.assertTrue(m.equals(r));
        Assert.assertTrue(i.hasNext());
        m = i.next();
        r = String.format("Registro agregado por %d.", serie);
        Assert.assertTrue(m.equals(r));
        Assert.assertTrue(i.hasNext());
        m = i.next();
        r = String.format("Guardando base de datos en %s.", archivo);
        Assert.assertTrue(m.equals(r));
        Assert.assertTrue(i.hasNext());
        m = i.next();
        Assert.assertTrue(m.equals("Base de datos guardada."));
        Assert.assertTrue(i.hasNext());
        m = i.next();
        r = String.format("Registro eliminado por %d.", serie);
        Assert.assertTrue(m.equals(r));
        Assert.assertTrue(i.hasNext());
        m = i.next();
        r = String.format("Guardando base de datos en %s.", archivo);
        Assert.assertTrue(m.equals(r));
        Assert.assertTrue(i.hasNext());
        m = i.next();
        Assert.assertTrue(m.equals("Base de datos guardada."));
        Assert.assertTrue(i.hasNext());
        m = i.next();
        r = String.format("Desconectando la conexión %d: Mensaje inválido.",
                        serie);
        Assert.assertTrue(m.equals(r));
        Assert.assertTrue(i.hasNext());
        m = i.next();
        r = String.format("La conexión %d ha sido desconectada.", serie);
        Assert.assertTrue(m.equals(r));
        Assert.assertFalse(i.hasNext());
        Assert.assertTrue(c.recibeCadena() == null);
        sbdd.limpiaEscuchas();
    }

    /**
     * Prueba unitaria para {@link
     * ServidorBaseDeDatosSismos#creaBaseDeDatos}.
     */
    @Test public void testCreaBaseDeDatos() {
        BaseDeDatos<Sismo, CampoSismo> bdd = sbdd.creaBaseDeDatos();
        Assert.assertTrue(bdd instanceof BaseDeDatosSismos);
        Assert.assertTrue(bdd.getNumRegistros() == 0);
    }
}
