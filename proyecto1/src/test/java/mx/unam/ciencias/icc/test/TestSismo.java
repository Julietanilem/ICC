package mx.unam.ciencias.icc.test;

import java.util.Random;
import mx.unam.ciencias.icc.CampoSismo;
import mx.unam.ciencias.icc.Sismo;
import mx.unam.ciencias.icc.Lista;
import mx.unam.ciencias.icc.ExcepcionLineaInvalida;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * Clase para pruebas unitarias de la clase {@link Sismo}.
 */
public class TestSismo {

    /** Expiración para que ninguna prueba tarde más de 5 segundos. */
    @Rule public Timeout expiracion = Timeout.seconds(5);

    /* Meses */
    private static final String[] MESES = {
        "Enero", "Febrero", "Marzo", "Abril", "Mayo",
        "Junio", "Julio", "Agosto", "Septiembre", "Octubre",
	"Noviembre", "Diciembre"
    };
    /*Entidades */
    private static final String[] ENTIDADES= {
    "Aguascalientes", "Baja California", "Baja California Sur", "Campeche", "Coahuila", "Colima", "Chiapas",
    "Chihuahua", "Durango", "Ciudad de México", "Guanajuato", "Guerrero", "Hidalgo", "Jalisco", "Estado de México",
    "Michoacán", "Morelos", "Nayarit", "Nuevo León", "Oaxaca", "Puebla", "Querétaro"," Quintana Roo", "San Luis Potosí",
    "Sinaloa", "Sonora", "Tabasco", "Tamaulipas", "Tlaxcala", "Veracruz", "Yucatán",  "Zacatecas"
    };

    /* Generador de números aleatorios. */
    private static Random random;

    /* Replica el comportamiento de {@link Sismo#formatoEntidades} para poder hacer prubas de seriación*/ 
    public static String formatoEntidades (Lista<String> entidades ){
        Lista<String>.Nodo n = entidades.getCabeza();
        StringBuffer sb  = new StringBuffer();
        while (n != null){
            sb.append (n.get().toString());
            n = n.getSiguiente();
            if (n !=null)
                sb.append(", ");
        }
        return sb.toString();
    }
    /**
     * Genera un fecha aleatoria.
     * @return un fecha aleatoria.
     */
    public static String fechaAleatoria() {
        int mes = random.nextInt(MESES.length);
        String m = MESES[mes];
        int maximo_dias = 0;
        if (m.equals("Noviembre") || m.equals("Abril") || m.equals("Enero") || m.equals("Septiembre"))
            maximo_dias = 30;
        else if (m.equals("Febrero"))
            maximo_dias = 28;
        else
            maximo_dias = 31;  
        String d = String.valueOf(random.nextInt(maximo_dias)+1);
        String a = String.valueOf(random.nextInt(2)+2020);
        return d + " de " + m + " de " + a;
    }

    /**
     * Genera un numero de muertes aleatorio.
     * @return un numero de muertes aleatorio.
     */
    public static int muertesAleatorias() {
        return random.nextInt(111);
    }

    /**
     * Genera una profundidad aleatoria.
     * @return una profundidad aleatoria.
     */
    public static double profundidadAleatoria() {
        return (random.nextInt(500)+1) / 1.0;
    }

    /**
     * Genera un número de replicas aleatorio.
     * @return un número de replicas aleatorio.
     */
    public static int replicasAleatorias() {
        return random.nextInt(15);
    }

    /**
     * Genera una magnitud aleatoria.
     * @return una magnitud  aleatoria.
     */
    public static double magnitudAleatoria() {
        return (random.nextInt(900)) / 100.0;
    }


    /**
     * Genera una lista de entidades aleatoria.
     * @return una lista de entidades aleatoria.
     */
    public static Lista<String> entidadesAleatorias() {
        int n_entidades = random.nextInt(6)+1;
        Lista<String> l = new Lista<>();
        int n = 0;
        for (int i=0; i<= n_entidades ;i++){
            n = random.nextInt(ENTIDADES.length);
            l.agregaFinal(ENTIDADES[n]);
        }
        return l;
    }
    /**
     * Genera un sismo aleatorio.
     * @return un sismo aleatorio.
     */
    public static Sismo sismoAleatorio() {
        return new Sismo(fechaAleatoria(),
                        muertesAleatorias(),
                        profundidadAleatoria(),
                        replicasAleatorias(),
                        magnitudAleatoria (),
                        entidadesAleatorias());
    }

