package mx.unam.ciencias.icc.igu;

/**
 * Clase abstracta para controladores del contenido de diálogo con formas con
 * datos de sismos que se aceptan o rechazan.
 */
public abstract class ControladorFormaSismo extends ControladorForma {

    /** El valor de la fecha. */
    protected String fecha;
    /** El valor del número de heridos. */
    protected int heridos;
    /** El valor de la profundidad. */
    protected double profundidad;
    /** El valor de las replicas. */
    protected int replicas;
    /** El valor de la magnitud. */
    protected double magnitud;
     /** El valor de las localidades. */
    protected String localidades;

    /**
     * Verifica que la fecha sea válido.
     * @param fecha la fecha a verificar.
     * @return <code>true</code> si la fecha es válida; <code>false</code> en
     *         otro caso.
     */
    protected boolean verificaFecha(String fecha) {
        if (fecha == null || fecha.isEmpty())
            return false ;
        this.fecha = fecha ;
        return true ;
    }

    /**
     * Verifica que el número de heridos sea válido.
     * @param heridos el número de heridos a verificar.
     * @return <code>true</code> si el número de heridos es válido;
     *         <code>false</code> en otro caso.
     */
    protected boolean verificaHeridos(String heridos) {
        if ( heridos == null || heridos.isEmpty ())
            return false ;
        try {
            this.heridos = Integer.parseInt(heridos);
        } catch (NumberFormatException nfe) {
            return false ;
        }
        return true ;
    }

    /**
     * Verifica que la profundidad sea válida.
     * @param profundidad la profundidad a verificar.
     * @return <code>true</code> si la profundidad es válida; <code>false</code> en
     *         otro caso.
     */
    protected boolean verificaProfundidad(String profundidad) {
        if (profundidad == null || profundidad.isEmpty())
            return false ;
        try {
            this.profundidad = Double.parseDouble(profundidad);
        } catch(NumberFormatException nfe) {
            return false ;
        }
        return true;
    }

    /**
     * Verifica que las replicas sean válidas.
     * @param replicas las replicas a verificar.
     * @return <code>true</code> si las replicas son válidas; <code>false</code> en
     *         otro caso.
     */
    protected boolean verificaReplicas(String replicas) {
        if (replicas == null || replicas.isEmpty())
            return false ;
        try {
            this.replicas = Integer.parseInt(replicas);
        } catch (NumberFormatException nfe) {
            return false ;
        }
        return true ;
    }

    /**
     * Verifica que la magnitud sea válida.
     * @param magnitud la magnitud a verificar.
     * @return <code>true</code> si la magnitud es válida; <code>false</code> en
     *         otro caso.
     */
    protected boolean verificaMagnitud(String magnitud) {
        if (magnitud == null || magnitud.isEmpty())
            return false;
        try {
            this.magnitud = Double.parseDouble(magnitud);
        } catch(NumberFormatException nfe) {
            return false ;
        }
        return true;
    }

    /**
     * Verifica que la localidades sea válido.
     * @param localidades la localidades a verificar.
     * @return <code>true</code> si las localidades es válida; <code>false</code> en
     *         otro caso.
     */
    protected boolean verificaLocalidades(String localidades) {
        if (localidades == null || localidades.isEmpty())
            return false ;
        this.localidades = localidades ;
        return true ;
    }
}
