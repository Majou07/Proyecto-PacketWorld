package clienteescritorio;

import clienteescritorio.utilidad.Utilidades;
import clienteescritorio.pojo.Unidad;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXMLUnidadesController implements Initializable {

    @FXML private TextField tfBusqueda;
    @FXML private TableView<Unidad> tvUnidades;
    @FXML private TableColumn colMarca;
    @FXML private TableColumn colModelo;
    @FXML private TableColumn colAnio;
    @FXML private TableColumn colVin;
    @FXML private TableColumn colNii;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarDatos();
    }    

    private void configurarTabla() {
        colMarca.setCellValueFactory(new PropertyValueFactory("marca"));
        colModelo.setCellValueFactory(new PropertyValueFactory("modelo"));
        colAnio.setCellValueFactory(new PropertyValueFactory("anio"));
        colVin.setCellValueFactory(new PropertyValueFactory("vin"));
        colNii.setCellValueFactory(new PropertyValueFactory("nii"));
    }
    
    private void cargarDatos() {
        HashMap<String, Object> respuesta = UnidadImp.obtenerUnidades();
        if(!(boolean)respuesta.get("error")){
            tvUnidades.setItems(FXCollections.observableArrayList((List<Unidad>)respuesta.get("unidades")));
        } else {
            Utilidades.mostrarAlertaSimple("Error", (String)respuesta.get("mensaje"), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicRegistrar(ActionEvent event) {
        irFormulario(null);
    }

    @FXML
    private void clicEditar(ActionEvent event) {
        Unidad seleccionada = tvUnidades.getSelectionModel().getSelectedItem();
        if(seleccionada != null){
            irFormulario(seleccionada);
        } else {
             Utilidades.mostrarAlertaSimple("Selección", "Selecciona una unidad", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicDarBaja(ActionEvent event) {
    }
    
    private void irFormulario(Unidad unidad) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLFormularioUnidad.fxml"));
            Parent root = loader.load();
            FXMLFormularioUnidadController controller = loader.getController();
            controller.inicializarValores(unidad);
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            cargarDatos();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}