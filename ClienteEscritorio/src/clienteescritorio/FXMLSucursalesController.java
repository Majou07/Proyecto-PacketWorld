/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package clienteescritorio;

import clienteescritorio.dominio.SucursalImp;
import clienteescritorio.dto.Respuesta;
import clienteescritorio.pojo.Sucursal;
import clienteescritorio.utilidad.Utilidades;
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
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.concurrent.Task;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Bruno
 */
public class FXMLSucursalesController implements Initializable {

    @FXML
    private TableColumn colCodigo;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colEstatus;
    @FXML
    private TableColumn colCalle;
    @FXML
    private TableColumn colNumero;
    @FXML
    private TableColumn colColonia;
    @FXML
    private TableColumn colCodigoPostal;
    @FXML
    private TableColumn colCiudad;
    @FXML
    private TableColumn colEstado;
    @FXML
    private TableView<Sucursal> tvSucursales;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        configurarTabla();
        cargarDatos();
    }
    
     private void configurarTabla() {
        colCodigo.setCellValueFactory(new PropertyValueFactory("codigoSucursal"));
        colNombre.setCellValueFactory(new PropertyValueFactory("nombreCorto"));
        colEstatus.setCellValueFactory(new PropertyValueFactory("estatus"));
        colCalle.setCellValueFactory(new PropertyValueFactory("calle"));
        colNumero.setCellValueFactory(new PropertyValueFactory("numero"));
        colColonia.setCellValueFactory(new PropertyValueFactory("colonia"));
        colCodigoPostal.setCellValueFactory(new PropertyValueFactory("codigoPostal"));
        colCiudad.setCellValueFactory(new PropertyValueFactory("ciudad")); 
        colEstado.setCellValueFactory(new PropertyValueFactory("estado"));
       
    }
    
    private void cargarDatos() {

    Task<HashMap<String, Object>> task = new Task<HashMap<String, Object>>() {
        @Override
        protected HashMap<String, Object> call() {
            return SucursalImp.obtenerSucursales();
        }
    };

    task.setOnSucceeded(e -> {

        HashMap<String, Object> respuesta = task.getValue();

        if (!(boolean) respuesta.get("error")) {

            List<Sucursal> lista = (List<Sucursal>) respuesta.get("sucursales");
            tvSucursales.setItems(FXCollections.observableArrayList(lista));

        } else {
            Utilidades.mostrarAlertaSimple(
                    "Error",
                    respuesta.get("mensaje").toString(),
                    Alert.AlertType.ERROR
            );
        }
    });

    task.setOnFailed(e -> {
        tvSucursales.setPlaceholder(
                new Label("Error al cargar sucursales")
        );
    });

    tvSucursales.setPlaceholder(new Label("Cargando sucursales..."));

    new Thread(task).start();
}

    

    @FXML
    private void clicRegistrar(ActionEvent event) {
        irFormulario(null);
    }

    @FXML
    private void clicEditar(ActionEvent event) {
        Sucursal seleccionada = tvSucursales.getSelectionModel().getSelectedItem();
        if(seleccionada != null){
            irFormulario(seleccionada);
        } else {
             Utilidades.mostrarAlertaSimple("Selección", "Selecciona una sucursal para editar", Alert.AlertType.WARNING);
        }
    }

  @FXML
private void clicDarBaja(ActionEvent event) {

    Sucursal seleccionada = tvSucursales.getSelectionModel().getSelectedItem();

    if (seleccionada == null) {
        Utilidades.mostrarAlertaSimple(
                "Selección",
                "Selecciona una sucursal para dar de baja",
                Alert.AlertType.WARNING
        );
        return;
    }

    if ("inactiva".equalsIgnoreCase(seleccionada.getEstatus())) {
        Utilidades.mostrarAlertaSimple(
                "Aviso",
                "La sucursal ya está inactiva",
                Alert.AlertType.INFORMATION
        );
        return;
    }

    Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
    confirmacion.setTitle("Confirmar baja");
    confirmacion.setHeaderText(null);
    confirmacion.setContentText("¿Deseas dar de baja la sucursal seleccionada?");

    confirmacion.showAndWait().ifPresent(response -> {

        if (response == javafx.scene.control.ButtonType.OK) {

            Task<Respuesta> task = new Task<Respuesta>() {
                @Override
                protected Respuesta call() {
                    return SucursalImp.darBajaSucursal(seleccionada.getCodigoSucursal());
                }
            };

            task.setOnSucceeded(e -> {

                Respuesta resp = task.getValue();

                Utilidades.mostrarAlertaSimple(
                        "Resultado",
                        resp.getMensaje(),
                        resp.isError()
                                ? Alert.AlertType.ERROR
                                : Alert.AlertType.INFORMATION
                );

                if (!resp.isError()) {
                    cargarDatos();
                }
            });

            new Thread(task).start();
        }
    });
}


@FXML
private void clicReactivar(ActionEvent event) {

    Sucursal seleccionada = tvSucursales.getSelectionModel().getSelectedItem();

    if (seleccionada == null) {
        Utilidades.mostrarAlertaSimple(
                "Selección",
                "Selecciona una sucursal para reactivar",
                Alert.AlertType.WARNING
        );
        return;
    }

    Task<Respuesta> task = new Task<Respuesta>() {
        @Override
        protected Respuesta call() {
            return SucursalImp.reactivarSucursal(seleccionada.getCodigoSucursal());
        }
    };

    task.setOnSucceeded(e -> {

        Respuesta resp = task.getValue();

        Utilidades.mostrarAlertaSimple(
                "Resultado",
                resp.getMensaje(),
                resp.isError()
                        ? Alert.AlertType.ERROR
                        : Alert.AlertType.INFORMATION
        );

        if (!resp.isError()) {
            cargarDatos();
        }
    });

    new Thread(task).start();
}
    
    private void irFormulario(Sucursal sucursal) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLFormularioSucursal.fxml"));
            Parent root = loader.load();
            FXMLFormularioSucursalController controller = loader.getController();
            controller.inicializarValores(sucursal);
            
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
