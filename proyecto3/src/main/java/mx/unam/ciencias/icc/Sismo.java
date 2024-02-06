package mx.unam.ciencias.icc;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Clase para representar sismos. Un sismo tiene fecha, número de
 * heridos, profundidad, replicas, magnitud y localidades afectadas. La clase implementa {@link Registro}, por lo que
 * puede seriarse en una línea de texto y deseriarse de una línea de
 * texto; además de determinar si sus campos casan valores arbitrarios y
 * actualizarse con los valores de otro sismo.
 */
public class Sismo implements Registro<Sismo, CampoSismo> {

    /* Fecha del sismo. */
    private final StringProperty fecha;
    /* Número de heridos. */
    private final IntegerProperty heridos;
    /* Profundidad del sismo. */
    private final DoubleProperty profundidad;
    /* Replicas del sismo.*/
    private final IntegerProperty replicas;
     /* Magnitud del sismo. */
    private final DoubleProperty magnitud;
    /* Localidades afectadas por el sismo. */
    private final StringProperty localidades;
    /**
     * Define el estado inicial de un sismo.
     * @param fecha la fecha del sismo.
     * @param heridos el número de heridos del sismo.
     * @param profundidad el profundidad del sismo.
     * @param replicas las replicas del sismo.
     * @param magnitud la magnitud del sismo.
     * @param localidades la localidades del sismo.
     */
    public Sismo(String fecha,
                int    heridos,
                double profundidad,
                int    replicas, 
                double magnitud,
                String localidades) {
        this.fecha = new SimpleStringProperty(fecha);
        this.heridos = new SimpleIntegerProperty (heridos);
        this.profundidad = new SimpleDoubleProperty (profundidad);
        this.replicas = new SimpleIntegerProperty (replicas);
        this.magnitud = new SimpleDoubleProperty (magnitud);
        this.localidades = new SimpleStringProperty(localidades);
    }

    /**
     * Regresa el fecha del sismo.
     * @return el fecha del sismo.
     */
    public String getFecha() {
        return fecha.get();
    }

    /**
     * Define el fecha del sismo.
     * @param fecha el nuevo fecha del sismo.
     */
    public void setFecha(String fecha) {
        this.fecha.set(fecha);
    }

    /**
     * Regresa la propireplicas del fecha.
     * @return la propireplicas del fecha.
     */
    public StringProperty fechaProperty() {
        return this.fecha;
    }

    /**
     * Regresa el número de heridos del sismo.
     * @return el número de heridos del sismo.
     */
    public int getHeridos() {
        return heridos.get();
    }

    /**
     * Define el número heridos del sismo.
     * @param heridos el nuevo número de heridos del sismo.
     */
    public void setHeridos(int heridos) {
        this.heridos.set(heridos);
    }

    /**
     * Regresa la propireplicas del número de heridos.
     * @return la propireplicas del número de heridos.
     */
    public IntegerProperty heridosProperty() {
        return this.heridos;
    }

    /**
     * Regresa el profundidad del sismo.
     * @return el profundidad del sismo.
     */
    public double getProfundidad() {
        return profundidad.get();
    }

    /**
     * Define el profundidad del sismo.
     * @param profundidad el nuevo profundidad del sismo.
     */
    public void setProfundidad(double profundidad) {
        this.profundidad.set(profundidad);
    }

    /**
     * Regresa la propireplicas del profundidad.
     * @return la propireplicas del profundidad.
     */
    public DoubleProperty profundidadProperty() {
        return this.profundidad;
    }

    /**
     * Regresa la replicas del sismo.
     * @return la replicas del sismo.
     */
    public int getReplicas() {
        return replicas.get();
    }

    /**
     * Define la replicas del sismo.
     * @param replicas la nueva replicas del sismo.
     */
    public void setReplicas(int replicas) {
        this.replicas.set(replicas);
    }

    /**
     * Regresa la propireplicas de las replicas.
     * @return la propireplicas de las replicas.
     */
    public IntegerProperty replicasProperty() {
        return this.replicas;
    }

