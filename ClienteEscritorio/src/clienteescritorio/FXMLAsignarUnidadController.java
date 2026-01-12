package clienteescritorio;

import clienteescritorio.dominio.UnidadImp;
import clienteescritorio.dominio.ColaboradorImp;
import clienteescritorio.dto.Respuesta;
import clienteescritorio.pojo.Colaborador;
import clienteescritorio.pojo.Unidad;
import clienteescritorio.utilidad.Utilidades;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class FXMLAsignarUnidadController implements Initializable {
    @FXML private Label lbConductor;
    @FXML private ComboBox<Unidad> cbUnidades;
    private Colaborador conductor;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarUnidades();

        // Personalizar cómo se muestran las unidades en el ComboBox
        cbUnidades.setCellFactory(param -> new ListCell<Unidad>() {
            @Override
            protected void updateItem(Unidad unidad, boolean empty) {
                super.updateItem(unidad, empty);
                if (empty || unidad == null) {
                    setText(null);
                } else {
                    setText(unidad.getVin() + " - " + unidad.getMarca() + " " + unidad.getModelo() + " (" + unidad.getAnio() + ")");
                }
            }
        });

        cbUnidades.setButtonCell(new ListCell<Unidad>() {
            @Override
            protected void updateItem(Unidad unidad, boolean empty) {
                super.updateItem(unidad, empty);
                if (empty || unidad == null) {
                    setText(null);
                } else {
                    setText(unidad.getVin() + " - " + unidad.getMarca() + " " + unidad.getModelo() + " (" + unidad.getAnio() + ")");
                }
            }
        });
    }

    public void inicializarConductor(Colaborador conductor) {
        this.conductor = conductor;
        this.lbConductor.setText("Conductor: " + conductor.getNombre() + " " + conductor.getApellidoPaterno());
    }

    private void cargarUnidades() {
        HashMap<String, Object> respuesta = UnidadImp.obtenerUnidades(); 
        
        if (!(boolean) respuesta.get("error")) {
            List<Unidad> lista = (List<Unidad>) respuesta.get("unidades");
            cbUnidades.setItems(FXCollections.observableArrayList(lista));
        } else {
            Utilidades.mostrarAlertaSimple("Error", 
                (String) respuesta.get("mensaje"), 
                Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicGuardar(ActionEvent event) {
        Unidad seleccionada = cbUnidades.getValue();
        if (seleccionada == null) {
            Utilidades.mostrarAlertaSimple("Selección requerida", 
                "Debes seleccionar una unidad antes de asignarla.", 
                Alert.AlertType.WARNING);
            return;
        }

        Respuesta resp = ColaboradorImp.asignarVehiculo(conductor.getIdColaborador(), seleccionada.getIdUnidad());
        
        if (!resp.isError()) {
            Utilidades.mostrarAlertaSimple("Éxito", resp.getMensaje(), Alert.AlertType.INFORMATION);
            cerrarVentana();
        } else {
            Utilidades.mostrarAlertaSimple("Error", resp.getMensaje(), Alert.AlertType.ERROR);
        }
    }

    @FXML 
    private void clicCancelar(ActionEvent event) { 
        cerrarVentana(); 
    }
    
    private void cerrarVentana() { 
        ((Stage) lbConductor.getScene().getWindow()).close(); 
    }
}
