package mx.unam.ciencias.icc.test;

import java.util.Random;
import mx.unam.ciencias.icc.CampoSismo;
import mx.unam.ciencias.icc.Sismo;
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
     * Genera un numero de heridos aleatorio.
     * @return un numero de heridos aleatorio.
     */
    public static int heridosAleatorios() {
        return random.nextInt(111)+1;
    }

    /**
     * Genera una profundidad aleatoria.
     * @return una profundidad aleatoria.
     */
    public static double profundidadAleatoria() {
        return random.nextInt(6000000) + 500 / 10.0 ;
    }

    /**
     * Genera un número de replicas aleatorio.
     * @return un número de replicas aleatorio.
     */
    public static int replicasAleatorias() {
        return random.nextInt(100)+1;
    }

    /**
     * Genera una magnitud aleatoria.
     * @return una magnitud  aleatoria.
     */
    public static double magnitudAleatoria() {
        return random.nextInt(100) / 10.0;
    }


    /**
     * Genera una lista de localidades aleatoria.
     * @return una lista de localidades aleatoria.
     */
    public static String  localidadesAleatorias() {
        int n_entidades = random.nextInt(6)+1;
        StringBuffer sb = new StringBuffer();
        int n = 0;
        boolean primero = true;
        for (int i=0; i<= n_entidades ;i++){
            n = random.nextInt(ENTIDADES.length);
            sb.append(ENTIDADES[n]);
            if(primero) {
                primero = false;
                continue;
            }
            sb.append(",");
        }
        return sb.toString();
    }

    /**
     * Genera un sismo aleatorio.
     * @return un sismo aleatorio.
     */
    public static Sismo sismoAleatorio() {
        return new Sismo(fechaAleatoria(),
                            heridosAleatorios(),
                            profundidadAleatoria(),
                            replicasAleatorias(), 
                            magnitudAleatoria(), 
                            localidadesAleatorias());
    }

    /**
     * Genera un sismo aleatorio con un número de heridos dado.
     * @param heridos el número de heridos del nuevo sismo.
     * @return un sismo aleatorio.
     */
    public static Sismo sismoAleatorio(int heridos) {
        return new Sismo(fechaAleatoria(),
                            heridos,
                            profundidadAleatoria(),
                            replicasAleatorias(), magnitudAleatoria(), localidadesAleatorias());
    }

    /* El sismo. */
    private Sismo sismo;

    /**
     * Prueba unitaria para {@link
     * Sismo#Sismo(String,int,double,int, double, String)}.
     */
    @Test public void testConstructor() {
        String fecha = fechaAleatoria();
        int heridos = heridosAleatorios();
        double profundidad = profundidadAleatoria();
        int replicas = replicasAleatorias();
        double magnitud = magnitudAleatoria();
        String localidades = localidadesAleatorias();
        sismo = new Sismo(fecha, heridos, profundidad, replicas, magnitud, localidades);
        Assert.assertTrue(sismo.getFecha().equals(fecha));
        Assert.assertTrue(sismo.getHeridos() == heridos);
        Assert.assertTrue(sismo.getProfundidad() == profundidad);
        Assert.assertTrue(sismo.getReplicas() == replicas);
        Assert.assertTrue(sismo.getMagnitud() == magnitud);
        Assert.assertTrue(sismo.getLocalidades().equals(localidades));
    }

    /**
     * Prueba unitaria para {@link Sismo#getFecha}.
     */
    @Test public void testGetFecha() {
        String fecha = fechaAleatoria();
        int heridos = heridosAleatorios();
        double profundidad = profundidadAleatoria();
        int replicas = replicasAleatorias();
        double magnitud = magnitudAleatoria();
        String localidades = localidadesAleatorias();
        sismo = new Sismo(fecha, heridos, profundidad, replicas, magnitud, localidades);
        Assert.assertTrue(sismo.getFecha().equals(fecha));
        Assert.assertFalse(sismo.getFecha().equals(fecha + " X"));
    }

    /**
     * Prueba unitaria para {@link Sismo#setFecha}.
     */
    @Test public void testSetFecha() {
        String fecha = fechaAleatoria();
        String nuevaFecha = fecha + " X";
        int heridos = heridosAleatorios();
        double profundidad = profundidadAleatoria();
        int replicas = replicasAleatorias();
        double magnitud = magnitudAleatoria();
        String localidades = localidadesAleatorias();
        sismo = new Sismo(fecha, heridos, profundidad, replicas, magnitud, localidades);
        Assert.assertTrue(sismo.getFecha().equals(fecha));
        Assert.assertFalse(sismo.getFecha().equals(nuevaFecha));
        sismo.setFecha(nuevaFecha);
        Assert.assertFalse(sismo.getFecha().equals(fecha));
        Assert.assertTrue(sismo.getFecha().equals(nuevaFecha));
    }

    /**
     * Prueba unitaria para {@link Sismo#fechaProperty}.
     */
    @Test public void testFechaProperty() {
        String fecha = fechaAleatoria();
        int heridos = heridosAleatorios();
        double profundidad = profundidadAleatoria();
        int replicas = replicasAleatorias();
          double magnitud = magnitudAleatoria();
        String localidades = localidadesAleatorias();
        sismo = new Sismo(fecha, heridos, profundidad, replicas, magnitud, localidades);
        Assert.assertTrue(sismo.fechaProperty().get().equals(fecha));
    }

    /**
     * Prueba unitaria para {@link Sismo#getHeridos}.
     */
    @Test public void testGetHeridos() {
        String fecha = fechaAleatoria();
        int heridos = heridosAleatorios();
        double profundidad = profundidadAleatoria();
        int replicas = replicasAleatorias();
        double magnitud = magnitudAleatoria();
        String localidades = localidadesAleatorias();
        sismo = new Sismo(fecha, heridos, profundidad, replicas, magnitud, localidades);
        Assert.assertTrue(sismo.getHeridos() == heridos);
        Assert.assertFalse(sismo.getHeridos() == heridos + 100);
    }

    /**
     * Prueba unitaria para {@link Sismo#setHeridos}.
     */
    @Test public void testSetHeridos() {
        String fecha = fechaAleatoria();
        int heridos = heridosAleatorios();
        int nuevosHeridos = heridos + 100;
        double profundidad = profundidadAleatoria();
        int replicas = replicasAleatorias();
        double magnitud = magnitudAleatoria();
        String localidades = localidadesAleatorias();
        sismo = new Sismo(fecha, heridos, profundidad, replicas, magnitud, localidades);
        Assert.assertTrue(sismo.getHeridos() == heridos);
        Assert.assertFalse(sismo.getHeridos() == heridos + 100);
        sismo.setHeridos(nuevosHeridos);
        Assert.assertFalse(sismo.getHeridos() == heridos);
        Assert.assertTrue(sismo.getHeridos() == nuevosHeridos);
    }

    /**
     * Prueba unitaria para {@link Sismo#heridosProperty}.
     */
    @Test public void testHeridosProperty() {
        String fecha = fechaAleatoria();
        int heridos = heridosAleatorios();
        double profundidad = profundidadAleatoria();
        int replicas = replicasAleatorias();
        double magnitud = magnitudAleatoria();
        String localidades = localidadesAleatorias();
        sismo = new Sismo(fecha, heridos, profundidad, replicas, magnitud, localidades);
        Assert.assertTrue(sismo.heridosProperty().get() == heridos);
    }

    /**
     * Prueba unitaria para {@link Sismo#getProfundidad}.
     */
    @Test public void testGetProfundidad() {
        String fecha = fechaAleatoria();
        int heridos = heridosAleatorios();
        double profundidad = profundidadAleatoria();
        int replicas = replicasAleatorias();
        double magnitud = magnitudAleatoria();
        String localidades = localidadesAleatorias();
        sismo = new Sismo(fecha, heridos, profundidad, replicas, magnitud, localidades);
        Assert.assertTrue(sismo.getProfundidad() == profundidad);
        Assert.assertFalse(sismo.getProfundidad() == profundidad + 1.0);
    }

    /**
     * Prueba unitaria para {@link Sismo#setProfundidad}.
     */
    @Test public void testSetProfundidad() {
        String fecha = fechaAleatoria();
        int heridos = heridosAleatorios();
        double profundidad = profundidadAleatoria();
        double nuevaProfundidad = profundidad + 1.0;
        int replicas = replicasAleatorias();
        double magnitud = magnitudAleatoria();
        String localidades = localidadesAleatorias();
        sismo = new Sismo(fecha, heridos, profundidad, replicas, magnitud, localidades);
        Assert.assertTrue(sismo.getProfundidad() == profundidad);
        Assert.assertFalse(sismo.getProfundidad() == nuevaProfundidad);
        sismo.setProfundidad(nuevaProfundidad);
        Assert.assertFalse(sismo.getProfundidad() == profundidad);
        Assert.assertTrue(sismo.getProfundidad() == nuevaProfundidad);
    }

    /**
     * Prueba unitaria para {@link Sismo#profundidadProperty}.
     */
    @Test public void testProfundidadProperty() {
        String fecha = fechaAleatoria();
        int heridos = heridosAleatorios();
        double profundidad = profundidadAleatoria();
        int replicas = replicasAleatorias();
        double magnitud = magnitudAleatoria();
        String localidades = localidadesAleatorias();
        sismo = new Sismo(fecha, heridos, profundidad, replicas, magnitud, localidades);
        Assert.assertTrue(sismo.profundidadProperty().get() == profundidad);
    }

    /**
     * Prueba unitaria para {@link Sismo#getReplicas}.
     */
    @Test public void testGetReplicas() {
        String fecha = fechaAleatoria();
        int heridos = heridosAleatorios();
        double profundidad = profundidadAleatoria();
        int replicas = replicasAleatorias();
        double magnitud = magnitudAleatoria();
        String localidades = localidadesAleatorias();
        sismo = new Sismo(fecha, heridos, profundidad, replicas, magnitud, localidades);
        Assert.assertTrue(sismo.getReplicas() == replicas);
        Assert.assertFalse(sismo.getReplicas() == replicas + 50);
    }

    /**
     * Prueba unitaria para {@link Sismo#setReplicas}.
     */
    @Test public void testSetReplicas() {
        String fecha = fechaAleatoria();
        int heridos = heridosAleatorios();
        double profundidad = profundidadAleatoria();
        int replicas = replicasAleatorias();
        int nuevasReplicas = replicas + 50;
        double magnitud = magnitudAleatoria();
        String localidades = localidadesAleatorias();
        sismo = new Sismo(fecha, heridos, profundidad, replicas, magnitud, localidades);
        Assert.assertTrue(sismo.getReplicas() == replicas);
        Assert.assertFalse(sismo.getReplicas() == nuevasReplicas);
        sismo.setReplicas(nuevasReplicas);
        Assert.assertFalse(sismo.getReplicas() == replicas);
        Assert.assertTrue(sismo.getReplicas() == nuevasReplicas);
    }

    /**
     * Prueba unitaria para {@link Sismo#replicasProperty}.
     */
    @Test public void testReplicasProperty() {
        String fecha = fechaAleatoria();
        int heridos = heridosAleatorios();
        double profundidad = profundidadAleatoria();
        int replicas = replicasAleatorias();
          double magnitud = magnitudAleatoria();
        String localidades = localidadesAleatorias();
        sismo = new Sismo(fecha, heridos, profundidad, replicas, magnitud, localidades);
        Assert.assertTrue(sismo.replicasProperty().get() == replicas);
    }

    /**
     * Prueba unitaria para {@link Sismo#getMagnitud}.
     */
    @Test public void testGetMagnitud() {
        String fecha = fechaAleatoria();
        int heridos = heridosAleatorios();
        double profundidad = profundidadAleatoria();
        int replicas = replicasAleatorias();
        double magnitud = magnitudAleatoria();
        String localidades = localidadesAleatorias();
        sismo = new Sismo(fecha, heridos, profundidad, replicas, magnitud, localidades);
        Assert.assertTrue(sismo.getMagnitud() == magnitud);
        Assert.assertFalse(sismo.getMagnitud() == magnitud + 1.0);
    }

    /**
     * Prueba unitaria para {@link Sismo#setMagnitud}.
     */
    @Test public void testSetMagnitud() {
        String fecha = fechaAleatoria();
        int heridos = heridosAleatorios();
        double profundidad = profundidadAleatoria();
        int replicas = replicasAleatorias();
        double magnitud = magnitudAleatoria();
        double nuevaMagnitud = magnitud + 1.0;
        String localidades = localidadesAleatorias();
        sismo = new Sismo(fecha, heridos, profundidad, replicas, magnitud, localidades);
        Assert.assertTrue(sismo.getMagnitud() == magnitud);
        Assert.assertFalse(sismo.getMagnitud() == nuevaMagnitud);
        sismo.setMagnitud(nuevaMagnitud);
        Assert.assertFalse(sismo.getMagnitud() == magnitud);
        Assert.assertTrue(sismo.getMagnitud() == nuevaMagnitud);
    }

    /**
     * Prueba unitaria para {@link Sismo#magnitudProperty}.
     */
    @Test public void testMagnitudProperty() {
        String fecha = fechaAleatoria();
        int heridos = heridosAleatorios();
        double profundidad = profundidadAleatoria();
        int replicas = replicasAleatorias();
        double magnitud = magnitudAleatoria();
        String localidades = localidadesAleatorias();
        sismo = new Sismo(fecha, heridos, profundidad, replicas, magnitud, localidades);
        Assert.assertTrue(sismo.magnitudProperty().get() == magnitud);
    }
    /**
     * Prueba unitaria para {@link Sismo#toString}.
     */
    @Test public void testToString() {
        String fecha = fechaAleatoria();
        int heridos = heridosAleatorios();
        double profundidad = profundidadAleatoria();
        int replicas = replicasAleatorias();
        double magnitud = magnitudAleatoria();
        String localidades = localidadesAleatorias();
        sismo = new Sismo(fecha, heridos, profundidad, replicas, magnitud, localidades);
        String cadena =String.format("Fecha   : %s\n" +
                                        "Heridos   : %d\n" +
                                        "Profundidad : %4.1f\n" +
                                        "Replicas     : %d\n"+
                                        "Magnitud    : %2.1f\n"+
                                        "Localidades  : %s",
                                    fecha, heridos, profundidad, replicas, magnitud, localidades);
        Assert.assertTrue(sismo.toString().equals(cadena));
        heridos = 213;
        profundidad = 0.99;
        sismo.setHeridos(heridos);
        sismo.setProfundidad(profundidad);
        cadena =String.format("Fecha   : %s\n" +
                                        "Heridos   : %d\n" +
                                        "Profundidad : %4.1f\n" +
                                        "Replicas     : %d\n"+
                                        "Magnitud    : %2.1f\n"+
                                        "Localidades  : %s",
                                    fecha, heridos, profundidad, replicas, magnitud, localidades);
        Assert.assertTrue(sismo.toString().equals(cadena));
    }

    /**
     * Prueba unitaria para {@link Sismo#equals}.
     */
    @Test public void testEquals() {
        String fecha = fechaAleatoria();
        int heridos = heridosAleatorios();
        double profundidad = profundidadAleatoria();
        int replicas = replicasAleatorias();
        double magnitud = magnitudAleatoria();
        String localidades = localidadesAleatorias();
        sismo = new Sismo(fecha, heridos, profundidad, replicas, magnitud, localidades);
        Sismo igual = new Sismo(new String(fecha),
                                        heridos, profundidad, replicas, magnitud, new String(localidades));
        Assert.assertTrue(sismo.equals(igual));
        String otraFecha = fecha + " x";
        int otrosHeridos = heridos + 1;
        double otraProfundidad = (profundidad != 0.0) ? profundidad / 10.0 : 10.0;
        int otrasReplicas = replicas + 1;
        double otraMagnitud = (magnitud != 0.0) ? magnitud / 10.0 : 10.0;
        String otrasLocalidades = localidades + ", x ";
        Sismo distinto =
            new Sismo(otraFecha, heridos, profundidad, replicas, magnitud, localidades);
        Assert.assertFalse(sismo.equals(distinto));
        distinto = new Sismo(fecha, otrosHeridos, profundidad, replicas, magnitud, localidades);
        Assert.assertFalse(sismo.equals(distinto));
        distinto = new Sismo(fecha, heridos, otraProfundidad, replicas, magnitud, localidades);
        Assert.assertFalse(sismo.equals(distinto));
        distinto = new Sismo(fecha, heridos, profundidad, otrasReplicas, magnitud, localidades);
        Assert.assertFalse(sismo.equals(distinto));
        distinto = new Sismo(fecha, heridos, profundidad, replicas, otraMagnitud, localidades);
        Assert.assertFalse(sismo.equals(distinto));
        distinto = new Sismo(fecha, heridos, profundidad, replicas, magnitud, otrasLocalidades);
        Assert.assertFalse(sismo.equals(distinto));
        distinto = new Sismo(otraFecha, otrosHeridos,
                                otraProfundidad, otrasReplicas, otraMagnitud, otrasLocalidades);
        Assert.assertFalse(sismo.equals(distinto));
        Assert.assertFalse(sismo.equals("Una cadena"));
        Assert.assertFalse(sismo.equals(null));
    }

    /**
     * Prueba unitaria para {@link Sismo#seria}.
     */
    @Test public void testSeria() {
        String fecha = fechaAleatoria();
        int heridos = heridosAleatorios();
        double profundidad = profundidadAleatoria();
        int replicas = replicasAleatorias();
        double magnitud = magnitudAleatoria();
        String localidades = localidadesAleatorias();
        sismo = new Sismo(fecha, heridos, profundidad, replicas, magnitud, localidades);
        String linea =String.format("%s\t%d\t%4.1f\t%d\t%2.1f\t%s\n", 
                                    fecha, heridos, profundidad, replicas, magnitud, localidades);
        Assert.assertTrue(sismo.seria().equals(linea));
    }

    /**
     * Prueba unitaria para {@link Sismo#deseria}.
     */
    @Test public void testDeseria() {
        sismo = new Sismo(null, 0, 0.0, 0, 0, null);

        String fecha = fechaAleatoria();
        int heridos = heridosAleatorios();
        double profundidad = profundidadAleatoria();
        int replicas = replicasAleatorias();
        double magnitud = magnitudAleatoria();
        String localidades = localidadesAleatorias();
        String linea = String.format("%s\t%d\t%4.1f\t%d\t%2.1f\t%s\n",
                                fecha, heridos, profundidad, replicas, magnitud, localidades);
        try {
            sismo.deseria(linea);
        } catch (ExcepcionLineaInvalida eli) {
            Assert.fail();
        }

        Assert.assertTrue(sismo.getFecha().equals(fecha));
        Assert.assertTrue(sismo.getHeridos() == heridos);
        Assert.assertTrue(sismo.getProfundidad() == profundidad);
        Assert.assertTrue(sismo.getReplicas() == replicas);
        Assert.assertTrue(sismo.getMagnitud() == magnitud);
        Assert.assertTrue(sismo.getLocalidades().equals(localidades));
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
        sismo = new Sismo("A", 1, 1, 1, 1, "A");
        Sismo e = new Sismo("B", 2, 2, 2, 2, "B");
        Assert.assertFalse(sismo == e);
        Assert.assertFalse(sismo.equals(e));
        sismo.actualiza(e);
        Assert.assertFalse(sismo == e);
        Assert.assertTrue(sismo.equals(e));
        Assert.assertTrue(sismo.getFecha().equals("B"));
        Assert.assertFalse(sismo.fechaProperty() ==
                            e.fechaProperty());
        Assert.assertFalse(sismo.heridosProperty() ==
                            e.heridosProperty());
        Assert.assertFalse(sismo.profundidadProperty() ==
                            e.profundidadProperty());
        Assert.assertFalse(sismo.replicasProperty() ==
                            e.replicasProperty());
        Assert.assertFalse(sismo.magnitudProperty() ==
                            e.magnitudProperty());
        Assert.assertTrue(sismo.getLocalidades().equals("B"));
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
        int heridos = heridosAleatorios();
        double profundidad = profundidadAleatoria();
        int replicas = replicasAleatorias();
        double magnitud = magnitudAleatoria();
        String localidades = localidadesAleatorias();
        sismo = new Sismo(fecha, heridos, profundidad, replicas, magnitud, localidades);

        String f = sismo.getFecha();
        int g = sismo.getFecha().length();
        Assert.assertTrue(sismo.casa(CampoSismo.FECHA, f));
        f = sismo.getFecha().substring(0, g/2);
        Assert.assertTrue(sismo.casa(CampoSismo.FECHA, f));
        f = sismo.getFecha().substring(g/2, g);
        Assert.assertTrue(sismo.casa(CampoSismo.FECHA, f));
        f = sismo.getFecha().substring(g/3, 2*(g/3));
        Assert.assertTrue(sismo.casa(CampoSismo.FECHA, f));
        Assert.assertFalse(sismo.casa(CampoSismo.FECHA, ""));
        Assert.assertFalse(sismo.casa(CampoSismo.FECHA, "XXX"));
        Assert.assertFalse(sismo.casa(CampoSismo.FECHA, 1000));
        Assert.assertFalse(sismo.casa(CampoSismo.FECHA, null));

        int h = sismo.getHeridos();
        Assert.assertTrue(sismo.casa(CampoSismo.HERIDOS, h));
        h = sismo.getHeridos() - 100;
        Assert.assertTrue(sismo.casa(CampoSismo.HERIDOS, h));
        h = sismo.getHeridos() + 100;
        Assert.assertFalse(sismo.casa(CampoSismo.HERIDOS, h));
        Assert.assertFalse(sismo.casa(CampoSismo.HERIDOS, "XXX"));
        Assert.assertFalse(sismo.casa(CampoSismo.HERIDOS, null));

        double p = sismo.getProfundidad();
        Assert.assertTrue(sismo.casa(CampoSismo.PROFUNDIDAD, p));
        p = sismo.getProfundidad() - 5.0;
        Assert.assertTrue(sismo.casa(CampoSismo.PROFUNDIDAD, p));
        p = sismo.getProfundidad() + 5.0;
        Assert.assertFalse(sismo.casa(CampoSismo.PROFUNDIDAD, p));
        Assert.assertFalse(sismo.casa(CampoSismo.PROFUNDIDAD, "XXX"));
        Assert.assertFalse(sismo.casa(CampoSismo.PROFUNDIDAD, null));

        int r = sismo.getReplicas();
        Assert.assertTrue(sismo.casa(CampoSismo.REPLICAS, r));
        r = sismo.getReplicas() - 10;
        Assert.assertTrue(sismo.casa(CampoSismo.REPLICAS, r));
        r = sismo.getReplicas() + 10;
        Assert.assertFalse(sismo.casa(CampoSismo.REPLICAS, r));
        Assert.assertFalse(sismo.casa(CampoSismo.REPLICAS, "XXX"));
        Assert.assertFalse(sismo.casa(CampoSismo.REPLICAS, null));

        double m = sismo.getMagnitud();
        Assert.assertTrue(sismo.casa(CampoSismo.MAGNITUD, m));
        m = sismo.getMagnitud() - 5.0;
        Assert.assertTrue(sismo.casa(CampoSismo.MAGNITUD, m));
        m = sismo.getMagnitud() + 5.0;
        Assert.assertFalse(sismo.casa(CampoSismo.MAGNITUD, m));
        Assert.assertFalse(sismo.casa(CampoSismo.MAGNITUD, "XXX"));
        Assert.assertFalse(sismo.casa(CampoSismo.MAGNITUD, null));

        String l = sismo.getLocalidades();
        int k = sismo.getLocalidades().length();
        Assert.assertTrue(sismo.casa(CampoSismo.LOCALIDADES, l));
        l = sismo.getLocalidades().substring(0, k/2);
        Assert.assertTrue(sismo.casa(CampoSismo.LOCALIDADES, l));
        l = sismo.getLocalidades().substring(k/2, k);
        Assert.assertTrue(sismo.casa(CampoSismo.LOCALIDADES, l));
        l = sismo.getLocalidades().substring(k/3, 2*(k/3));
        Assert.assertTrue(sismo.casa(CampoSismo.LOCALIDADES, l));
        Assert.assertFalse(sismo.casa(CampoSismo.LOCALIDADES, ""));
        Assert.assertFalse(sismo.casa(CampoSismo.LOCALIDADES, "XXX"));
        Assert.assertFalse(sismo.casa(CampoSismo.LOCALIDADES, 1000));
        Assert.assertFalse(sismo.casa(CampoSismo.LOCALIDADES, null));
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
