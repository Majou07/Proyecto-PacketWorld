package clienteescritorio;

import clienteescritorio.dominio.DireccionImp; 
import clienteescritorio.pojo.Direccion;     
import clienteescritorio.dominio.SucursalImp;
import clienteescritorio.dto.Respuesta;
import clienteescritorio.pojo.Sucursal;
import clienteescritorio.utilidad.Utilidades;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class FXMLFormularioSucursalController implements Initializable {

    @FXML private Label lbTitulo;
    @FXML private TextField tfCodigo, tfNombre, tfCalle, tfNumero, tfCodigoPostal, tfCiudad, tfEstado;
    @FXML private ComboBox<String> cbEstatus, cbColonia;

    @FXML private Label lbErrorCodigo, lbErrorNombre, lbErrorEstatus, lbErrorCP, lbErrorCalle, lbErrorNumero, lbErrorColonia;

    private Sucursal sucursalEdicion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbEstatus.getItems().addAll("Activa", "Inactiva");
        configurarValidacionesDinamicas();
        configurarAutoRellenoCP();
        
         configurarLimpiezaErrores();
    }

    private void configurarValidacionesDinamicas() {
        // Solo números en CP y Número de calle
        tfCodigoPostal.textProperty().addListener((obs, oldV, newV) -> {
            if (!newV.matches("\\d*")) tfCodigoPostal.setText(newV.replaceAll("[^\\d]", ""));
            if (newV.length() > 5) tfCodigoPostal.setText(oldV);
            lbErrorCP.setVisible(false);
        });

        tfNumero.textProperty().addListener((obs, oldV, newV) -> lbErrorNumero.setVisible(false));
        tfNombre.textProperty().addListener((obs, oldV, newV) -> lbErrorNombre.setVisible(false));
    }
    

    private void configurarAutoRellenoCP() {
    tfCodigoPostal.textProperty().addListener((obs, oldValue, newValue) -> {
        if (newValue != null && newValue.length() == 5) {
            cargarDatosPorCodigoPostal(newValue);
        } else if (newValue == null || newValue.isEmpty()) {
            limpiarCamposDireccion();
        }
    });

    cbColonia.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
        if (newVal != null) {
            List<Direccion> resultados = DireccionImp.obtenerInformacionPorCP(tfCodigoPostal.getText());
            for (Direccion dir : resultados) {
                if (dir.getColonia().equals(newVal)) {
                    tfCiudad.setText(dir.getCiudad());
                    tfEstado.setText(dir.getEstado());
                    lbErrorColonia.setVisible(false);
                    break;
                }
            }
        }
    });
}

    private void cargarDatosPorCodigoPostal(String codigoPostal) {
    List<Direccion> resultados = DireccionImp.obtenerInformacionPorCP(codigoPostal);
    
    if (resultados != null && !resultados.isEmpty()) {
        cbColonia.getItems().clear();
        for (Direccion dir : resultados) {
            cbColonia.getItems().add(dir.getColonia());
        }
        cbColonia.getSelectionModel().selectFirst();
        
        // Llenamos campos automáticos con el primer resultado
        tfCiudad.setText(resultados.get(0).getCiudad());
        tfEstado.setText(resultados.get(0).getEstado());
        
        tfCodigoPostal.setStyle("-fx-border-color: green;");
        lbErrorCP.setVisible(false);
    } else {
        limpiarCamposDireccion();
        tfCodigoPostal.setStyle("-fx-border-color: red;");
        lbErrorCP.setText("CP no encontrado");
        lbErrorCP.setVisible(true);
    }
}

    @FXML
    private void clicGuardar(ActionEvent event) {
        if (validarCampos()) {
            Sucursal sucursal = (sucursalEdicion == null) ? new Sucursal() : sucursalEdicion;
            sucursal.setCodigoSucursal(tfCodigo.getText());
            sucursal.setNombreCorto(tfNombre.getText());
            sucursal.setCalle(tfCalle.getText());
            sucursal.setNumero(tfNumero.getText());
            sucursal.setCodigoPostal(tfCodigoPostal.getText());
            sucursal.setColonia(cbColonia.getSelectionModel().getSelectedItem());
            sucursal.setCiudad(tfCiudad.getText());
            sucursal.setEstado(tfEstado.getText());
            sucursal.setEstatus(cbEstatus.getSelectionModel().getSelectedItem());

            Respuesta resp = (sucursalEdicion == null) ? SucursalImp.registrarSucursal(sucursal) : SucursalImp.editarSucursal(sucursal);

            if (!resp.isError()) {
                Utilidades.mostrarAlertaSimple("Éxito", resp.getMensaje(), Alert.AlertType.INFORMATION);
                cerrarVentana();
            } else {
                Utilidades.mostrarAlertaSimple("Error", resp.getMensaje(), Alert.AlertType.ERROR);
            }
        }
    }

    private boolean validarCampos() {
        boolean valido = true;
        valido &= revisarVacio(tfCodigo, lbErrorCodigo);
        valido &= revisarVacio(tfNombre, lbErrorNombre);
        valido &= revisarVacio(tfCalle, lbErrorCalle);
        valido &= revisarVacio(tfNumero, lbErrorNumero);
        valido &= revisarVacio(tfCodigoPostal, lbErrorCP);

        if (cbEstatus.getValue() == null) {
        lbErrorEstatus.setText("Selecciona un estatus");
        lbErrorEstatus.setVisible(true);
        lbErrorEstatus.setManaged(true);
        valido = false;
    }else {
    lbErrorEstatus.setVisible(false);
    lbErrorEstatus.setManaged(false);
}
        if (cbColonia.getValue() == null) {
        lbErrorColonia.setText("Selecciona una colonia");
        lbErrorColonia.setVisible(true);
        lbErrorColonia.setManaged(true);
    valido = false;
} else {
    lbErrorColonia.setVisible(false);
    lbErrorColonia.setManaged(false);
}

        return valido;
    }
    
    private void limpiarCamposDireccion() {
        if (cbColonia != null) {
            cbColonia.getItems().clear();
        }
        if (tfCiudad != null) tfCiudad.clear();
        if (tfEstado != null) tfEstado.clear();
        if (tfCodigoPostal != null) tfCodigoPostal.setStyle("");
        if (lbErrorCP != null) lbErrorCP.setVisible(false);
    }

    private boolean revisarVacio(TextField tf, Label lb) {

    if (tf.getText() == null || tf.getText().trim().isEmpty()) {

        tf.setStyle("-fx-border-color: red;");

        lb.setText("Campo obligatorio");
        lb.setVisible(true);
        lb.setManaged(true);

        return false;

    } else {

        tf.setStyle("");

        lb.setVisible(false);
        lb.setManaged(false);

        return true;
    }
}
    
    private void configurarLimpieza(TextField tf, Label lb) {

    tf.textProperty().addListener((obs, oldV, newV) -> {

        tf.setStyle("");

        lb.setVisible(false);
        lb.setManaged(false);
    });
}
    
   private void configurarLimpiezaErrores() {

    configurarLimpieza(tfCodigo, lbErrorCodigo);
    configurarLimpieza(tfNombre, lbErrorNombre);
    configurarLimpieza(tfCalle, lbErrorCalle);
    configurarLimpieza(tfNumero, lbErrorNumero);
    configurarLimpieza(tfCodigoPostal, lbErrorCP);

    cbEstatus.valueProperty().addListener((obs, oldV, newV) -> {
        lbErrorEstatus.setVisible(false);
        lbErrorEstatus.setManaged(false);
    });

    cbColonia.valueProperty().addListener((obs, oldV, newV) -> {
        lbErrorColonia.setVisible(false);
        lbErrorColonia.setManaged(false);
    });
}
    

    public void inicializarValores(Sucursal sucursal) {

    this.sucursalEdicion = sucursal;

    if (sucursal != null) {

        lbTitulo.setText("Actualizar Sucursal");

        // =========================
        // CAMPOS PRINCIPALES
        // =========================
        tfCodigo.setText(sucursal.getCodigoSucursal());
        tfCodigo.setDisable(true);

        tfNombre.setText(sucursal.getNombreCorto());
        tfCalle.setText(sucursal.getCalle());
        tfNumero.setText(sucursal.getNumero());
        tfCodigoPostal.setText(sucursal.getCodigoPostal());

        tfCiudad.setText(sucursal.getCiudad());
        tfEstado.setText(sucursal.getEstado());

        // =========================
        // ESTATUS (NO EDITABLE)
        // =========================
        cbEstatus.getItems().clear();
        cbEstatus.getItems().addAll("Activa", "Inactiva");
        cbEstatus.getSelectionModel().select(sucursal.getEstatus());
        cbEstatus.setDisable(true);

        // =========================
        // COLONIA
        // =========================
        cbColonia.getItems().clear();
        cbColonia.getItems().add(sucursal.getColonia());
        cbColonia.getSelectionModel().select(sucursal.getColonia());

        // =========================
        // LIMPIAR ESTILOS DE ERROR
        // =========================
        limpiarErroresSucursal();

        // =========================
        // ACTIVAR MISMAS VALIDACIONES DINÁMICAS
        // =========================
        configurarValidacionesDinamicas();
    }
}
    private void limpiarErroresSucursal() {

    tfCodigo.setStyle("");
    tfNombre.setStyle("");
    tfCalle.setStyle("");
    tfNumero.setStyle("");
    tfCodigoPostal.setStyle("");

    lbErrorCodigo.setVisible(false);
    lbErrorNombre.setVisible(false);
    lbErrorCalle.setVisible(false);
    lbErrorNumero.setVisible(false);
    lbErrorCP.setVisible(false);
    lbErrorColonia.setVisible(false);
    lbErrorEstatus.setVisible(false);

    lbErrorCodigo.setManaged(false);
    lbErrorNombre.setManaged(false);
    lbErrorCalle.setManaged(false);
    lbErrorNumero.setManaged(false);
    lbErrorCP.setManaged(false);
    lbErrorColonia.setManaged(false);
    lbErrorEstatus.setManaged(false);
}
    

    @FXML private void clicCancelar(ActionEvent event) { cerrarVentana(); }
    private void cerrarVentana() { ((Stage) tfNombre.getScene().getWindow()).close(); }
}