    /**
     * Genera un sismo aleatorio con una magnitud dada.
     * @param magnitud la magnitud  del nuevo sismo.
     * @return un sismo aleatorio.
     */
    public static Sismo sismoAleatorio(int magnitud) {
        return new Sismo(fechaAleatoria(),
                        muertesAleatorias(),
                        profundidadAleatoria(),
                        replicasAleatorias(),
                        magnitud,
                        entidadesAleatorias());
    }

    /* El sismo. */
    private Sismo sismo;

    /**
     * Prueba unitaria para {@link
     * Sismo#Sismo(String,int,double,int, double, Lista<String>)}.
     */
    @Test public void testConstructor() {
        String fecha = fechaAleatoria();
        int muertes = muertesAleatorias();
        double profundidad = profundidadAleatoria();
        int replicas = replicasAleatorias();
        double magnitud = magnitudAleatoria();
        Lista<String>  entidades  = entidadesAleatorias();
        sismo = new Sismo(fecha, muertes, profundidad, replicas, magnitud, entidades);
        Assert.assertTrue(sismo.getFecha().equals(fecha));
        Assert.assertTrue(sismo.getMuertes() == muertes);
        Assert.assertTrue(sismo.getProfundidad() == profundidad);
        Assert.assertTrue(sismo.getMagnitud() == magnitud);
        Assert.assertTrue(sismo.getEntidades().equals(entidades));
    }

    /**
     * Prueba unitaria para {@link Sismo#getFecha}.
     */
    @Test public void testGetFecha() {
        String fecha = fechaAleatoria();
        int muertes = muertesAleatorias();
        double profundidad = profundidadAleatoria();
        int replicas = replicasAleatorias();
        double magnitud = magnitudAleatoria();
        Lista<String>  entidades  = entidadesAleatorias();
        sismo = new Sismo(fecha, muertes, profundidad, replicas, magnitud, entidades);
        Assert.assertTrue(sismo.getFecha().equals(fecha));
        Assert.assertFalse(sismo.getFecha().equals(fecha + " X"));
    }

    /**
     * Prueba unitaria para {@link Sismo#setFecha}.
     */
    @Test public void testSetFecha() {
	String fecha = fechaAleatoria();
        String nuevoFecha = fecha + "X";
        int muertes = muertesAleatorias();
        double profundidad = profundidadAleatoria();
        int replicas = replicasAleatorias();
        double magnitud = magnitudAleatoria();
        Lista<String>  entidades  = entidadesAleatorias();
        sismo = new Sismo(fecha, muertes, profundidad, replicas, magnitud, entidades);
        Assert.assertTrue(sismo.getFecha().equals(fecha));
        Assert.assertFalse(sismo.getFecha().equals(nuevoFecha));
        sismo.setFecha(nuevoFecha);
        Assert.assertFalse(sismo.getFecha().equals(fecha));
        Assert.assertTrue(sismo.getFecha().equals(nuevoFecha));
    }

    /**
     * Prueba unitaria para {@link Sismo#getMuertes}.
     */
    @Test public void testGetMuertes() {
        String fecha = fechaAleatoria();
        int muertes = muertesAleatorias();
        double profundidad = profundidadAleatoria();
        int replicas = replicasAleatorias();
        double magnitud = magnitudAleatoria();
        Lista<String>  entidades  = entidadesAleatorias();
        sismo = new Sismo(fecha, muertes, profundidad, replicas, magnitud, entidades);
        Assert.assertTrue(sismo.getMuertes() == muertes);
        Assert.assertFalse(sismo.getMuertes() == muertes + 10);
    }

    /**
     * Prueba unitaria para {@link Sismo#setMuertes}.
     */
    @Test public void testSetMuertes() {
        String fecha = fechaAleatoria();
        int muertes = muertesAleatorias();
        int nuevasMuertes = muertes + 10;
        double profundidad = profundidadAleatoria();
        int replicas = replicasAleatorias();
        double magnitud = magnitudAleatoria();
        Lista<String>  entidades  = entidadesAleatorias();
        sismo = new Sismo(fecha, muertes, profundidad, replicas, magnitud, entidades);
        Assert.assertTrue(sismo.getMuertes() == muertes);
        Assert.assertFalse(sismo.getMuertes() == muertes + 10);
        sismo.setMuertes(nuevasMuertes);
        Assert.assertFalse(sismo.getMuertes() == muertes);
        Assert.assertTrue(sismo.getMuertes() == nuevasMuertes);
    }

