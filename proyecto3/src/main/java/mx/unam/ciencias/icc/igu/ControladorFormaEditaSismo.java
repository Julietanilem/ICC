package mx.unam.ciencias.icc.igu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import mx.unam.ciencias.icc.Sismo;

/**
 * Clase para el controlador del contenido del diálogo para editar y crear
 * sismos.
 */
public class ControladorFormaEditaSismo
    extends ControladorFormaSismo {

    /* La entrada verificable para la fecha. */
    @FXML private EntradaVerificable entradaFecha;
    /* La entrada verificable para el número de heridos. */
    @FXML private EntradaVerificable entradaHeridos;
    /* La entrada verificable para la profundidad. */
    @FXML private EntradaVerificable entradaProfundidad;
    /* La entrada verificable para las replicas. */
    @FXML private EntradaVerificable entradaReplicas;
     /* La entrada verificable para la magnitud. */
    @FXML private EntradaVerificable entradaMagnitud;
    /* La entrada verificable para las localidades */
    @FXML private EntradaVerificable entradaLocalidades;

    /* El sismo creado o editado. */
    private Sismo sismo;

    /* Inicializa el estado de la forma. */
    @FXML private void initialize() {
        entradaFecha.setVerificador(f -> verificaFecha(f));
        entradaHeridos.setVerificador(h -> verificaHeridos(h));
        entradaProfundidad.setVerificador(p -> verificaProfundidad(p));
        entradaReplicas.setVerificador(r -> verificaReplicas(r));
        entradaMagnitud.setVerificador(m -> verificaMagnitud(m));
        entradaLocalidades.setVerificador(l -> verificaLocalidades(l));

        entradaFecha.textProperty().addListener(
            (o, v, n) -> verificaSismo());
        entradaHeridos.textProperty().addListener(
            (o, v, n) -> verificaSismo());
        entradaProfundidad.textProperty().addListener(
            (o, v, n) -> verificaSismo());
        entradaReplicas.textProperty().addListener(
            (o, v, n) -> verificaSismo());
        entradaMagnitud.textProperty().addListener(
            (o, v, n) -> verificaSismo());
        entradaLocalidades.textProperty().addListener(
            (o, v, n) -> verificaSismo());
    }

    /* Manejador para cuando se activa el botón aceptar. */
    @FXML private void aceptar(ActionEvent evento) {
        actualizaSismo();
        aceptado = true;
        escenario.close();
    }

    /* Actualiza al sismo, o lo crea si no existe. */
    private void actualizaSismo() {
        if (sismo != null){
            sismo.setFecha(fecha);
            sismo.setHeridos(heridos);
            sismo.setProfundidad(profundidad);
            sismo.setReplicas(replicas);
            sismo.setMagnitud(magnitud);
            sismo.setLocalidades(localidades);
        }else{
            sismo = new Sismo(fecha, heridos, profundidad, replicas, magnitud, localidades);
        }
    }

    /**
     * Define el sismo del diálogo.
     * @param sismo el nuevo sismo del diálogo.
     */
    public void setSismo(Sismo sismo) {
        this.sismo = sismo;
        if (sismo == null)
            return;
        this.sismo = new Sismo(null, 0, 0, 0, 0, null);
        this.sismo.actualiza(sismo);
        entradaFecha.setText(sismo.getFecha());
        entradaHeridos.setText(String.valueOf(sismo.getHeridos()));
        String p = String.format("%4.1f", sismo.getProfundidad());
        entradaProfundidad.setText(p);
        entradaReplicas.setText(String.valueOf(sismo.getReplicas()));
        String m = String.format("%2.1f", sismo.getMagnitud());
        entradaMagnitud.setText(m);
        entradaLocalidades.setText(sismo.getLocalidades());
    }

    /**
     * Regresa el sismo del diálogo.
     * @return el sismo del diálogo.
     */
    public Sismo getSismo() {
        return sismo;
    }

    /**
     * Define el verbo del botón de aceptar.
     * @param verbo el nuevo verbo del botón de aceptar.
     */
    public void setVerbo(String verbo) {
        botonAceptar.setText(verbo);
    }

    /**
     * Define el foco incial del diálogo.
     */
    @Override public void defineFoco() {
        entradaFecha.requestFocus();
    }

    /* Verifica que los cuatro campos sean válidos. */
    private void verificaSismo() {
        boolean f = entradaFecha.esValida();
        boolean h = entradaHeridos.esValida();
        boolean p = entradaProfundidad.esValida();
        boolean r = entradaReplicas.esValida();
        boolean m = entradaMagnitud.esValida();
        boolean l = entradaLocalidades.esValida();
        botonAceptar.setDisable(!f || !h || !p || !r || !m || !l);
    }

    /**
     * Verifica que el número de heridos sea válido.
     * @param heridos el número de heridos a verificar.
     * @return <code>true</code> si el número de heridos es válido;
     *         <code>false</code> en otro caso.
     */
    @Override protected boolean verificaHeridos(String heridos) {
        return super.verificaHeridos(heridos) &&
            this.heridos >= 0;
    }

    /**
     * Verifica que la profundidad sea válida.
     * @param profundidad la profundidad a verificar.
     * @return <code>true</code> si la profundidad es válida; <code>false</code> en
     *         otro caso.
     */
    @Override protected boolean verificaProfundidad(String profundidad) {
        return super.verificaProfundidad(profundidad) &&
            this.profundidad >= 0.5 && this.profundidad <= 60000;
    }

    /**
     * Verifica que las replicas sean válidas.
     * @param replicas las replicas a verificar.
     * @return <code>true</code> si las replicas son válidas; <code>false</code> en
     *         otro caso.
     */
    @Override protected boolean verificaReplicas(String replicas) {
        return super.verificaReplicas(replicas) &&
            this.replicas >= 0 && this.replicas <= 100;
    }

    /**
     * Verifica que la magnitud sea válida.
     * @param magnitud la magnitud a verificar.
     * @return <code>true</code> si la magnitud es válida; <code>false</code> en
     *         otro caso.
     */
    @Override protected boolean verificaMagnitud(String magnitud) {
        return super.verificaMagnitud(magnitud) &&
            this.magnitud >= 0.0 && this.magnitud <= 10.0;
    }
}
