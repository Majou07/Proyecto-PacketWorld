package clienteescritorio;

import clienteescritorio.dominio.ClienteImp;
import clienteescritorio.dto.Respuesta;
import clienteescritorio.pojo.Cliente;
import clienteescritorio.utilidad.Utilidades;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class FXMLFormularioClienteController implements Initializable {
    @FXML private Label lbTitulo;
    @FXML private TextField tfNombre, tfPaterno, tfMaterno, tfCalle, tfNumero, tfColonia, tfTelefono, tfCP, tfCorreo;
    private Cliente clienteEdicion;

    @Override
    public void initialize(URL url, ResourceBundle rb) { }

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

    @FXML
    private void clicGuardar(ActionEvent event) {
        Cliente nuevo = new Cliente();
        nuevo.setNombre(tfNombre.getText());
        nuevo.setApellidoPaterno(tfPaterno.getText());
        nuevo.setApellidoMaterno(tfMaterno.getText());
        nuevo.setCalle(tfCalle.getText());
        nuevo.setNumero(tfNumero.getText());
        nuevo.setColonia(tfColonia.getText());
        nuevo.setTelefono(tfTelefono.getText());
        nuevo.setCodigoPostal(tfCP.getText());
        nuevo.setCorreoElectronico(tfCorreo.getText());

        Respuesta resp;
        if (clienteEdicion == null) {
            resp = ClienteImp.registrar(nuevo);
        } else {
            nuevo.setIdCliente(clienteEdicion.getIdCliente());
            resp = ClienteImp.editar(nuevo);
        }

        if (!resp.isError()) {
            Utilidades.mostrarAlertaSimple("Éxito", resp.getMensaje(), Alert.AlertType.INFORMATION);
            cerrarVentana();
        } else {
            Utilidades.mostrarAlertaSimple("Error", resp.getMensaje(), Alert.AlertType.ERROR);
        }
    }

    @FXML private void clicCancelar(ActionEvent event) { cerrarVentana(); }
    private void cerrarVentana() { ((Stage) tfNombre.getScene().getWindow()).close(); }
}