    /**
     * Prueba unitaria para {@link Sismo#getProfundidad}.
     */
    @Test public void testGetProfundidad() {
        String fecha = fechaAleatoria();
        int muertes = muertesAleatorias();
        double profundidad = profundidadAleatoria();
        int replicas = replicasAleatorias();
        double magnitud = magnitudAleatoria();
        Lista<String>  entidades  = entidadesAleatorias();
        sismo = new Sismo(fecha, muertes, profundidad, replicas, magnitud, entidades);
        Assert.assertTrue(sismo.getProfundidad() == profundidad);
        Assert.assertFalse(sismo.getProfundidad() == profundidad + 1.0);
    }

    /**
     * Prueba unitaria para {@link Sismo#setProfundidad}.
     */
    @Test public void testSetProfundidad() {
        String fecha = fechaAleatoria();
        int muertes = muertesAleatorias();
        double profundidad = profundidadAleatoria();
        double nuevaProfundidad = profundidad + 1.0;
        int replicas = replicasAleatorias();
        double magnitud = magnitudAleatoria();
        Lista<String>  entidades  = entidadesAleatorias();
        sismo = new Sismo(fecha, muertes, profundidad, replicas, magnitud, entidades);  
        Assert.assertTrue(sismo.getProfundidad() == profundidad);
        Assert.assertFalse(sismo.getProfundidad() == nuevaProfundidad);
        sismo.setProfundidad(nuevaProfundidad);
        Assert.assertFalse(sismo.getProfundidad() == profundidad);
        Assert.assertTrue(sismo.getProfundidad() == nuevaProfundidad);
    }

    /**
     * Prueba unitaria para {@link Sismo#getReplicas}.
     */
    @Test public void testGetReplicas() {
        String fecha = fechaAleatoria();
        int muertes = muertesAleatorias();
        double profundidad = profundidadAleatoria();
        int replicas = replicasAleatorias();
        double magnitud = magnitudAleatoria();
        Lista<String>  entidades  = entidadesAleatorias();
        sismo = new Sismo(fecha, muertes, profundidad, replicas, magnitud, entidades);
        Assert.assertTrue(sismo.getReplicas() == replicas);
        Assert.assertFalse(sismo.getReplicas() == replicas + 5);
    }

    /**
     * Prueba unitaria para {@link Sismo#setReplicas}.
     */
    @Test public void testSetReplicas() {
        String fecha = fechaAleatoria();
        int muertes = muertesAleatorias();
        double profundidad = profundidadAleatoria();
        int replicas = replicasAleatorias();
        double magnitud = magnitudAleatoria();
        Lista<String>  entidades  = entidadesAleatorias();
        sismo = new Sismo(fecha, muertes, profundidad, replicas, magnitud, entidades); 
        int nuevasReplicas = replicas + 50;
        Assert.assertTrue(sismo.getReplicas() == replicas);
        Assert.assertFalse(sismo.getReplicas() == nuevasReplicas);
        sismo.setReplicas(nuevasReplicas);
        Assert.assertFalse(sismo.getReplicas() == replicas);
        Assert.assertTrue(sismo.getReplicas() == nuevasReplicas);
    }


    /**
     * Prueba unitaria para {@link Sismo#getMagnitud}.
     */
    @Test public void testGetMagnitud() {
        String fecha = fechaAleatoria();
        int muertes = muertesAleatorias();
        double profundidad = profundidadAleatoria();
        int replicas = replicasAleatorias();
        double magnitud = magnitudAleatoria();
        Lista<String>  entidades  = entidadesAleatorias();
        sismo = new Sismo(fecha, muertes, profundidad, replicas, magnitud, entidades);
        Assert.assertTrue(sismo.getMagnitud() == magnitud);
        Assert.assertFalse(sismo.getMagnitud() == magnitud + 1);
    }

