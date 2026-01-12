package clienteescritorio;

import clienteescritorio.dominio.ClienteImp;
import clienteescritorio.dominio.DireccionImp;
import clienteescritorio.dominio.EnvioImp;
import clienteescritorio.dominio.SucursalImp;
import clienteescritorio.dto.Respuesta;
import clienteescritorio.pojo.Cliente;
import clienteescritorio.pojo.Direccion;
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
    @FXML private TextField tfApPaternoDest;
    @FXML private TextField tfApMaternoDest;
    @FXML private TextField tfNumero;
    @FXML private TextField tfCalle;
    @FXML private TextField tfColonia;
    @FXML private TextField tfCiudad;
    @FXML private TextField tfCP;
    @FXML private TextField tfEstado;
    private Envio envioEdicion;
    private boolean esEdicion = false;
    
    @FXML private Label lbErrorNombre, lbErrorApPaterno, lbErrorApMaterno, lbErrorNumero, lbErrorCP, 
            lbErrorCliente, lbErrorSucursal, lbErrorCalle, lbErrorColonia;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarCatalogos();
        configurarValidacionesDinamicas();
        configurarAutoRellenoCP();
        
        cbCliente.setConverter(new javafx.util.StringConverter<Cliente>() {
            @Override public String toString(Cliente o) { return (o != null) ? o.getNombre() + " " + o.getApellidoPaterno() : ""; }
            @Override public Cliente fromString(String s) { return null; }
        });
    }    
    
    
    private void configurarValidacionesDinamicas() {
        tfNumero.textProperty().addListener((obs, oldVal, newVal) -> {
            //Sobre el num ext e int
            if (!newVal.matches("\\d*")) {
                tfNumero.setText(newVal.replaceAll("[^\\d]", ""));
                lbErrorNumero.setVisible(true);
            } else { lbErrorNumero.setVisible(false); }
        });

        //Aqui son solo los caracteres
        configurarRegexLetras(tfNombreDest, lbErrorNombre);
        configurarRegexLetras(tfApPaternoDest, lbErrorApPaterno);
        configurarRegexLetras(tfApMaternoDest, lbErrorApMaterno);
    }
    
    private void configurarRegexLetras(TextField tf, Label lb) {
        tf.textProperty().addListener((obs, oldV, newV) -> {
            if (!newV.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*")) {
                lb.setVisible(true);
            } else { lb.setVisible(false); }
        });
    }
    
    private void configurarAutoRellenoCP() {
    tfCP.textProperty().addListener((obs, oldVal, newVal) -> {
        tfCP.setStyle(""); 
        lbErrorCP.setVisible(false);

        if (newVal.length() == 5) {
            try {
                List<Direccion> direcciones = DireccionImp.obtenerInformacionPorCP(newVal);
                
                if (direcciones != null && !direcciones.isEmpty()) {
                    Direccion primera = direcciones.get(0);
                    
                    tfCiudad.setText(primera.getCiudad());
                    tfEstado.setText(primera.getEstado());
                    tfColonia.setText(primera.getColonia()); 
                    
                    tfCP.setStyle("-fx-border-color: green;");
                } else {
                    mostrarErrorCP("El CP " + newVal + " no existe en el catálogo");
                }
            } catch (Exception e) {
                mostrarErrorCP("Error al conectar con el servidor");
            }
        }
        });
    }

    private void mostrarErrorCP(String mensaje) {
        lbErrorCP.setText(mensaje);
        lbErrorCP.setVisible(true);
        tfCP.setStyle("-fx-border-color: red;");
        tfCiudad.setText("");
        tfEstado.setText("");
        tfColonia.setText("");
    }
    
     
    
    @FXML
    private void clicGuardar(ActionEvent event) {
        if (validarCamposRequeridos()) {
            Envio envio = (esEdicion) ? envioEdicion : new Envio();
            envio.setIdClienteRemitente(cbCliente.getValue().getIdCliente());
            envio.setCodigoSucursalOrigen(cbSucursal.getValue().getCodigoSucursal());
            envio.setDestinatarioNombre(tfNombreDest.getText());
            envio.setDestinatarioApPaterno(tfApPaternoDest.getText());
            envio.setDestinatarioApMaterno(tfApMaternoDest.getText());
            envio.setDestinoCalle(tfCalle.getText());
            envio.setDestinoNumero(tfNumero.getText());
            envio.setDestinoColonia(tfColonia.getText());
            envio.setDestinoCodigoPostal(tfCP.getText());
            envio.setDestinoCiudad(tfCiudad.getText());
            envio.setDestinoEstado(tfEstado.getText());
            
            envio.setCostoTotal(0.0);
            envio.setIdEstatusEnvio(1); 

            Respuesta resp = (esEdicion) ? EnvioImp.editar(envio) : EnvioImp.registrar(envio);
            if (!resp.isError()) {
                Utilidades.mostrarAlertaSimple("Éxito", resp.getMensaje(), Alert.AlertType.INFORMATION);
                cerrarVentana();
            } else {
                Utilidades.mostrarAlertaSimple("Error", resp.getMensaje(), Alert.AlertType.ERROR);
            }
        }
    }
    
    private boolean validarCamposRequeridos() {
        boolean valido = true;
        valido &= revisarVacio(tfNombreDest, lbErrorNombre);
        valido &= revisarVacio(tfApPaternoDest, lbErrorApPaterno);
        valido &= revisarVacio(tfNumero, lbErrorNumero);
        valido &= revisarVacio(tfCalle, lbErrorCalle);
        valido &= revisarVacio(tfColonia, lbErrorColonia);
        valido &= revisarVacio(tfCP, lbErrorCP);
        
        if (cbCliente.getValue() == null) { lbErrorCliente.setVisible(true); valido = false; }
        if (cbSucursal.getValue() == null) { lbErrorSucursal.setVisible(true); valido = false; }
        
        return valido;
    }
    
    private boolean revisarVacio(TextField tf, Label lb) {
        if (tf.getText().trim().isEmpty()) {
            tf.setStyle("-fx-border-color: red;");
            lb.setText("Este campo es obligatorio");
            lb.setVisible(true);
            return false;
        } else {
            tf.setStyle("");
            lb.setVisible(false);
            return true;
        }
    }
    
    
    private boolean validarCampos() {
    return cbCliente.getSelectionModel().getSelectedItem() != null &&
           cbSucursal.getSelectionModel().getSelectedItem() != null &&
           !tfNombreDest.getText().trim().isEmpty();
    }
    
    
    private void cargarCatalogos(){
        HashMap<String, Object> respClientes = ClienteImp.obtenerClientes();
        if (!(boolean)respClientes.get("error"))
            cbCliente.setItems(FXCollections.observableArrayList((List<Cliente>)respClientes.get("clientes")));
        
        HashMap<String, Object> respSuc = SucursalImp.obtenerSucursales();
        if (!(boolean)respSuc.get("error"))
            cbSucursal.setItems(FXCollections.observableArrayList((List<Sucursal>)respSuc.get("sucursales")));
    }
    
    
    public void prepararFormulario(Envio envio) {
        this.envioEdicion = envio;
        this.esEdicion = true;
        tfNombreDest.setText(envio.getDestinatarioNombre());
        tfApPaternoDest.setText(envio.getDestinatarioApPaterno());
        tfApMaternoDest.setText(envio.getDestinatarioApMaterno());
        tfCalle.setText(envio.getDestinoCalle());
        tfNumero.setText(envio.getDestinoNumero());
        tfColonia.setText(envio.getDestinoColonia());
        tfCP.setText(envio.getDestinoCodigoPostal());
        tfCiudad.setText(envio.getDestinoCiudad());
        tfEstado.setText(envio.getDestinoEstado());
    }
  

    private void cerrarVentana() {
        ((Stage) tfNombreDest.getScene().getWindow()).close();
    }
}