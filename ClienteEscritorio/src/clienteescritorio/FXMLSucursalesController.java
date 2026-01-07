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
        HashMap<String, Object> respuesta = SucursalImp.obtenerSucursales();
        if(!(boolean)respuesta.get("error")){
            tvSucursales.setItems(FXCollections.observableArrayList((List<Sucursal>)respuesta.get("sucursales")));
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
    if (seleccionada != null) {
        // Validar si ya está inactiva
        if ("inactiva".equalsIgnoreCase(seleccionada.getEstatus())) {
            Utilidades.mostrarAlertaSimple("Aviso",
                    "La sucursal seleccionada ya está inactiva",
                    Alert.AlertType.INFORMATION);
            return; // salir sin llamar al API
        }

        // Ventana de confirmación
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar baja");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Deseas dar de baja la sucursal seleccionada?");
        
        // Mostrar y esperar respuesta
        confirmacion.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                // Llamar al API
                Respuesta resp = SucursalImp.darBajaSucursal(seleccionada.getCodigoSucursal());
                Utilidades.mostrarAlertaSimple("Resultado", resp.getMensaje(),
                        resp.isError() ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
                if (!resp.isError()) {
                    cargarDatos(); // refrescar tabla
                }
            }
        });
    } else {
        Utilidades.mostrarAlertaSimple("Selección",
                "Selecciona una sucursal para dar de baja",
                Alert.AlertType.WARNING);
    }
}


@FXML
private void clicReactivar(ActionEvent event) {
    Sucursal seleccionada = tvSucursales.getSelectionModel().getSelectedItem();
    if (seleccionada != null) {
        // Llamar al API
        Respuesta resp = SucursalImp.reactivarSucursal(seleccionada.getCodigoSucursal());
        // Mostrar mensaje
        Utilidades.mostrarAlertaSimple("Resultado", resp.getMensaje(),
                resp.isError() ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        // Refrescar tabla si fue exitoso
        if (!resp.isError()) {
            cargarDatos();
        }
    } else {
        Utilidades.mostrarAlertaSimple("Selección",
                "Selecciona una sucursal para reactivar",
                Alert.AlertType.WARNING);
    }
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