    /**
     * Prueba unitaria para {@link Sismo#setMagnitud}.
     */
    @Test public void testSetMagnitud() {
        String fecha = fechaAleatoria();
        int muertes = muertesAleatorias();
        double profundidad = profundidadAleatoria();
        int replicas = replicasAleatorias();
        double magnitud = magnitudAleatoria();
        Lista<String>  entidades  = entidadesAleatorias();
        sismo = new Sismo(fecha, muertes, profundidad, replicas, magnitud, entidades); 
        double nuevaMagnitud = magnitud + 1.3;
        Assert.assertTrue(sismo.getMagnitud() == magnitud);
        Assert.assertFalse(sismo.getMagnitud() == nuevaMagnitud);
        sismo.setMagnitud(nuevaMagnitud);
        Assert.assertFalse(sismo.getMagnitud() == magnitud);
        Assert.assertTrue(sismo.getMagnitud() == nuevaMagnitud);
    }

/**
     * Prueba unitaria para {@link Sismo#getEntidades}.
     */
    @Test public void testGetEntidades() {
        String fecha = fechaAleatoria();
        int muertes = muertesAleatorias();
        double profundidad = profundidadAleatoria();
        int replicas = replicasAleatorias();
        double magnitud = magnitudAleatoria();
        Lista<String>  entidades  = entidadesAleatorias();
        sismo = new Sismo(fecha, muertes, profundidad, replicas, magnitud, entidades);
        Assert.assertTrue(sismo.getEntidades().equals(entidades));
        Assert.assertFalse(sismo.getEntidades().equals(new Lista<String>()));
    }

    /**
     * Prueba unitaria para {@link Sismo#setEntidades}.
     */
    @Test public void testSetEntidades() {
        String fecha = fechaAleatoria();
        int muertes = muertesAleatorias();
        double profundidad = profundidadAleatoria();
        int replicas = replicasAleatorias();
        double magnitud = magnitudAleatoria();
        Lista<String>  entidades  = entidadesAleatorias();
        sismo = new Sismo(fecha, muertes, profundidad, replicas, magnitud, entidades); 
        Lista<String> nuevasEntidades = entidadesAleatorias();
        Assert.assertTrue(sismo.getEntidades().equals(entidades));
        Assert.assertFalse(sismo.getEntidades().equals(nuevasEntidades));
        sismo.setEntidades(nuevasEntidades);
        Assert.assertFalse(sismo.getEntidades().equals(entidades));
        Assert.assertTrue(sismo.getEntidades().equals(nuevasEntidades));
    }

    /**
     * Prueba unitaria para {@link Sismo#toString}.
     */
    @Test public void testToString() {
        String fecha = fechaAleatoria();
        int muertes = muertesAleatorias();
        double profundidad = profundidadAleatoria();
        int replicas = replicasAleatorias();
        double magnitud = magnitudAleatoria();
        Lista<String>  entidades  = entidadesAleatorias();
        sismo = new Sismo(fecha, muertes, profundidad, replicas, magnitud, entidades); 
        String cadena = String.format("Fecha   : %s\n" +
                                    "Muertes   : %d\n" +
                                    "Profundidad : %2.2f\n" +
                                    "Replicas     : %d\n"+
                                    "Magnitud : %2.2f\n"+
                                    "Entidades : %s",
                                    fecha, muertes, 
                                    profundidad, replicas, 
                                    magnitud, formatoEntidades(entidades));
        Assert.assertTrue(sismo.toString().equals(cadena));
        muertes = 100;
        profundidad = 0.99;
        sismo.setMuertes(muertes);
        sismo.setProfundidad(profundidad);
        cadena = String.format("Fecha   : %s\n" +
                                "Muertes   : %d\n" +
                                "Profundidad : %2.2f\n" +   
                                "Replicas     : %d\n"+
                                "Magnitud : %2.2f\n"+
                                "Entidades : %s",
                                fecha, muertes,
                                profundidad, replicas, 
                                magnitud, formatoEntidades(entidades));
        Assert.assertTrue(sismo.toString().equals(cadena));
    }

