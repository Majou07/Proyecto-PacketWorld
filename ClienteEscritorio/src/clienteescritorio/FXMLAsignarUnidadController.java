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
        }
    }

    @FXML
    private void clicGuardar(ActionEvent event) {
        Unidad seleccionada = cbUnidades.getValue();
        if (seleccionada != null) {
            Respuesta resp = ColaboradorImp.asignarVehiculo(conductor.getIdColaborador(), seleccionada.getIdUnidad());
            
            if (!resp.isError()) {
                Utilidades.mostrarAlertaSimple("Éxito", resp.getMensaje(), Alert.AlertType.INFORMATION);
                cerrarVentana();
                
            } else {
                
                Utilidades.mostrarAlertaSimple("Error", resp.getMensaje(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML private void clicCancelar(ActionEvent event) { 
        cerrarVentana(); 
    }
    
    private void cerrarVentana() { 
        ((Stage) lbConductor.getScene().getWindow()).close(); 
    }
}