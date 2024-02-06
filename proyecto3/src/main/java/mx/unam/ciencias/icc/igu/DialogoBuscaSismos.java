package mx.unam.ciencias.icc.igu;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mx.unam.ciencias.icc.CampoSismo;

/**
 * Clase para diálogos con formas de búsquedas de sismos.
 */
public class DialogoBuscaSismos extends Stage {

    /* Vista de la forma para realizar búsquedas de sismos. */
    private static final String BUSCA_SISMOS_FXML =
        "fxml/forma-busca-sismos.fxml";

    /* El controlador. */
    private ControladorFormaBuscaSismos controlador;

    /**
     * Define el estado inicial de un diálogo para búsquedas de sismos.
     * @param escenario el escenario al que el diálogo pertenece.
     * @throws IOException si no se puede cargar el archivo FXML.
     */
    public DialogoBuscaSismos(Stage escenario) throws IOException {
        ClassLoader cl = getClass().getClassLoader();
        FXMLLoader cargador = new FXMLLoader(
            cl.getResource(BUSCA_SISMOS_FXML));
        AnchorPane cristal = (AnchorPane)cargador.load();

        setTitle("Buscar sismos");
        initOwner(escenario);
        initModality(Modality.WINDOW_MODAL);
        Scene escena = new Scene(cristal);
        setScene(escena);
        controlador = cargador.getController();
        controlador.setEscenario(this);
        setOnShown(w -> controlador.defineFoco());
        setResizable(false);
    }

    /**
     * Nos dice si el usuario activó el botón de aceptar.
     * @return <code>true</code> si el usuario activó el botón de aceptar,
     *         <code>false</code> en otro caso.
     */
    public boolean isAceptado() {
        return controlador.isAceptado();
    }

    /**
     * Regresa el campo seleccionado.
     * @return el campo seleccionado.
     */
    public CampoSismo getCampo() {
        return controlador.getCampo();
    }

    /**
     * Regresa el valor ingresado.
     * @return el valor ingresado.
     */
    public Object getValor() {
        return controlador.getValor();
    }
}
