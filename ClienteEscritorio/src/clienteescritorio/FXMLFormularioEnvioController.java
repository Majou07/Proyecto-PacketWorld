package clienteescritorio;

import clienteescritorio.dominio.ClienteImp;
import clienteescritorio.dominio.EnvioImp;
import clienteescritorio.dominio.SucursalImp;
import clienteescritorio.dto.Respuesta;
import clienteescritorio.pojo.Cliente;
import clienteescritorio.pojo.Sucursal;
import clienteescritorio.pojo.Envio;
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

public class FXMLFormularioEnvioController implements Initializable {
    @FXML private ComboBox<Cliente> cbCliente;
    @FXML private ComboBox<Sucursal> cbSucursal;
    @FXML private TextField tfNombreDest;
    @FXML private TextField tfCalle;
    @FXML private TextField tfCiudad;
    @FXML private TextField tfCP;
    @FXML private TextField tfEstado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarCatalogos();
    }    

    private void cargarCatalogos(){
        HashMap<String, Object> respClientes = ClienteImp.obtenerClientes();
        cbCliente.setItems(FXCollections.observableArrayList((List<Cliente>)respClientes.get("clientes")));
        
        HashMap<String, Object> respSuc = SucursalImp.obtenerSucursales();
        cbSucursal.setItems(FXCollections.observableArrayList((List<Sucursal>)respSuc.get("sucursales")));
    }

    
    @FXML
    private void clicGuardar(ActionEvent event) {
        Cliente cliente = cbCliente.getSelectionModel().getSelectedItem();
        Sucursal sucursal = cbSucursal.getSelectionModel().getSelectedItem();

        if (cliente != null && sucursal != null && !tfNombreDest.getText().isEmpty() && !tfCP.getText().isEmpty()) {
            // Creamos el objeto de envio
            Envio nuevoEnvio = new Envio();
            nuevoEnvio.setIdClienteRemitente(cliente.getIdCliente());
            nuevoEnvio.setCodigoSucursalOrigen(sucursal.getCodigoSucursal());
            nuevoEnvio.setDestinatarioNombre(tfNombreDest.getText());
            nuevoEnvio.setDestinoCalle(tfCalle.getText());
            nuevoEnvio.setDestinoCiudad(tfCiudad.getText());
            nuevoEnvio.setDestinoCodigoPostal(tfCP.getText());
            nuevoEnvio.setDestinoEstado(tfEstado.getText());

            // El costo inicial es 0.0 porque se recalcula al agregar paquetes
            nuevoEnvio.setCostoTotal(0.0);

            // Estatus inicial: 1 - "Recibido en sucursal" 
            nuevoEnvio.setIdEstatusEnvio(1);

            // Llamamos al método registrar (debes tenerlo en EnvioImp de escritorio)
            Respuesta resp = EnvioImp.registrar(nuevoEnvio);

            if (!resp.isError()) {
                Utilidades.mostrarAlertaSimple("Éxito", "Envío registrado correctamente. Guía: " + resp.getMensaje(), Alert.AlertType.INFORMATION);
                cerrarVentana();
            } else {
                Utilidades.mostrarAlertaSimple("Error", resp.getMensaje(), Alert.AlertType.ERROR);
            }
        } else {
            Utilidades.mostrarAlertaSimple("Campos vacíos", "Por favor rellena todos los datos obligatorios.", Alert.AlertType.WARNING);
        }
    }

    private void cerrarVentana() {
        ((Stage) tfNombreDest.getScene().getWindow()).close();
    }
}