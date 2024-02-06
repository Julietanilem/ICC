package mx.unam.ciencias.icc.igu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import mx.unam.ciencias.icc.CampoSismo;

/**
 * Clase para el controlador del contenido del diálogo para buscar sismos.
 */
public class ControladorFormaBuscaSismos
    extends ControladorFormaSismo {

    /* El combo del campo. */
    @FXML private ComboBox<CampoSismo> opcionesCampo;
    /* El campo de texto para el valor. */
    @FXML private EntradaVerificable entradaValor;

    /* Inicializa el estado de la forma. */
    @FXML private void initialize() {
        entradaValor.setVerificador(s -> verificaValor(s));
        entradaValor.textProperty().addListener(
            (o, v, n) -> revisaValor(null));
    }

    /* Revisa el valor después de un cambio. */
    @FXML private void revisaValor(ActionEvent evento) {
        Tooltip.install(entradaValor, getTooltip());
        botonAceptar.setDisable(!entradaValor.esValida());
    }

    /* Manejador para cuando se activa el botón aceptar. */
    @FXML private void aceptar(ActionEvent evento) {
        aceptado = true;
        escenario.close();
    }

    /* Verifica el valor. */
    private boolean verificaValor(String valor) {
        switch (opcionesCampo.getValue()) {
            case FECHA:       return verificaFecha(valor);
            case HERIDOS:     return verificaHeridos(valor);
            case PROFUNDIDAD: return verificaProfundidad(valor);
            case REPLICAS:    return verificaReplicas(valor);
            case MAGNITUD:    return verificaMagnitud(valor);
            case LOCALIDADES: return verificaLocalidades(valor);
            default:          return false;  // No puede ocurrir.
        }
    }

    /* Obtiene la pista. */
    private Tooltip getTooltip() {
        String m = "";
        switch (opcionesCampo.getValue()) {
            case FECHA:
                m = "Buscar por fecha necesita al menos un carácter";
                break;
            case HERIDOS:
                m = "Buscar por heridos necesita un número mayor o igual a 0";
                break;
            case PROFUNDIDAD:
                m = "Buscar por profundidad necesita un número entre 0.5 y 6000";
                break;
            case REPLICAS:
                m = "Buscar por réplicas necesita un número entre 0 y 100";
                break;
            case MAGNITUD:
                m = "Buscar por magnitud necesita un número entre 0.0 y 10.0";
                break;
            case LOCALIDADES:
                m = "Buscar por localidades necesita al menos un carácter";
                break;
        }
        return new Tooltip(m);
    }

    /**
     * Regresa el valor ingresado.
     * @return el valor ingresado.
     */
    public Object getValor() {
        switch (opcionesCampo.getValue()) {
            case FECHA:
                return entradaValor.getText();
            case HERIDOS :
                return Integer.parseInt(entradaValor.getText());
            case PROFUNDIDAD :
                return Double.parseDouble(entradaValor.getText());
            case REPLICAS :
                return Integer.parseInt(entradaValor.getText());
            case MAGNITUD :
                return Double.parseDouble(entradaValor.getText());
            case LOCALIDADES:
                return entradaValor.getText();
            default:       
                return entradaValor.getText(); // No puede ocurrir.
        }
    }

    /**
     * Regresa el campo seleccionado.
     * @return el campo seleccionado.
     */
    public CampoSismo getCampo() {
        return opcionesCampo.getValue();
    }

    /**
     * Define el foco incial del diálogo.
     */
    @Override public void defineFoco() {
        entradaValor.requestFocus();
    }
}