    /**
     * Prueba unitaria para {@link Sismo#equals}.
     */
    @Test public void testEquals() {
        String fecha = fechaAleatoria();
        int muertes = muertesAleatorias();
        double profundidad = profundidadAleatoria();
        int replicas = replicasAleatorias();
        double magnitud = magnitudAleatoria();
        Lista<String>  entidades  = entidadesAleatorias();
        sismo = new Sismo(fecha, muertes, profundidad, replicas, magnitud, entidades);
        Sismo igual =  new Sismo(fecha, muertes, profundidad, replicas, magnitud, entidades);
        Assert.assertTrue(sismo.equals(igual));
        String otraFecha = fecha + " 12:00";
        int otrasMuertes = muertes + 1;
        double otraProfundidad = (profundidad != 0.0) ? profundidad / 10.0 : 10.0;
        int otrasReplicas = replicas + 1;
        double otraMagnitud = magnitud +2.9;
        Sismo distinto =
            new Sismo(otraFecha, muertes, profundidad, replicas, magnitud, entidades);
        Assert.assertFalse(sismo.equals(distinto));
        distinto = new Sismo(fecha, otrasMuertes, profundidad, replicas,  magnitud, entidades);
        Assert.assertFalse(sismo.equals(distinto));
        distinto = new Sismo(fecha, muertes, otraProfundidad, replicas,  magnitud, entidades);
        Assert.assertFalse(sismo.equals(distinto));
        distinto = new Sismo(fecha, muertes, profundidad, otrasReplicas,  magnitud, entidades);
        Assert.assertFalse(sismo.equals(distinto));
        distinto = new Sismo(fecha, muertes, profundidad, replicas,  otraMagnitud, entidades);
        Assert.assertFalse(sismo.equals(distinto));
        entidades.agregaFinal(ENTIDADES[random.nextInt(entidades.getLongitud())]) ;
        distinto = new Sismo(fecha, muertes, profundidad, replicas,  magnitud, entidades);
        Assert.assertFalse(sismo.equals(distinto));
        distinto = new Sismo(otraFecha, otrasMuertes,otraProfundidad, 
                                otrasReplicas, otraMagnitud, entidades);
        Assert.assertFalse(sismo.equals(distinto));
        Assert.assertFalse(sismo.equals("Una cadena"));
        Assert.assertFalse(sismo.equals(null));
    }

    /**
     * Prueba unitaria para {@link Sismo#seria}.
     */
    @Test public void testSeria() {
        String fecha = fechaAleatoria();
        int muertes = muertesAleatorias();
        double profundidad = profundidadAleatoria();
        int replicas = replicasAleatorias();
        double magnitud = magnitudAleatoria();
        Lista<String>  entidades  = entidadesAleatorias();
        sismo = new Sismo(fecha, muertes, profundidad, replicas, magnitud, entidades);
        String linea = String.format("%s\t%d\t%2.2f\t%d\t%2.2f\t%s\n", 
        fecha, muertes, profundidad, replicas, magnitud, formatoEntidades(entidades));
        Assert.assertTrue(sismo.seria().equals(linea));
    }

    /**
     * Prueba unitaria para {@link Sismo#deseria}.
     */
    @Test public void testDeseria() {
        sismo = new Sismo(null, 0, 0.0, 0, 0.0, null);
        String fecha = fechaAleatoria();
        int muertes = muertesAleatorias();
        double profundidad = profundidadAleatoria();
        int replicas = replicasAleatorias();
        double magnitud = magnitudAleatoria();
        Lista<String>  entidades  = entidadesAleatorias();
        String linea = String.format("%s\t%d\t%2.2f\t%d\t%2.2f\t%s\n", 
        fecha, muertes, profundidad, replicas, magnitud, formatoEntidades(entidades));
        try {
            sismo.deseria(linea);
        }catch (ExcepcionLineaInvalida eli) {
            Assert.fail();
        }
	
        Assert.assertTrue(sismo.getFecha().equals(fecha));
        Assert.assertTrue(sismo.getMuertes() == muertes);
        Assert.assertTrue(sismo.getProfundidad() == profundidad);
        Assert.assertTrue(sismo.getReplicas() == replicas);
        Assert.assertTrue(sismo.getMagnitud() == magnitud);
        Assert.assertTrue(sismo.getEntidades().equals(entidades));
        String[] invalidas = {"", " ", "\t", "  ", "\t\t",
                                " \t", "\t ", "\n", "a\ta\ta",
                                "a\ta\ta\ta"};
	
        for (int i = 0; i < invalidas.length; i++) {
            linea = invalidas[i];
            try {
                sismo.deseria(linea);
                Assert.fail();
            } catch (ExcepcionLineaInvalida eli) {}
        }
    }

