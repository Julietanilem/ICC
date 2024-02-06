package mx.unam.ciencias.icc;

/**
 * Enumeración para los campos de un {@link Sismo}.
 */
public enum CampoSismo {

    /** La fecha del sismo. */
    FECHA,
    /** El número de heridos del sismo. */
    HERIDOS,
    /** La profundidad del sismo. */
    PROFUNDIDAD,
    /** Las replicas del sismo. */
    REPLICAS, 
    /** La magnitud del sismo. */
    MAGNITUD,
    /** Las localidades afectadas por el sismo. */
    LOCALIDADES;



    /**
     * Regresa una representación en cadena del campo para ser usada en
     * interfaces gráficas.
     * @return una representación en cadena del campo.
     */
    @Override public String toString() {
        switch (this){
            case FECHA : return "Fecha";
            case HERIDOS : return "Heridos";
            case PROFUNDIDAD : return "Profundidad";
            case REPLICAS: return "Réplicas";
            case MAGNITUD : return "Magnitud";
            case LOCALIDADES : return "Localidades";
            default: return "";
        }
    }
}
