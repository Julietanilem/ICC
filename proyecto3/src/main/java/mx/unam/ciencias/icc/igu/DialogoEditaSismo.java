package mx.unam.ciencias.icc.igu;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mx.unam.ciencias.icc.Sismo;

/**
 * Clase para diálogos con formas para editar sismos.
 */
public class DialogoEditaSismo extends Stage {

    /* Vista de la forma para agregar/editar sismos. */
    private static final String EDITA_SISMO_FXML =
        "fxml/forma-edita-sismo.fxml";

    /* El controlador. */
    private ControladorFormaEditaSismo controlador;

    /**
     * Define el estado inicial de un diálogo para sismo.
     * @param escenario el escenario al que el diálogo pertenece.
     * @param sismo el sismo; puede ser <code>null</code> para agregar
     *                   un sismo.
     * @throws IOException si no se puede cargar el archivo FXML.
     */
    public DialogoEditaSismo(Stage escenario,
                                Sismo sismo) throws IOException {
        ClassLoader cl = getClass().getClassLoader();
        FXMLLoader cargador =
            new FXMLLoader(cl.getResource(EDITA_SISMO_FXML));
        AnchorPane cristal = (AnchorPane)cargador.load();

        if (sismo == null)
            setTitle("Agregar sismo");
        else
            setTitle("Editar sismo");
        initOwner(escenario);
        initModality(Modality.WINDOW_MODAL);
        Scene escena = new Scene(cristal);
        setScene(escena);

        controlador = cargador.getController();
        controlador.setEscenario(this);
        controlador.setSismo(sismo);
        if (sismo == null)
            controlador.setVerbo("Agregar");
        else
            controlador.setVerbo("Actualizar");

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
     * Regresa el sismo del diálogo.
     * @return el sismo del diálogo.
     */
    public Sismo getSismo() {
        return controlador.getSismo();
    }
}
