package clienteescritorio;

import clienteescritorio.dominio.ClienteImp;
import clienteescritorio.dominio.DireccionImp;
import clienteescritorio.dto.Respuesta;
import clienteescritorio.pojo.Cliente;
import clienteescritorio.pojo.Direccion;
import clienteescritorio.utilidad.Utilidades;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FXMLFormularioClienteController implements Initializable {
    @FXML private Label lbTitulo;
    @FXML private TextField tfNombre, tfPaterno, tfMaterno, tfCalle, tfNumero, tfColonia, tfTelefono, tfCP, tfCorreo;
    private Cliente clienteEdicion;
    @FXML private Label lbErrorNombre, lbErrorPaterno, lbErrorCalle, lbErrorColonia, 
            lbErrorTelefono, lbErrorCP, lbErrorCorreo;

    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        configurarValidacionesDinamicas();
        configurarAutoRellenoCP();
    }

    public void inicializarValores(Cliente cliente) {
        this.clienteEdicion = cliente;
        if (cliente != null) {
            lbTitulo.setText("Actualizar Cliente");
            tfNombre.setText(cliente.getNombre());
            tfPaterno.setText(cliente.getApellidoPaterno());
            tfMaterno.setText(cliente.getApellidoMaterno());
            tfCalle.setText(cliente.getCalle());
            tfNumero.setText(cliente.getNumero());
            tfColonia.setText(cliente.getColonia());
            tfTelefono.setText(cliente.getTelefono());
            tfCP.setText(cliente.getCodigoPostal());
            tfCorreo.setText(cliente.getCorreoElectronico());
        }
    }
    
    private void configurarValidacionesDinamicas() {
        tfTelefono.textProperty().addListener((obs, oldV, newV) -> {
            if (!newV.matches("\\d*")) tfTelefono.setText(newV.replaceAll("[^\\d]", ""));
            lbErrorTelefono.setVisible(newV.length() != 10); 
        });
        
        tfCorreo.textProperty().addListener((obs, oldV, newV) -> {
            boolean esValido = newV.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
            lbErrorCorreo.setVisible(!esValido);
        });
    }
    

    @FXML
    private void clicGuardar(ActionEvent event) {
        if (validarCamposRequeridos()) {
        Cliente nuevo = (clienteEdicion == null) ? new Cliente() : clienteEdicion;
        nuevo.setNombre(tfNombre.getText());
        nuevo.setApellidoPaterno(tfPaterno.getText());
        nuevo.setApellidoMaterno(tfMaterno.getText());
        nuevo.setCalle(tfCalle.getText());
        nuevo.setNumero(tfNumero.getText());
        nuevo.setColonia(tfColonia.getText());
        nuevo.setTelefono(tfTelefono.getText());
        nuevo.setCodigoPostal(tfCP.getText());
        nuevo.setCorreoElectronico(tfCorreo.getText());

        Respuesta resp = (clienteEdicion == null) ? ClienteImp.registrar(nuevo) : ClienteImp.editar(nuevo);

            if (!resp.isError()) {
                Utilidades.mostrarAlertaSimple("Éxito", resp.getMensaje(), Alert.AlertType.INFORMATION);
                cerrarVentana();
            } else {
                Utilidades.mostrarAlertaSimple("Error", resp.getMensaje(), Alert.AlertType.ERROR);
            }
            
        }
    }
    
    private void configurarRegex(TextField tf, String regex, Label lb) {
        tf.textProperty().addListener((obs, oldV, newV) -> {
            if (!newV.matches(regex)) {
                lb.setVisible(true);
            } else {
                lb.setVisible(false);
            }
        });
    }
    
    
    
    private void configurarAutoRellenoCP() {
        tfCP.textProperty().addListener((obs, oldV, newV) -> {
            if (newV.length() == 5) {
                List<Direccion> direcciones = DireccionImp.obtenerInformacionPorCP(newV);
                if (direcciones != null && !direcciones.isEmpty()) {
                    Direccion d = direcciones.get(0);
                    tfColonia.setText(d.getColonia());
                    tfCP.setStyle("-fx-border-color: green;");
                } else {
                    tfCP.setStyle("-fx-border-color: red;");
                }
            }
        });
    }
    
    
    private boolean validarCamposRequeridos() {
        boolean valido = true;
        valido &= revisarVacio(tfNombre, lbErrorNombre);
        valido &= revisarVacio(tfPaterno, lbErrorPaterno);
        valido &= revisarVacio(tfCalle, lbErrorCalle);
        valido &= revisarVacio(tfColonia, lbErrorColonia);
        valido &= revisarVacio(tfTelefono, lbErrorTelefono);
        valido &= revisarVacio(tfCP, lbErrorCP);
        
        if (!tfCorreo.getText().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            lbErrorCorreo.setVisible(true);
            valido = false;
        }
        return valido;
    }
    
    private boolean revisarVacio(TextField tf, Label lb) {
        if (tf.getText().trim().isEmpty()) {
            tf.setStyle("-fx-border-color: red;");
            lb.setVisible(true);
            return false;
        } else {
            tf.setStyle("");
            lb.setVisible(false);
            return true;
        }
    }
    

    @FXML private void clicCancelar(ActionEvent event) { 
        cerrarVentana(); 
    }
    
    private void cerrarVentana() { 
        ((Stage) tfNombre.getScene().getWindow()).close(); 
    }
    
    
}