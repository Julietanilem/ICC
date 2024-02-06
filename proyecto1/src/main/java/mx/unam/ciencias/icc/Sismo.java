package mx.unam.ciencias.icc;

/**
 * Clase para representar sismos. Un sismo tiene fecha, número de
 * muertes, profundidad y replicas. La clase implementa {@link Registro}, por lo que
 * puede seriarse en una línea de texto y deseriarse de una línea de
 * texto; además de determinar si sus campos casan valores arbitrarios y
 * actualizarse con los valores de otro sismo.
 */
public class Sismo implements Registro<Sismo, CampoSismo> {

    /* fecha del sismo. */
    private String fecha;
    /* Número de muertes. */
    private int muertes;
    /* profundidad del sismo. */
    private double profundidad;
    /* replicas del sismo.*/
    private int replicas;
    /* magnitud del sismo.*/
    private double magnitud;
    /* entidades afectadas.*/
    private Lista<String>  entidades;
    /**
     * Define el estado inicial de un sismo.
     * @param fecha la fecha del sismo.
     * @param muertes el número de muertes del sismo.
     * @param profundidad la profundidad del sismo.
     * @param replicas las replicas del sismo.
     * @param magnitud la magitud del sismo
     * @param entidades las entidades afectadas
     */
    public Sismo(String fecha,
                      int    muertes,
                      double profundidad,
                      int    replicas, 
                      double magnitud, 
                      Lista<String> entidades) {
        this.entidades = (entidades == null) ? new Lista<>():  entidades.copia();
        this.fecha = fecha;
        this.muertes = muertes;
        this.profundidad = profundidad;
        this.replicas = replicas;
        this.magnitud = magnitud;

    }

    /**
     * Regresa la fecha del sismo.
     * @return la fecha del sismo.
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * Define la  fecha del sismo.
     * @param fecha la nuevo fecha del sismo.
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    /**
     * Regresa el número de muertes del sismo.
     * @return el número de muertes del sismo.
     */
    public int getMuertes() {
        return muertes;
    }

    /**
     * Define el número muertes del sismo.
     * @param muertes el nuevo número de muertes del sismo.
     */
    public void setMuertes(int muertes) {
        this.muertes = muertes;
    }

    /**
     * Regresa la profundidad del sismo.
     * @return la profundidad del sismo.
     */
    public double getProfundidad() {
        return profundidad;
    }

    /**
     * Define la profundidad del sismo.
     * @param profundidad la nuevo profundidad del sismo.
     */
    public void setProfundidad(double profundidad) {
        this.profundidad = profundidad;  
    }

    /**
     * Regresa las replicas del sismo.
     * @return las replicas del sismo.
     */
    public int getReplicas() {
        return replicas;
    }

    /**
     * Define las replicas del sismo.
     * @param replicas las nueva replicas del sismo.
     */
    public void setReplicas(int replicas) {
        this.replicas = replicas;  
    }
    /**
     * Regresa la magnitud del sismo.
     * @return la magnitud del sismo.
     */
    public double getMagnitud() {
        return magnitud;
    }

    /**
     * Define la magnitud del sismo.
     * @param magnitud la nueva replicas del sismo.
     */
    public void setMagnitud(double magnitud) {
        this.magnitud = magnitud;  
    }
        /**
     * Regresa las entidades afectas por el sismo.
     * @return las entidades afectas por el sismo.
     */
    public Lista<String>  getEntidades() {
        return entidades;	
    }
        /**
         * Define  las  entidades afectas por el sismo.
         * @param  las  entidades afectas por el sismo.
         */
        public void setEntidades(Lista<String> entidades) {
            this.entidades = entidades.copia();
    }
    /**
     * Regresa una representación en cadena del sismo.
     * @return una representación en cadena del sismo.
     */
    @Override public String toString() {
        return String.format("Fecha   : %s\n" +
                            "Muertes   : %d\n" +
                            "Profundidad : %2.2f\n" +
                            "Replicas     : %d\n"+
                            "Magnitud : %2.2f\n"+
                            "Entidades : %s",
        fecha, muertes, profundidad, replicas, magnitud, formatoEntidades());
    }

