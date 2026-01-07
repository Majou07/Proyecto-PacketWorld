package clienteescritorio;

import clienteescritorio.dominio.ClienteImp;
import clienteescritorio.dto.Respuesta;
import clienteescritorio.pojo.Cliente;
import clienteescritorio.utilidad.Utilidades;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXMLClientesController implements Initializable {
    @FXML private TableView<Cliente> tvClientes;
    @FXML private TableColumn colNombre, colTelefono, colCorreo, colCP;
    @FXML private TextField tfBusqueda;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarDatos();
    }

    private void configurarTabla() {
        colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        colTelefono.setCellValueFactory(new PropertyValueFactory("telefono"));
        colCorreo.setCellValueFactory(new PropertyValueFactory("correoElectronico"));
        colCP.setCellValueFactory(new PropertyValueFactory("codigoPostal"));
    }

    private void cargarDatos() {
        HashMap<String, Object> respuesta = ClienteImp.obtenerClientes();
        if (!(boolean) respuesta.get("error")) {
            List<Cliente> lista = (List<Cliente>) respuesta.get("clientes");
            tvClientes.setItems(FXCollections.observableArrayList(lista));
        }
    }

    
    @FXML private void clicRegistrar(ActionEvent event) {
        irFormulario(null);
    }
    
    
    @FXML private void clicEditar(ActionEvent event) {  
        Cliente seleccionado = tvClientes.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                irFormulario(seleccionado);
            } else {
                Utilidades.mostrarAlertaSimple("Selección", 
                    "Por favor, selecciona un cliente de la tabla para editar.", 
                    Alert.AlertType.WARNING);
            }
    }
    
    
    @FXML private void clicEliminar(ActionEvent event) {  
        Cliente seleccionado = tvClientes.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            boolean confirmar = Utilidades.mostrarAlertaConfirmacion("Eliminar cliente", 
                "¿Estás seguro de eliminar al cliente " + seleccionado.getNombre() + "?");
            
            if (confirmar) {
                // Debes tener el método eliminar en tu ClienteImp de escritorio
                Respuesta respuesta = ClienteImp.eliminar(seleccionado.getIdCliente());
                if (!respuesta.isError()) {
                    Utilidades.mostrarAlertaSimple("Éxito", respuesta.getMensaje(), Alert.AlertType.INFORMATION);
                    cargarDatos(); // Refresca la tabla
                } else {
                    Utilidades.mostrarAlertaSimple("Error", respuesta.getMensaje(), Alert.AlertType.ERROR);
                }
            }
        } else {
            Utilidades.mostrarAlertaSimple("Selección", 
                "Selecciona un cliente para eliminar.", Alert.AlertType.WARNING);
        }
    }
    
    
    private void irFormulario(Cliente cliente) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLFormularioCliente.fxml"));
            Parent root = loader.load();
            
            FXMLFormularioClienteController controller = loader.getController();
            controller.inicializarValores(cliente); // Este método lo debes crear en el controlador del formulario
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(cliente == null ? "Registrar Cliente" : "Editar Cliente");
            stage.initModality(Modality.APPLICATION_MODAL); 
            stage.showAndWait();
            
            cargarDatos(); 
        } catch (IOException ex) {
            ex.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", "No se pudo cargar la ventana del formulario.", Alert.AlertType.ERROR);
        }
    }
}