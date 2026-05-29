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

    @FXML private TextField tfNombre, tfPaterno, tfMaterno, tfCalle, tfNumero, tfTelefono, tfCP, tfCorreo;
    @FXML private ComboBox<String> cbColonia;

    @FXML private Label lbErrorNombre, lbErrorPaterno, lbErrorCalle, lbErrorColonia,
            lbErrorTelefono, lbErrorCP, lbErrorCorreo, lbErrorNumero;

    private Cliente clienteEdicion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        configurarValidacionesDinamicas();
        configurarAutoRellenoCP();
        configurarLimpiezaErrores();
    }

    // =========================
    // VALIDACIONES DINÁMICAS
    // =========================
    private void configurarValidacionesDinamicas() {

        tfTelefono.textProperty().addListener((obs, oldV, newV) -> {
            if (!newV.matches("\\d*")) {
                tfTelefono.setText(newV.replaceAll("[^\\d]", ""));
            }
            ocultar(lbErrorTelefono);
        });

        tfCP.textProperty().addListener((obs, oldV, newV) -> {
            if (!newV.matches("\\d*")) {
                tfCP.setText(newV.replaceAll("[^\\d]", ""));
            }
            if (newV.length() > 5) {
                tfCP.setText(oldV);
            }
            ocultar(lbErrorCP);
        });

        tfNombre.textProperty().addListener((obs, oldV, newV) -> ocultar(lbErrorNombre));
        tfPaterno.textProperty().addListener((obs, oldV, newV) -> ocultar(lbErrorPaterno));
        tfCalle.textProperty().addListener((obs, oldV, newV) -> ocultar(lbErrorCalle));
        tfNumero.textProperty().addListener((obs, oldV, newV) -> ocultar(lbErrorNumero));
    }

    // =========================
    // AUTOCOMPLETADO CP
    // =========================
    private void configurarAutoRellenoCP() {

        tfCP.textProperty().addListener((obs, oldV, newV) -> {

            if (newV != null && newV.length() == 5) {
                cargarDatosPorCP(newV);
            } else if (newV == null || newV.isEmpty()) {
                limpiarCamposDireccion();
            }
        });

        cbColonia.valueProperty().addListener((obs, oldV, newV) -> {
            ocultar(lbErrorColonia);
        });
    }

    private void cargarDatosPorCP(String cp) {

        List<Direccion> resultados = DireccionImp.obtenerInformacionPorCP(cp);

        if (resultados != null && !resultados.isEmpty()) {

            cbColonia.getItems().clear();

            for (Direccion d : resultados) {
                cbColonia.getItems().add(d.getColonia());
            }

            cbColonia.getSelectionModel().selectFirst();

            tfCP.setStyle("-fx-border-color: green;");
            ocultar(lbErrorCP);

        } else {

            limpiarCamposDireccion();

            tfCP.setStyle("-fx-border-color: red;");
            lbErrorCP.setText("CP no encontrado");
            mostrar(lbErrorCP);
        }
    }

    private void limpiarCamposDireccion() {
        cbColonia.getItems().clear();
        tfCP.setStyle("");
    }

    // =========================
    // GUARDAR
    // =========================
    @FXML
    private void clicGuardar(ActionEvent event) {

        if (validarCampos()) {

            Cliente c = (clienteEdicion == null) ? new Cliente() : clienteEdicion;

            c.setNombre(tfNombre.getText());
            c.setApellidoPaterno(tfPaterno.getText());
            c.setApellidoMaterno(tfMaterno.getText());
            c.setCalle(tfCalle.getText());
            c.setNumero(tfNumero.getText());
            c.setColonia(cbColonia.getValue());
            c.setTelefono(tfTelefono.getText());
            c.setCodigoPostal(tfCP.getText());
            c.setCorreoElectronico(tfCorreo.getText());

            Respuesta resp = (clienteEdicion == null)
                    ? ClienteImp.registrar(c)
                    : ClienteImp.editar(c);

            if (!resp.isError()) {
                Utilidades.mostrarAlertaSimple("Éxito", resp.getMensaje(), Alert.AlertType.INFORMATION);
                cerrarVentana();
            } else {
                Utilidades.mostrarAlertaSimple("Error", resp.getMensaje(), Alert.AlertType.ERROR);
            }
        }
    }

    // =========================
    // VALIDACIÓN
    // =========================
    private boolean validarCampos() {

        boolean valido = true;

        valido &= revisar(tfNombre, lbErrorNombre);
        valido &= revisar(tfPaterno, lbErrorPaterno);
        valido &= revisar(tfCalle, lbErrorCalle);
        valido &= revisar(tfNumero, lbErrorNumero);
        valido &= revisar(tfCP, lbErrorCP);
        valido &= revisar(tfTelefono, lbErrorTelefono);

        if (cbColonia.getValue() == null) {
            lbErrorColonia.setText("Selecciona una colonia");
            mostrar(lbErrorColonia);
            valido = false;
        } else {
            ocultar(lbErrorColonia);
        }

        if (tfCorreo.getText() == null ||
            !tfCorreo.getText().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {

            lbErrorCorreo.setText("Correo inválido");
            mostrar(lbErrorCorreo);
            valido = false;
        } else {
            ocultar(lbErrorCorreo);
        }

        return valido;
    }

    private boolean revisar(TextField tf, Label lb) {

        if (tf.getText() == null || tf.getText().trim().isEmpty()) {

            tf.setStyle("-fx-border-color: red;");
            lb.setText("Campo obligatorio");
            mostrar(lb);
            return false;

        } else {
            tf.setStyle("");
            ocultar(lb);
            return true;
        }
    }

    // =========================
    // LIMPIEZA
    // =========================
    private void configurarLimpiezaErrores() {

        configurar(tfNombre, lbErrorNombre);
        configurar(tfPaterno, lbErrorPaterno);
        configurar(tfCalle, lbErrorCalle);
        configurar(tfNumero, lbErrorNumero);
        configurar(tfCP, lbErrorCP);
        configurar(tfTelefono, lbErrorTelefono);

        cbColonia.valueProperty().addListener((obs, oldV, newV) -> ocultar(lbErrorColonia));
    }

    private void configurar(TextField tf, Label lb) {
        tf.textProperty().addListener((obs, oldV, newV) -> {
            tf.setStyle("");
            ocultar(lb);
        });
    }

    // =========================
    // UTILIDAD VISUAL
    // =========================
    private void mostrar(Label lb) {
        lb.setVisible(true);
        lb.setManaged(true);
    }

    private void ocultar(Label lb) {
        lb.setVisible(false);
        lb.setManaged(false);
    }

    // =========================
    // EDICIÓN
    // =========================
    public void inicializarValores(Cliente cliente) {

        this.clienteEdicion = cliente;

        if (cliente != null) {

            lbTitulo.setText("Actualizar Cliente");

            tfNombre.setText(cliente.getNombre());
            tfPaterno.setText(cliente.getApellidoPaterno());
            tfMaterno.setText(cliente.getApellidoMaterno());
            tfCalle.setText(cliente.getCalle());
            tfNumero.setText(cliente.getNumero());
            tfTelefono.setText(cliente.getTelefono());
            tfCP.setText(cliente.getCodigoPostal());
            tfCorreo.setText(cliente.getCorreoElectronico());

            cargarDatosPorCP(cliente.getCodigoPostal());
            cbColonia.getSelectionModel().select(cliente.getColonia());

            limpiarErroresCliente();
        }
    }

    private void limpiarErroresCliente() {

        ocultar(lbErrorNombre);
        ocultar(lbErrorPaterno);
        ocultar(lbErrorCalle);
        ocultar(lbErrorNumero);
        ocultar(lbErrorCP);
        ocultar(lbErrorTelefono);
        ocultar(lbErrorColonia);
        ocultar(lbErrorCorreo);
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        cerrarVentana();
    }

    private void cerrarVentana() {
        ((Stage) tfNombre.getScene().getWindow()).close();
    }
}