package mx.unam.ciencias.icc.red;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import mx.unam.ciencias.icc.BaseDeDatos;
import mx.unam.ciencias.icc.Lista;
import mx.unam.ciencias.icc.Registro;

/**
 * Clase abstracta para servidores de bases de datos genéricas.
 */
public abstract class ServidorBaseDeDatos<R extends Registro<R, ?>> {

    /* La base de datos. */
    private BaseDeDatos<R, ?> bdd;
    /* La ruta donde cargar/guardar la base de datos. */
    private String ruta;
    /* El servidor de enchufes. */
    private ServerSocket servidor;
    /* El puerto. */
    private int puerto;
    /* Lista con las conexiones. */
    private Lista<Conexion<R>> conexiones;
    /* Bandera de continuación. */
    private boolean continuaEjecucion;
    /* Escuchas del servidor. */
    private Lista<EscuchaServidor> escuchas;

    /**
     * Crea un nuevo servidor usando la ruta recibida para poblar la base de
     * datos.
     * @param puerto el puerto dónde escuchar por conexiones.
     * @param ruta la ruta en el disco del cual cargar/guardar la base de
     *             datos. Puede ser <code>null</code>, en cuyo caso se usará el
     *             fecha por omisión <code>base-de-datos.bd</code>.
     * @throws IOException si ocurre un error de entrada o salida.
     */
    public ServidorBaseDeDatos(int puerto, String ruta)
    throws IOException {
        this.puerto = puerto;
        this.ruta = (ruta == null) ? "base-de-datos.bd" : ruta;
        servidor = new ServerSocket(puerto);
        conexiones = new Lista<>();
        escuchas = new Lista<>();
        bdd = creaBaseDeDatos();
        carga();
    }

    /**
     * Comienza a escuchar por conexiones de clientes.
     */
    public void sirve() {
        continuaEjecucion = true;
        anotaMensaje("Escuchando en el puerto: %d.", puerto);
        while (continuaEjecucion){
            try {
                Socket enchufe = servidor.accept();
                Conexion<R> conexion = new Conexion<R>(bdd, enchufe);
                anotaMensaje("Conexión recibida de: %s.", enchufe.getInetAddress().getCanonicalHostName());
                anotaMensaje("Serie de conexión: %d.", conexion.getSerie());
                conexion.agregaEscucha((c, m) -> mensajeRecibido(c, m));
                new Thread(() -> conexion.recibeMensajes()).start();
                synchronized(conexiones) {
                    conexiones.agregaFinal(conexion);
                }
            } catch (IOException ioe) {
                if (continuaEjecucion)
                    anotaMensaje("Error al recibir una conexión...");
            }
        }
        anotaMensaje("La ejecución del servidor ha terminado.");
    }

    /**
     * Agrega un escucha de servidor.
     * @param escucha el escucha a agregar.
     */
    public void agregaEscucha(EscuchaServidor escucha) {
        escuchas.agregaFinal(escucha);
    }

    /**
     * Limpia todos los escuchas del servidor.
     */
    public void limpiaEscuchas() {
        escuchas.limpia();
        
    }

    /* Carga la base de datos del disco duro. */
    private void carga() {
        try {
            anotaMensaje("Cargando base de datos de %s.", ruta);
            BufferedReader in = new BufferedReader(
                                    new InputStreamReader(
                                        new FileInputStream(ruta)));
            bdd.carga(in);
            in.close(); 
            anotaMensaje("Base de datos cargada exitosamente de %s.", ruta);
        } catch (IOException ioe) {
            anotaMensaje("Ocurrió un error al tratar de cargar %s.", ruta);
            anotaMensaje("La base de datos estará inicialmente vacía.");
        }
    }

    /* Guarda la base de datos en el disco duro. */
    private synchronized void guarda() {
        try {
            anotaMensaje("Guardando base de datos en %s.", ruta);
            BufferedWriter out = new BufferedWriter(
                                    new OutputStreamWriter(
                                        new FileOutputStream(ruta)));
            bdd.guarda(out);
            out.close();
            anotaMensaje("Base de datos guardada.");
        } catch (IOException ioe) {
            anotaMensaje("Ocurrió un error al guardar la base de datos.");
        }
    }

    /* Recibe los mensajes de la conexión. */
    private void mensajeRecibido(Conexion<R> conexion, Mensaje mensaje) {
        if(!conexion.isActiva()) return;
        switch(mensaje){
            case BASE_DE_DATOS:
                baseDeDatos(conexion);
                break;
            case REGISTRO_AGREGADO:
                registroAlterado(conexion, Mensaje.REGISTRO_AGREGADO);
                break;
            case REGISTRO_ELIMINADO:
                registroAlterado(conexion, Mensaje.REGISTRO_ELIMINADO);
                break;
            case REGISTRO_MODIFICADO:
                registroModificado(conexion);
                break;
            case DESCONECTAR:
                desconectar(conexion);
                break;
            case DETENER_SERVICIO:
                detenerServicio();
                break;
            case GUARDA:
                guarda();
                break;
            case ECO:
                eco(conexion);
                break;
            case INVALIDO:
                error(conexion, "Mensaje inválido.");
                break;
        }
    }