    /**
     * Prueba unitaria para {@link Sismo#actualiza}.
     */
    @Test public void testActualiza() { 
        sismo = new Sismo("A", 1, 1, 1, 1,entidadesAleatorias() );
        Sismo e = new Sismo("B", 2, 2, 2, 2, entidadesAleatorias());
        Assert.assertFalse(sismo == e);
        Assert.assertFalse(sismo.equals(e));
        sismo.actualiza(e);
        Assert.assertFalse(sismo == e);
        Assert.assertTrue(sismo.equals(e));
        Assert.assertTrue(sismo.getFecha().equals("B"));
        try {
            sismo.actualiza(null);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
    }

    /**
     * Prueba unitaria para {@link Sismo#casa}.
     */
    @Test public void testCasa() {
        String fecha = fechaAleatoria();
        int muertes = muertesAleatorias();
        double profundidad = profundidadAleatoria();
        int replicas = replicasAleatorias();
        double magnitud = magnitudAleatoria();
        Lista<String>  entidades  = entidadesAleatorias();
        sismo = new Sismo(fecha, muertes, profundidad, replicas, magnitud, entidades);
        String n = sismo.getFecha();
        int m = sismo.getFecha().length();
        Assert.assertTrue(sismo.casa(CampoSismo.FECHA, n));
        n = sismo.getFecha().substring(0, m/2);
        Assert.assertTrue(sismo.casa(CampoSismo.FECHA, n));
        n = sismo.getFecha().substring(m/2, m);
        Assert.assertTrue(sismo.casa(CampoSismo.FECHA, n));
        n = sismo.getFecha().substring(m/3, 2*(m/3));
        Assert.assertTrue(sismo.casa(CampoSismo.FECHA, n));
        Assert.assertFalse(sismo.casa(CampoSismo.FECHA, ""));
        Assert.assertFalse(sismo.casa(CampoSismo.FECHA, "XXX"));
        Assert.assertFalse(sismo.casa(CampoSismo.FECHA, 1000));
        Assert.assertFalse(sismo.casa(CampoSismo.FECHA, null));

        int c = sismo.getMuertes();
        Assert.assertTrue(sismo.casa(CampoSismo.MUERTES, c));
        c = sismo.getMuertes() - 100;
        Assert.assertTrue(sismo.casa(CampoSismo.MUERTES, c));
        c = sismo.getMuertes() + 100;
        Assert.assertFalse(sismo.casa(CampoSismo.MUERTES, c));
        Assert.assertFalse(sismo.casa(CampoSismo.MUERTES, "XXX"));
        Assert.assertFalse(sismo.casa(CampoSismo.MUERTES, null));

        double p = sismo.getProfundidad();
        Assert.assertTrue(sismo.casa(CampoSismo.PROFUNDIDAD, p));
        p = sismo.getProfundidad() - 5.0;
        Assert.assertTrue(sismo.casa(CampoSismo.PROFUNDIDAD, p));
        p = sismo.getProfundidad() + 5.0;
        Assert.assertFalse(sismo.casa(CampoSismo.PROFUNDIDAD, p));
        Assert.assertFalse(sismo.casa(CampoSismo.PROFUNDIDAD, "XXX"));
        Assert.assertFalse(sismo.casa(CampoSismo.PROFUNDIDAD, null));

        int e = sismo.getReplicas();
        Assert.assertTrue(sismo.casa(CampoSismo.REPLICAS, e));
        e = sismo.getReplicas() - 10;
        Assert.assertTrue(sismo.casa(CampoSismo.REPLICAS, e));
        e = sismo.getReplicas() + 10;
        Assert.assertFalse(sismo.casa(CampoSismo.REPLICAS, e));
        Assert.assertFalse(sismo.casa(CampoSismo.REPLICAS, "XXX"));
        Assert.assertFalse(sismo.casa(CampoSismo.REPLICAS, null));
        
        double ma = sismo.getMagnitud();
        Assert.assertTrue(sismo.casa(CampoSismo.MAGNITUD, ma));
        ma = sismo.getMagnitud() - 5.0;
        Assert.assertTrue(sismo.casa(CampoSismo.MAGNITUD, ma));
        ma = sismo.getMagnitud() + 5.0;
        Assert.assertFalse(sismo.casa(CampoSismo.MAGNITUD, ma));
        Assert.assertFalse(sismo.casa(CampoSismo.MAGNITUD, "XXX"));
        Assert.assertFalse(sismo.casa(CampoSismo.MAGNITUD, null));

        String entidad = "";
        for  (int i =0; i< sismo.getEntidades().getLongitud(); i++){
            entidad =  sismo.getEntidades().get (i);
            Assert.assertTrue(sismo.casa(CampoSismo.ENTIDADES, entidad));
        }
        Assert.assertFalse(sismo.casa(CampoSismo.ENTIDADES, ""));
        Assert.assertFalse(sismo.casa(CampoSismo.ENTIDADES, 1000));
        Assert.assertFalse(sismo.casa(CampoSismo.ENTIDADES, null));
	
        try {
            sismo.casa(null, null);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
    }

    /* Inicializa el generador de números aleatorios. */
    static {
        random = new Random();
    }
}
