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

        if (cbEstatus.getValue() == null) { lbErrorEstatus.setVisible(true); valido = false; } else { lbErrorEstatus.setVisible(false); }
        if (cbColonia.getValue() == null) { lbErrorColonia.setVisible(true); valido = false; } else { lbErrorColonia.setVisible(false); }

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

    public void inicializarValores(Sucursal sucursal) {
        this.sucursalEdicion = sucursal;
        if (sucursal != null) {
            lbTitulo.setText("Actualizar Sucursal");
            tfCodigo.setText(sucursal.getCodigoSucursal());
            tfCodigo.setDisable(true);
            tfNombre.setText(sucursal.getNombreCorto());
            tfCalle.setText(sucursal.getCalle());
            tfNumero.setText(sucursal.getNumero());
            tfCodigoPostal.setText(sucursal.getCodigoPostal());
            cbEstatus.getSelectionModel().select(sucursal.getEstatus());
            // Para la colonia en edición, la cargamos manualmente si no se dispara el listener
            cbColonia.getItems().add(sucursal.getColonia());
            cbColonia.getSelectionModel().select(sucursal.getColonia());
            tfCiudad.setText(sucursal.getCiudad());
            tfEstado.setText(sucursal.getEstado());
        }
    }

    @FXML private void clicCancelar(ActionEvent event) { cerrarVentana(); }
    private void cerrarVentana() { ((Stage) tfNombre.getScene().getWindow()).close(); }
}