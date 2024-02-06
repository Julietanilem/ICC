package mx.unam.ciencias.icc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Clase para aplicaciones de la base de datos de sismos.
 */
public class Aplicacion {

    /* Modo de la aplicación. */
    private enum Modo {
        /* Modo para guardar. */
        GUARDA(1),
        /* Modo para cargar. */
        CARGA(2);

        /* Código de terminación. */
        private int codigo;

        /* Constructor. */
        private Modo(int codigo) {
            this.codigo = codigo;
        }

        /* Regresa el código. */
        public int getCodigo() {
            return codigo;
        }

        /* Regresa el modo de la bandera. */
        public static Modo getModo(String bandera) {
            switch (bandera) {
            case "-g": return GUARDA;
            case "-c": return CARGA;
            default: throw new IllegalArgumentException(
                "Bandera inválida: " + bandera);
            }
        }
    }

    /* La base de datos. */
    private BaseDeDatosSismos bdd;
    /* La ruta del archivo. */
    private String ruta;
    /* El modo de la aplicación. */
    private Modo modo;

    /**
     * Define el estado inicial de la aplicación.
     * @param bandera la bandera de modo.
     * @param ruta la ruta del archivo.
     * @throws IllegalArgumentException si la bandera no es "-r" o "-g".
     */
    public Aplicacion(String bandera, String ruta) {
        modo = Modo.getModo(bandera);
        this.ruta = ruta;
        bdd = new BaseDeDatosSismos();
    }
    /**
     * Ejecuta la aplicación.
     */
    public void ejecuta() {
        switch (modo) {
        case GUARDA:
            guarda();
            break;
        case CARGA:
            carga();
            break;
        }
    }
    /* Modo de guarda de la aplicación. */
    private void guarda() {
        Scanner sc = new Scanner(System.in);
        sc.useDelimiter("\n");
        verificaSalida(sc);
        agregaSismos(sc);
        sc.close();
        try {
            BufferedWriter out =
                new BufferedWriter(
                    new OutputStreamWriter(
                        new FileOutputStream(ruta)));
            bdd.guarda(out);
            out.close();
        } catch (IOException ioe) {
            System.err.printf("No pude guardar en el archivo \"%s\".\n",
                            ruta);
            System.exit(modo.getCodigo());
        }
    }
    /* Verifica que la salida no exista o le permite salir al usuario. */
    private void verificaSalida(Scanner sc) {
        File archivo = new File(ruta);
        if (archivo.exists()) {
            System.out.printf("El archivo \"%s\" ya existe y será " +
                            "reescrito.\n¿Desea continuar? (s/n): ", ruta);
            String r = sc.next();
            if (!r.equals("s")) {
                sc.close();
                System.exit(0);
            }
        }
    }
    /* Agrega sismos a la base de datos mientras el usuario lo desee. */
    private void agregaSismos(Scanner sc) {
        System.out.println("\nDeje el fecha en blanco para " +
                            "parar la captura de sismos.");
        Sismo e = null;
        do {
            try {
                e = getSismo(sc);
                if (e != null)
                    bdd.agregaRegistro(e);
            } catch (InputMismatchException ime) {
                System.err.printf("\nEntrada inválida. Se descartará " +
                                "este sismo.\n");
                sc.next(); // Purgamos la última entrada del usuario.
                continue;
            }
        } while (e != null);
    }
    /* Obtiene un sismo de la línea de comandos. */
    private Sismo getSismo(Scanner sc) {
        System.out.printf("\nFecha   : ");
        String fecha = sc.next();
        if (fecha.equals(""))
            return null;
        System.out.printf("Muertes   : ");
        int muertes = sc.nextInt();
        System.out.printf("Profundidad : ");
        double profundidad = sc.nextDouble();
        System.out.printf("Réplicas     : ");
        int replicas = sc.nextInt();
        System.out.printf("Magnitud : ");
        double magnitud = sc.nextDouble();
        Lista <String> entidades = getEntidades (sc);
        if (entidades.esVacia())
            throw new  InputMismatchException ();
        return new Sismo(fecha, muertes, profundidad, replicas, magnitud, entidades);
    }
    /* Obtiene las entidades afectadas mientras lo deseé el usuario*/
    private Lista<String> getEntidades (Scanner sc){
        System.out.println("\nDeje la entidad en blanco para parar la captura de estados.");
        Lista<String> e = new Lista<>();
        int  bandera = 1;
        do {
            try {
                System.out.printf("Entidades : ");
                String entidad = sc.next();
                if (entidad.equals("")) 
                    bandera  =  0;
                else 
                    e.agregaFinal(entidad);
            } catch (InputMismatchException ime) {
                System.err.printf("\n Entidad inválida. Se descartará.\n");
                sc.next(); // Purgamos la última entrada del usuario.
                continue;
            }
        } while (bandera != 0);
        
	return e;
    }
    /* Modo de carga de la aplicación. */
    private void carga() {
        try {
            BufferedReader in =
                new BufferedReader(
                    new InputStreamReader(
                        new FileInputStream(ruta)));
            bdd.carga(in);
            in.close();
        } catch (IOException ioe) {
            System.err.printf("No pude cargar del archivo \"%s\".\n",
                            ruta);
            System.exit(modo.getCodigo());
        }
        Scanner sc = new Scanner(System.in);
        sc.useDelimiter("\n");
        busca(sc);
        sc.close();
    }
    /* Hace la búsqueda. */
    private void busca(Scanner sc) {
        System.out.println("\nDeje el campo en blanco para " +
                        "parar la búsqueda de sismos.");
        String c = "X";
        while (!(c = getCampo(sc)).equals("")) {
            Lista<Sismo> l;
            try {
                l = getResultados(c, sc);
            } catch (ExcepcionOpcionInvalida epi) {
                System.out.printf("%s\n", epi.getMessage());
                continue;
            } catch (InputMismatchException ime) {
                System.out.printf("\nValor inválido. Búsqueda descartada.\n");
                sc.next(); // Purgamos la entrada.
                continue;
            }
            Lista<Sismo>.Nodo nodo = l.getCabeza();
            String m = nodo != null ? "" :
                "\nCero registros casan la búsqueda.";
            System.out.println(m);
            while (nodo != null) {
                System.out.printf("%s\n\n", nodo.get().toString());
                nodo = nodo.getSiguiente();
            }
        }
    }
    /* Regresa el campo. */
    private String getCampo(Scanner sc) {
        System.out.printf("\n¿Por qué campo quiere buscar? (mu/ma/f/r/p/e): ");
        return sc.next();
    }
    /* Regresa los resultados de la búsqueda. */
    private Lista<Sismo> getResultados(String c, Scanner sc) {
        System.out.println();
        switch (c) {
        case "f": return bdd.buscaRegistros(CampoSismo.FECHA,
                                            getValorFecha(sc));
        case "mu": return bdd.buscaRegistros(CampoSismo.MUERTES,
                                            getValorMuertes(sc));
        case "p": return bdd.buscaRegistros(CampoSismo.PROFUNDIDAD,
                                            getValorProfundidad(sc));
        case "r": return bdd.buscaRegistros(CampoSismo.REPLICAS,
                                            getValorReplicas(sc));
        case "ma": return bdd.buscaRegistros(CampoSismo.MAGNITUD,
                                            getValorMagnitud(sc));
        case "e": return bdd.buscaRegistros(CampoSismo.ENTIDADES,
                                            getValorEntidades(sc));
        default:
            String m = String.format("El campo '%s' es inválido.", c);
            throw new ExcepcionOpcionInvalida(m);
        }
    }
    /* Regresa el valor a buscar para fecha. */
    private String getValorFecha(Scanner sc) {
        System.out.printf("La fecha debe contener: ");
        return sc.next();
    }
    /* Regresa el valor a buscar para el número de muertes. */
    private Integer getValorMuertes(Scanner sc) {
        System.out.printf("El número de muertes debe ser mayor o igual a: ");
        return Integer.valueOf(sc.nextInt());
    }
    /* Regresa el valor a buscar para la profundidad. */
    private Double getValorProfundidad(Scanner sc) {
        System.out.printf("La profundidad debe ser mayor o igual a: ");
        return Double.valueOf(sc.nextDouble());
    }
      /* Regresa el valor a buscar para la magnitud. */
    private Double getValorMagnitud(Scanner sc) {
        System.out.printf("La magnitud debe ser mayor o igual a: ");
        return Double.valueOf(sc.nextDouble());
    }
    /* Regresa el valor a buscar para el número de réplicas. */
    private Integer getValorReplicas(Scanner sc) {
        System.out.printf("La replicas debe ser mayor o igual a: ");
        return Integer.valueOf(sc.nextInt());
    }
     /* Regresa el valor a buscar para la entidad. */
    private String getValorEntidades(Scanner sc) {
        System.out.printf("Entre las entidades se debe encontrar: ");
        return sc.next();
    }
}
