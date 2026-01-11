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
    private Envio envioEdicion;
    private boolean esEdicion = false;

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
    
     
    public void prepararFormulario(Envio envio) {
    this.envioEdicion = envio;
    this.esEdicion = true;
    
    // Rellenar campos con datos existentes 
    tfNombreDest.setText(envio.getDestinatarioNombre());
    tfCalle.setText(envio.getDestinoCalle());
    tfCiudad.setText(envio.getDestinoCiudad());
    tfCP.setText(envio.getDestinoCodigoPostal());
    tfEstado.setText(envio.getDestinoEstado());
    }

    
    @FXML
private void clicGuardar(ActionEvent event) {
    Cliente cliente = cbCliente.getSelectionModel().getSelectedItem();
    Sucursal sucursal = cbSucursal.getSelectionModel().getSelectedItem();

    if (cliente != null && sucursal != null && !tfNombreDest.getText().isEmpty()) {
        Envio nuevoEnvio = new Envio();
        nuevoEnvio.setIdClienteRemitente(cliente.getIdCliente()); 
        nuevoEnvio.setCodigoSucursalOrigen(sucursal.getCodigoSucursal()); 
        nuevoEnvio.setDestinatarioNombre(tfNombreDest.getText());
        nuevoEnvio.setDestinoCalle(tfCalle.getText());
        nuevoEnvio.setDestinoCiudad(tfCiudad.getText());
        nuevoEnvio.setDestinoCodigoPostal(tfCP.getText());
        nuevoEnvio.setDestinoEstado(tfEstado.getText());

        nuevoEnvio.setCostoTotal(0.0); 
        nuevoEnvio.setIdEstatusEnvio(1); 

        Respuesta resp = EnvioImp.registrar(nuevoEnvio);

        if (!resp.isError()) {
            Utilidades.mostrarAlertaSimple("Éxito", resp.getMensaje(), Alert.AlertType.INFORMATION);
            cerrarVentana();
        } else {
            Utilidades.mostrarAlertaSimple("Error", resp.getMensaje(), Alert.AlertType.ERROR);
        }
    } else {
        Utilidades.mostrarAlertaSimple("Campos requeridos", "Por favor selecciona cliente y sucursal.", Alert.AlertType.WARNING);
    }
}
    
    
    private boolean validarCampos() {
    return cbCliente.getSelectionModel().getSelectedItem() != null &&
           cbSucursal.getSelectionModel().getSelectedItem() != null &&
           !tfNombreDest.getText().trim().isEmpty();
    }
    
  

    private void cerrarVentana() {
        ((Stage) tfNombreDest.getScene().getWindow()).close();
    }
}