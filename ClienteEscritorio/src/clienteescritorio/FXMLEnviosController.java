package clienteescritorio;

import clienteescritorio.dominio.EnvioImp;
import clienteescritorio.dominio.PaqueteImp;
import clienteescritorio.pojo.Envio;
import clienteescritorio.pojo.Paquete;
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

    @FXML private TextField tfBusquedaEnvio;
    @FXML private TableView<Envio> tvEnvios;
    @FXML private TableColumn colGuia;
    @FXML private TableColumn colCliente;
    @FXML private TableColumn colOrigen;
    @FXML private TableColumn colDestino;
    @FXML private TableColumn colConductor;
    @FXML private TableColumn colEstado;
    
    @FXML private TableView<Paquete> tvPaquetes;
    @FXML private TableColumn colDescripcion;
    @FXML private TableColumn colPeso;
    @FXML private TableColumn colDimensiones;
    @FXML private TableColumn colEnvioPertence;

    private ObservableList<Envio> envios;
    private ObservableList<Paquete> paquetes;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarEnvios();
        
        tvEnvios.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
        if (newVal != null) {
            cargarPaquetesPorEnvio(newVal.getIdEnvio());
        }
        });

        tvEnvios.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2 && tvEnvios.getSelectionModel().getSelectedItem() != null){
                abrirModalEstatus(tvEnvios.getSelectionModel().getSelectedItem());
            }
        });
    }

    private void configurarTabla() {
        // Envíos
        colGuia.setCellValueFactory(new PropertyValueFactory<>("numeroGuia"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("nombreCliente"));
        colOrigen.setCellValueFactory(new PropertyValueFactory<>("sucursalOrigen"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estatusEnvio"));
        
        // Paquetes
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colPeso.setCellValueFactory(new PropertyValueFactory<>("pesoKg"));
        colDimensiones.setCellValueFactory(new PropertyValueFactory<>("dimensiones")); 
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

    private void cargarPaquetesPorEnvio(int idEnvio) {
        List<Paquete> lista = (List<Paquete>) PaqueteImp.obtenerPaquetesPorEnvio(idEnvio);
        paquetes = FXCollections.observableArrayList(lista);
        tvPaquetes.setItems(paquetes);
    }


    @FXML 
    private void btnHistorial(ActionEvent event) {
        Utilidades.mostrarAlertaSimple("Historial", "Funcionalidad de historial en desarrollo", Alert.AlertType.INFORMATION);
    }

    @FXML 
    private void btnRegistrarEnvio(ActionEvent event) {
        irFormularioEnvio(null);
    }

    @FXML 
    private void btnActualizarEnvio(ActionEvent event) {
        Envio seleccionado = tvEnvios.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            irFormularioEnvio(seleccionado);
        }
    }

    @FXML 
    private void btnEliminarEnvio(ActionEvent event) {
        Envio seleccionado = tvEnvios.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            boolean confirmar = Utilidades.mostrarConfirmacion("Eliminar", "¿Deseas eliminar el envío " + seleccionado.getNumeroGuia() + "?");
            if (confirmar) {
                EnvioImp.eliminarEnvio(seleccionado.getIdEnvio());
                cargarEnvios();
            }
        } else {
            Utilidades.mostrarAlertaSimple("Selección", "Primero selecciona un envío.", Alert.AlertType.WARNING);
        }
    }

    @FXML 
    private void btnAsignarConductor(ActionEvent event) {
    }
    
    private void irFormularioEnvio(Envio envio) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLFormularioEnvio.fxml"));
        Parent root = loader.load();
        
        if (envio != null) {
            FXMLFormularioEnvioController controller = loader.getController();
            controller.prepararFormulario(envio); 
        }
        
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(envio == null ? "Registrar Envío" : "Actualizar Envío");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        
        cargarEnvios(); 
    } catch (IOException ex) {
        ex.printStackTrace();
        Utilidades.mostrarAlertaSimple("Error", "No se pudo cargar el formulario", Alert.AlertType.ERROR);
    }
    }

    
    
    // --- PAQUETES ---
    
    
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
    private void btnEditarPaquete(ActionEvent event) {
        Paquete seleccionado = tvPaquetes.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
        }
    }

    @FXML 
    private void btnEliminarPaquete(ActionEvent event) {
        Paquete selecc = tvPaquetes.getSelectionModel().getSelectedItem();
        if (selecc != null) {
            if (Utilidades.mostrarConfirmacion("Eliminar", "¿Deseas quitar este paquete?")) {
                PaqueteImp.eliminarPaquete(selecc.getIdPaquete());
                cargarPaquetesPorEnvio(selecc.getIdEnvio()); 
            }
        } else {
            Utilidades.mostrarAlertaSimple("Selección", "Elige un paquete", Alert.AlertType.WARNING);
        }
    }
    
    private void configurarBusqueda() {
    tfBusquedaEnvio.textProperty().addListener((observable, oldValue, newValue) -> {
       
    });
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
}