    /* Maneja el mensaje BASE_DE_DATOS. */
    private void baseDeDatos(Conexion<R> conexion) {
        try {
            conexion.enviaMensaje(Mensaje.BASE_DE_DATOS);
            conexion.enviaBaseDeDatos();
        } catch (IOException ioe) {
            error(conexion, "Error enviando la base de datos.");
        }
        anotaMensaje("Base de datos pedida por %d.", conexion.getSerie());
    }

    /* Maneja los mensajes REGISTRO_AGREGADO y REGISTRO_ELIMINADO. */
    private void registroAlterado(Conexion<R> conexion, Mensaje mensaje) {
        try {
            R registro = conexion.recibeRegistro();
            if (mensaje == Mensaje.REGISTRO_AGREGADO)
                agregaRegistro(registro);
            else
                eliminaRegistro(registro);              
            for(Conexion<R> con : conexiones){
                if (con == conexion) continue;
                try {
                    con.enviaMensaje(mensaje);
                    con.enviaRegistro(registro);
                } catch (IOException e) {
                        error(conexion, "Error enviando registro.");
                }
            }
        } catch(IOException ioe) {
            error(conexion, "Error recibiendo registro.");
            return;
        }
        if (mensaje == Mensaje.REGISTRO_AGREGADO)
            anotaMensaje("Registro agregado por %d.", conexion.getSerie());
        else
            anotaMensaje("Registro eliminado por %d.", conexion.getSerie());
        guarda();
        
    }

    /* Maneja el mensaje REGISTRO_MODIFICADO. */
    private void registroModificado(Conexion<R> conexion) {
        try{
            R registro1 = conexion.recibeRegistro();
            R registro2 = conexion.recibeRegistro();
            modificaRegistro(registro1, registro2);
            for(Conexion<R> con : conexiones){
                if(con == conexion)  continue;
                try {
                    con.enviaMensaje(Mensaje.REGISTRO_MODIFICADO);
                    con.enviaRegistro(registro1);
                    con.enviaRegistro(registro2);
                } catch (IOException e) {
                    error(conexion, "Error enviando registro.");
                }
            }
        } catch(IOException ioe) {
            error(conexion, "Error recibiendo registros.");
            return;
        }
        anotaMensaje("Registro modificado por %d.", conexion.getSerie());
        guarda();
    }

    /* Maneja el mensaje DESCONECTAR. */
    private void desconectar(Conexion<R> conexion) {
        anotaMensaje("Solicitud de desconexión de %d.", conexion.getSerie());
        desconecta(conexion);
    }

    /* Maneja el mensaje DETENER_SERVICIO. */
    private void detenerServicio() {
        anotaMensaje("Solicitud para detener el servicio.");
        continuaEjecucion = false;
        for(Conexion<R> con : conexiones)
            desconecta(con);
        try {
            servidor.close();
        } catch(IOException ioe){}
    }

    /* Maneja el mensaje ECO. */
    private void eco(Conexion<R> conexion) {
        anotaMensaje("Solicitud de eco de %d.", conexion.getSerie());
        try {
            conexion.enviaMensaje(Mensaje.ECO);
        } catch(IOException ioe){
            error(conexion, "Error enviando eco.");
        }
    }

    /* Imprime un mensaje a los escuchas y desconecta la conexión. */
    private void error(Conexion<R> conexion, String mensaje) {
        anotaMensaje("Desconectando la conexión %d: %s", conexion.getSerie(), mensaje);
        desconecta(conexion);
    }

    /* Desconecta la conexión. */
    private void desconecta(Conexion<R> conexion) {
        conexion.desconecta();
        synchronized(conexiones){
            conexiones.elimina(conexion);
        }
        anotaMensaje(String.format("La conexión %d ha sido desconectada.", conexion.getSerie()));
        
    }

    /* Agrega el registro a la base de datos. */
    private synchronized void agregaRegistro(R registro) {
        bdd.agregaRegistro(registro);
    }

    /* Elimina el registro de la base de datos. */
    private synchronized void eliminaRegistro(R registro) {
        bdd.eliminaRegistro(registro);
    }

    /* Modifica el registro en la base de datos. */
    private synchronized void modificaRegistro(R registro1, R registro2) {
        bdd.modificaRegistro(registro1, registro2);
    }

    /* Procesa los mensajes de todos los escuchas. */
    private void anotaMensaje(String formato, Object ... argumentos) {
        for(EscuchaServidor escucha : escuchas)
            escucha.procesaMensaje(formato, argumentos);
    }

    /**
     * Crea la base de datos concreta.
     * @return la base de datos concreta.
     */
    public abstract BaseDeDatos<R, ?> creaBaseDeDatos();
}
