package mx.unam.ciencias.icc;

/**
 * Enumeración para los campos de un {@link Sismo}.
 */
public enum CampoSismo {

    /** La fecha del sismo. */
    FECHA,
    /** El número de muertes del sismo. */
    MUERTES,
    /** El profundidad del sismo. */
    PROFUNDIDAD,
    /** La replicas del sismo. */
    REPLICAS,
    /** La magnitud de sismo*/
    MAGNITUD,
    /** Las entidades afectadas por el sismo */
    ENTIDADES;
    
    /**
     * Regresa una representación en cadena del campo para ser usada en
     * interfaces gráficas.
     * @return una representación en cadena del campo.
     */
    @Override public String toString() {
        switch (this){
            case FECHA : return "Fecha";
            case MUERTES : return "Muertes";
            case PROFUNDIDAD : return "Profundidad";
            case REPLICAS: return "Réplicas";
            case MAGNITUD: return "Magnitud";
            case ENTIDADES: return "Entidades";
            default: return "";
        }
    }
}
