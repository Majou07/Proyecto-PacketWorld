package clienteescritorio;

import clienteescritorio.dominio.EnvioImp;
import clienteescritorio.pojo.Envio;
import clienteescritorio.utilidad.Utilidades;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class FXMLEnviosController implements Initializable {

    @FXML private TableView<Envio> tvEnvios;
    @FXML private TableColumn colGuia;
    @FXML private TableColumn colCliente;
    @FXML private TableColumn colOrigen;
    @FXML private TableColumn colDestino;
    @FXML private TableColumn colConductor;
    @FXML private TableColumn colEstado;
    
    private ObservableList<Envio> envios;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarEnvios();
        
        tvEnvios.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2 && tvEnvios.getSelectionModel().getSelectedItem() != null){
                abrirModalEstatus(tvEnvios.getSelectionModel().getSelectedItem());
            }
        });
    }

    private void configurarTabla() {
        colGuia.setCellValueFactory(new PropertyValueFactory("numeroGuia"));
        colCliente.setCellValueFactory(new PropertyValueFactory("nombreCliente"));
        colOrigen.setCellValueFactory(new PropertyValueFactory("sucursalOrigen"));
        colEstado.setCellValueFactory(new PropertyValueFactory("estatusEnvio"));
    }

    private void cargarEnvios() {
        HashMap<String, Object> respuesta = EnvioImp.obtenerEnvios();
        if(!(boolean)respuesta.get("error")){
            envios = FXCollections.observableArrayList((List<Envio>)respuesta.get("envios"));
            tvEnvios.setItems(envios);
        } else {
            Utilidades.mostrarAlertaSimple("Error", (String)respuesta.get("mensaje"), Alert.AlertType.ERROR);
        }
    }

    private void abrirModalEstatus(Envio envio) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLModalEstatus.fxml"));
            Parent root = loader.load();
            FXMLModalEstatusController controller = loader.getController();
            controller.inicializarEnvio(envio);
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            cargarEnvios(); 
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML 
    private void btnRegistrarEnvio(ActionEvent event) {
    }
    
    @FXML 
    private void btnAsignarConductor(ActionEvent event) {
    }
    
    @FXML 
    private void btnAgregarPaquete(ActionEvent event) {
        Envio envioSeleccionado = tvEnvios.getSelectionModel().getSelectedItem();
        if (envioSeleccionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLFormularioPaquete.fxml"));
                Parent root = loader.load();
                FXMLFormularioPaqueteController ctrl = loader.getController();
                ctrl.setEnvio(envioSeleccionado.getIdEnvio());
                
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.showAndWait();
                // Recargar paquetes
            } catch(Exception e) { e.printStackTrace(); }
        } else {
            Utilidades.mostrarAlertaSimple("Selección", "Selecciona un envío", Alert.AlertType.WARNING);
        }
    }
    
    
    @FXML
    private void clicVerDetalles(ActionEvent event) {
        Envio seleccionado = tvEnvios.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDetalleEnvio.fxml"));
                Parent root = loader.load();

                FXMLDetalleEnvioController controller = loader.getController();
                controller.inicializarDetalles(seleccionado); 

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Detalle del Envío: " + seleccionado.getNumeroGuia());
                stage.show();
            } catch (IOException ex) { ex.printStackTrace(); }
        }
    }
}