    /**
     * Regresa la magnitud del sismo.
     * @return la magnitud del sismo.
     */
    public double getMagnitud() {
        return magnitud.get();
    }

    /**
     * Define la magnitud del sismo.
     * @param magnitud la nueva magnitud del sismo.
     */
    public void setMagnitud(double magnitud) {
        this.magnitud.set(magnitud);
    }

    /**
     * Regresa la propireplicas de la magnitud.
     * @return la propireplicas de la magnitud.
     */
    public DoubleProperty magnitudProperty() {
        return this.magnitud;
    }

    /**
     * Regresa las localidades del sismo.
     * @return las localidades del sismo.
     */
    public String getLocalidades() {
        return localidades.get();
    }

    /**
     * Define las localidades del sismo.
     * @param localidades las nuevas localidades del sismo.
     */
    public void setLocalidades(String localidades) {
        this.localidades.set(localidades);
    }

    /**
     * Regresa la propireplicas de las localidades.
     * @return la propireplicas de las localidades.
     */
    public StringProperty localidadesProperty() {
        return this.localidades;
    }

    /**
     * Regresa una representación en cadena del sismo.
     * @return una representación en cadena del sismo.
     */
    @Override public String toString() {
		return String.format("Fecha   : %s\n" +
                            "Heridos   : %d\n" +
                            "Profundidad : %4.1f\n" +
                            "Replicas     : %d\n"+
                            "Magnitud    : %2.1f\n"+
                            "Localidades  : %s",
                            fecha.get(), heridos.get(), profundidad.get(), replicas.get(), 
                            magnitud.get(), localidades.get());
    }

    /**
     * Nos dice si el objeto recibido es un sismo igual al que manda llamar
     * el método.
     * @param objeto el objeto con el que el sismo se comparará.
     * @return <code>true</code> si el objeto recibido es un sismo con las
     *         mismas propireplicases que el objeto que manda llamar al método,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (!(objeto instanceof Sismo))
            return false;
        Sismo sismo = (Sismo)objeto;
        if(objeto == null) return false;
        return getFecha().equals(sismo.getFecha()) &&
                getHeridos() == sismo.getHeridos() &&
                getProfundidad() == sismo.getProfundidad() &&
                getReplicas() == sismo.getReplicas() &&
                getMagnitud() == sismo.getMagnitud() &&
                getLocalidades().equals(sismo.getLocalidades())  ;
    }

    /**
     * Regresa el sismo seriado en una línea de texto. La línea de
     * texto que este método regresa debe ser aceptada por el método {@link
     * Sismo#deseria}.
     * @return la seriación del sismo en una línea de texto.
     */
    @Override public String seria() {
        return String.format("%s\t%d\t%4.1f\t%d\t%2.1f\t%s\n", 
                            getFecha(), getHeridos(), getProfundidad(), 
                            getReplicas(), getMagnitud(), getLocalidades());
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
		if (linea == null) throw new ExcepcionLineaInvalida("La línea es nula");
        linea = linea.trim();
        if (linea.isEmpty()) throw new ExcepcionLineaInvalida("La línea esta vacía");
        String[] campos = linea.split("\t");
        if (campos.length != 6) throw new ExcepcionLineaInvalida("La línea no tiene los campos establecidos");
        try {
            setFecha(campos[0]);
            setHeridos(Integer.parseInt(campos[1]));
            setProfundidad(Double.parseDouble(campos[2]));
            setReplicas(Integer.parseInt(campos[3]));
            setMagnitud(Double.parseDouble(campos[4]));
            setLocalidades(campos[5]);
        } catch (Exception e){
            throw new ExcepcionLineaInvalida("Linea invalida, información incorrecta" );
        }
    }

