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
    @FXML private TableColumn  colTelefono, colCorreo, colCP;
    @FXML private TableColumn<Cliente, String> colDireccion;
    @FXML private TableColumn<Cliente, String> colNombre;
    @FXML private TextField tfBusqueda;
    @FXML
    private Button btBusqueda;
    @FXML
    private ComboBox<String> cbFiltro;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    configurarTabla();
    cargarDatos();
    
    cbFiltro.getItems().addAll("Nombre", "Teléfono", "Correo");
    cbFiltro.setValue("Nombre");
}
    
    
    

    private void configurarTabla() {
    // Nombre completo
    colNombre.setCellValueFactory(cellData -> {
        Cliente c = cellData.getValue();
        String nombreCompleto =
                (c.getNombre() != null ? c.getNombre() : "") + " " +
                (c.getApellidoPaterno() != null ? c.getApellidoPaterno() : "") + " " +
                (c.getApellidoMaterno() != null ? c.getApellidoMaterno() : "");
        return new javafx.beans.property.SimpleStringProperty(nombreCompleto.trim());
    });

    colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
    colCorreo.setCellValueFactory(new PropertyValueFactory<>("correoElectronico"));
    colCP.setCellValueFactory(new PropertyValueFactory<>("codigoPostal"));

    // Dirección completa
    colDireccion.setCellValueFactory(cellData -> {
        Cliente c = cellData.getValue();
        String direccion =
                (c.getCalle() != null ? c.getCalle() : "") + " " +
                (c.getNumero() != null ? c.getNumero() : "") + ", " +
                (c.getColonia() != null ? c.getColonia() : "");
        return new javafx.beans.property.SimpleStringProperty(direccion.trim());
    });
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

    @FXML
    private void clicBuscar(ActionEvent event) {
    String filtro = cbFiltro.getValue();       // "Nombre", "Teléfono" o "Correo"
    String valor = tfBusqueda.getText().trim(); // texto ingresado en el campo

    if (valor.isEmpty()) {
        // Si no hay texto, recargamos todos los clientes
        cargarDatos();
        return;
    }

    HashMap<String, Object> respuesta = null;

    switch (filtro) {
        case "Nombre":
            respuesta = ClienteImp.buscarPorNombre(valor);
            break;
        case "Teléfono":
            respuesta = ClienteImp.buscarPorTelefono(valor);
            break;
        case "Correo":
            respuesta = ClienteImp.buscarPorCorreo(valor);
            break;
    }

    if (respuesta != null && !(boolean) respuesta.get("error")) {
        List<Cliente> lista = (List<Cliente>) respuesta.get("clientes");
        tvClientes.setItems(FXCollections.observableArrayList(lista));

        if (lista.isEmpty()) {
            Utilidades.mostrarAlertaSimple("Sin resultados",
                    "No se encontraron clientes con ese criterio.",
                    Alert.AlertType.INFORMATION);
        }
    } else {
        Utilidades.mostrarAlertaSimple("Error",
                (respuesta != null ? (String) respuesta.get("mensaje") : "Error en la búsqueda"),
                Alert.AlertType.ERROR);
    }
}
}