    /**
     * Nos dice si el objeto recibido es un sismo igual al que manda llamar
     * el método.
     * @param objeto el objeto con el que el sismo se comparará.
     * @return <code>true</code> si el objeto recibido es un sismo con las
     *         mismas propiedades que el objeto que manda llamar al método,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (!(objeto instanceof Sismo))
            return false;
        Sismo sismo = (Sismo)objeto;
        if(objeto == null) 
            return false;
        return fecha.equals(sismo.fecha) &&
            muertes == sismo.muertes &&
            profundidad == sismo.profundidad &&
            replicas == sismo.replicas &&
            magnitud == sismo.magnitud &&
            entidades.equals(sismo.entidades);
    }

    /**
     * Regresa el sismo seriado en una línea de texto. La línea de
     * texto que este método regresa debe ser aceptada por el método {@link
     * Sismo#deseria}.
     * @return la seriación del sismo en una línea de texto.
     */
    @Override public String seria() {
        return String.format("%s\t%d\t%2.2f\t%d\t%2.2f\t%s\n", 
                            fecha,muertes,profundidad,replicas, magnitud, 
                            formatoEntidades());
    }
    /**
     * Regresa la lista de entidades afectadas en un formato separado por comas
     * @return una cadenada con las entidades correpondientes
     */
    private String formatoEntidades(){
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
     * Deseria una línea de texto en las propireplicases del sismo. La
     * seriación producida por el método {@link Sismo#seria} debe
     * ser aceptada por este método.
     * @param linea la línea a deseriar.
     * @throws ExcepcionLineaInvalida si la línea recibida es nula, vacía o no
     *         es una seriación válida de un sismo.
     */
    @Override public void deseria(String linea) {
        if(linea == null) throw new ExcepcionLineaInvalida("La línea es nula");
        linea = linea.trim();
        if(linea.isEmpty()) throw new ExcepcionLineaInvalida("La línea esta vacía");
        String[] campos = linea.split("\t");
        if (campos.length != 6) throw new ExcepcionLineaInvalida("La línea no tiene los campos establecidos");
        try{
            fecha = campos[0];
            muertes = Integer.parseInt(campos[1]);
            profundidad = Double.parseDouble(campos[2]);
            replicas = Integer.parseInt(campos[3]);
            magnitud = Double.parseDouble(campos[4]);
            entidades.limpia();
            String[] arreglo_entidades = campos[5].split(", ");
            for (int i = 0;i<arreglo_entidades.length;i++){
                entidades.agregaFinal(arreglo_entidades[i]);
            }
        }catch (Exception e){
            throw new ExcepcionLineaInvalida("Linea invalida, información incorrecta" );
        }
    }

    /**
     * Nos dice si el sismo casa el valor dado en el campo especificado.
     * @param campo el campo que hay que casar.
     * @param valor el valor con el que debe casar el campo del registro.
     * @return true si el valor es encontrado dentro de la lista para ese campo.
     * @throws IllegalArgumentException si el campo es <code>null</code>.
     */
    @Override public boolean casa(CampoSismo campo, Object valor) {
        if(!(campo instanceof CampoSismo))
        throw new IllegalArgumentException("El valor pasado como campo debe ser un CampoSismo válido");
        CampoSismo c =(CampoSismo) campo;
        switch (c){
        case FECHA:
            return casaFecha(valor);
        case MUERTES :
            return casaMuertes(valor);
        case REPLICAS:
            return casaReplicas(valor);
        case PROFUNDIDAD:
            return casaProfundidad(valor);
        case MAGNITUD:
            return casaMagnitud(valor);
        case ENTIDADES:
            return casaEntidades(valor);
        default:
            return false;
        }
    }
    /**
     * Determina  si la fecha contiene el valor proporcionado
     * @return true si el objeto es una cadena contenida en la fecha.
     */
    private boolean casaFecha(Object o){
        if(!(o instanceof String)) return false;
        String v = (String) o;
        if(v.isEmpty()) return false;
        return fecha.contains(v);
    }
    /**
     *  Determina si las muertes ocasionadas son mayores a las del valor proporcionado.
     * @return true si el objeto es una entero menor al número de muertes
     */
    private boolean casaMuertes(Object o){
        if(!(o instanceof Integer)) return false;
        Integer v = (Integer) o;
        return v.intValue() <= muertes;
    }
    /**
     *  Determina si las réplicas  son mayores a las del valor proporcionado.
     * @return true si el objeto es una entero menor al número de réplicas
     */
    private boolean casaReplicas(Object o){
        if(!(o instanceof Integer)) return false;
        Integer v = (Integer) o;
        return v.intValue() <= replicas;
    }
    /**
     *  Determina si la prófundidad  es  mayor al  valor proporcionado.
     * @return true si el objeto es un flotante menor a la profundidad.
     */
    private boolean casaProfundidad(Object o){
        if(!(o instanceof Double)) return false;
        Double v = (Double) o;
        return  v.doubleValue() <= profundidad;
    }
    /**
     *  Determina si la magnitud  es  mayor al  valor proporcionado.
     * @return true si el objeto es un flotante menor a la magnitud.
     */
    private boolean casaMagnitud(Object o){
        if(!(o instanceof Double)) return false;
        Double v = (Double) o;
        return  v.doubleValue() <= magnitud;
    }
    /**
     *  Determina si la entidad fue afectada por el sismo.
     * @return true si el objeto es una cadena que coincide con algún estado afectado.
     */
    private boolean casaEntidades(Object o){
        if(!(o instanceof String)) return false;
        String v = (String) o;
        if(v.isEmpty()) return false;
        return entidades.contiene(v);
    }
    /**
     * Actualiza los valores del sismo con los del sismo recibido.
     * @param sismo el sismo con el cual actualizar los valores.
     * @throws IllegalArgumentException si el sismo es <code>null</code>.
     */
    @Override public void actualiza(Sismo sismo) {
        if(sismo == null) throw new IllegalArgumentException("No puede actualizarse a un sismo null") ;
        fecha = sismo.fecha;
        profundidad = sismo.profundidad;
        replicas = sismo.replicas;
        muertes = sismo.muertes;
        magnitud = sismo.magnitud;
        entidades = sismo.entidades.copia();
    }
}