    /**
     * Actualiza los valores del sismo con los del sismo recibido.
     * @param sismo el sismo con el cual actualizar los valores.
     * @throws IllegalArgumentException si el sismo es <code>null</code>.
     */
    public void actualiza(Sismo sismo) {
		if(sismo == null) throw new IllegalArgumentException("No puede actualizarse a un sismo null") ;
        setFecha(sismo.getFecha());
        setProfundidad(sismo.getProfundidad());
        setReplicas(sismo.getReplicas());
        setHeridos(sismo.getHeridos());
        setMagnitud(sismo.getMagnitud());
        setLocalidades(sismo.getLocalidades());
    }

    /**
     * Nos dice si el sismo casa el valor dado en el campo especificado.
     * @param campo el campo que hay que casar.
     * @param valor el valor con el que debe casar el campo del registro.
     * @return <code>true</code> si:
     *         <ul>
     *           <li><code>campo</code> es {@link CampoSismo#FECHA} y
     *              <code>valor</code> es instancia de {@link String} y es una
     *              subcadena del fecha del sismo.</li>
     *           <li><code>campo</code> es {@link CampoSismo#HERIDOS} y
     *              <code>valor</code> es instancia de {@link Integer} y su
     *              valor entero es menor o igual a la heridos del
     *              sismo.</li>
     *           <li><code>campo</code> es {@link CampoSismo#PROFUNDIDAD} y
     *              <code>valor</code> es instancia de {@link Double} y su
     *              valor doble es menor o igual al profundidad del
     *              sismo.</li>
     *           <li><code>campo</code> es {@link CampoSismo#REPLICAS} y
     *              <code>valor</code> es instancia de {@link Integer} y su
     *              valor entero es menor o igual a la replicas del
     *              sismo.</li>
     *           <li><code>campo</code> es {@link CampoSismo#MAGNITUD} y
     *              <code>valor</code> es instancia de {@link Integer} y su
     *              valor doble es menor o igual a la magnitud del
     *              sismo.</li>
     *               <li><code>campo</code> es {@link CampoSismo#LOCALIDADES} y
     *              <code>valor</code> es instancia de {@link String} y es una
     *              subcadena de las localidades del sismo.</li>
     *         </ul>
     *         <code>false</code> en otro caso.
     * @throws IllegalArgumentException si el campo es <code>null</code>.
     */
    @Override public boolean casa(CampoSismo campo, Object valor) {
        if(!(campo instanceof CampoSismo)) 
            throw new IllegalArgumentException("El valor pasado como campo debe ser un CampoSismo válido");
        CampoSismo c =(CampoSismo) campo;
        switch (c){
            case FECHA:
                return casaFecha(valor);
            case HERIDOS :
                return casaHeridos(valor);
            case REPLICAS:
                return casaReplicas(valor);
            case PROFUNDIDAD:
                return casaProfundidad(valor);
            case MAGNITUD:
                return casaMagnitud(valor);
            case LOCALIDADES:
                return casaLocalidades(valor);
            default:
                return false;
        }
    }
    
    private boolean casaFecha(Object o) {
        if(!(o instanceof String)) return false;
        String v = (String) o;
        if(v.isEmpty()) return false;
        return fecha.getValue().contains(v);
    }

    private boolean casaHeridos(Object o) {
        if(!(o instanceof Integer)) return false;
        Integer v = (Integer) o;
        return v.intValue() <= heridos.get();
    }

    private boolean casaReplicas(Object o) {
        if(!(o instanceof Integer)) return false;
        Integer v = (Integer) o;
        return v.intValue() <= replicas.get();
    }

    private boolean casaProfundidad(Object o) {
        if(!(o instanceof Double)) return false;
        Double v = (Double) o;
        return  v.doubleValue() <= profundidad.get();
    }

    private boolean casaMagnitud(Object o) {
        if(!(o instanceof Double)) return false;
        Double v = (Double) o;
        return  v.doubleValue() <= magnitud.get();
    }

    private boolean casaLocalidades(Object o) {
        if(!(o instanceof String)) return false;
        String v = (String) o;
        if(v.isEmpty()) return false;
        return localidades.getValue().contains(v);
    }
}
