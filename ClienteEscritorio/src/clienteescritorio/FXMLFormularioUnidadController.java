package clienteescritorio;

import clienteescritorio.dominio.UnidadImp;
import clienteescritorio.dto.Respuesta;
import clienteescritorio.pojo.TipoUnidad;
import clienteescritorio.pojo.Unidad;
import clienteescritorio.utilidad.Utilidades;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class FXMLFormularioUnidadController implements Initializable {

    @FXML private Label lbTitulo;

    @FXML private TextField tfMarca;
    @FXML private TextField tfModelo;
    @FXML private TextField tfAnio;
    @FXML private TextField tfVin;
    @FXML private TextField tfNii;

    @FXML private ComboBox<TipoUnidad> cbTipo;
    @FXML private ComboBox<String> cbEstado;

    @FXML private Label lbErrorMarca;
    @FXML private Label lbErrorModelo;
    @FXML private Label lbErrorAnio;
    @FXML private Label lbErrorVin;
    @FXML private Label lbErrorTipo;

    private Unidad unidadEdicion;
    private ObservableList<TipoUnidad> tipos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        cargarTipos();
        configurarValidacionesDinamicas();
    }

    // =========================
    // INICIALIZAR EDICIÓN
    // =========================
    public void inicializarValores(Unidad unidad) {

        this.unidadEdicion = unidad;

        if (unidad != null) {

            lbTitulo.setText("Actualizar Registro");

            tfMarca.setText(unidad.getMarca());
            tfModelo.setText(unidad.getModelo());
            tfAnio.setText(String.valueOf(unidad.getAnio()));
            tfVin.setText(unidad.getVin());
            tfNii.setText(unidad.getNii());

            tfVin.setDisable(true);
            tfNii.setDisable(true);

            cbEstado.setVisible(true);
            cbEstado.getItems().setAll("Activo", "En mantenimiento");

            if (unidad.getIdEstatusUnidad() == 1) {
                cbEstado.getSelectionModel().select("Activo");
            } else {
                cbEstado.getSelectionModel().select("En mantenimiento");
            }

        } else {
            cbEstado.setVisible(false);
        }
    }

    // =========================
    // VALIDACIONES DINÁMICAS
    // =========================
    private void configurarValidacionesDinamicas() {

        // MARCA SOLO LETRAS
        tfMarca.textProperty().addListener((obs, oldV, newV) -> {

            String texto = newV.replaceAll("[^a-zA-ZáéíóúÁÉÍÓÚñÑ ]", "");

            if (texto.length() > 50) {
                texto = texto.substring(0, 50);
            }

            if (!texto.equals(tfMarca.getText())) {
                tfMarca.setText(texto);
            }

            limpiarError(lbErrorMarca);
        });

        // MODELO
        configurarLimpieza(tfModelo, lbErrorModelo);

        // AÑO SOLO NÚMEROS
        tfAnio.textProperty().addListener((obs, oldV, newV) -> {

            if (!newV.matches("\\d*")) {
                tfAnio.setText(newV.replaceAll("[^\\d]", ""));
            }

            if (newV.length() > 4) {
                tfAnio.setText(oldV);
            }

            limpiarError(lbErrorAnio);
        });

        // VIN
        tfVin.textProperty().addListener((obs, oldV, newV) -> {

            tfVin.setText(newV.toUpperCase());

            if (newV.length() > 17) {
                tfVin.setText(oldV);
            }

            limpiarError(lbErrorVin);
        });

        // TIPO
        cbTipo.valueProperty().addListener((obs, oldV, newV) -> {
            limpiarError(lbErrorTipo);
        });
    }

    // =========================
    // GUARDAR
    // =========================
    @FXML
    private void clicGuardar(ActionEvent event) {

    if (!validarCampos()) return;

    Unidad u = new Unidad();

    u.setMarca(tfMarca.getText().trim());
    u.setModelo(tfModelo.getText().trim());
    u.setAnio(Integer.parseInt(tfAnio.getText().trim()));
    u.setVin(tfVin.getText().trim());
    u.setNii(tfNii.getText().trim());

    // =========================
    // VALIDACIÓN SEGURA COMBOBOX
    // =========================
    if (cbTipo.getValue() == null) {

        Utilidades.mostrarAlertaSimple(
                "Error",
                "Debes seleccionar un tipo de unidad",
                Alert.AlertType.ERROR
        );
        return;
    }

    u.setIdTipoUnidad(cbTipo.getValue().getIdTipoUnidad());

    // =========================
    // ESTATUS
    // =========================
    if (unidadEdicion == null) {

        u.setIdEstatusUnidad(1);

    } else {

        String estado = cbEstado.getValue();

        if (estado == null) {

            u.setIdEstatusUnidad(unidadEdicion.getIdEstatusUnidad());

        } else if (estado.equals("Activo")) {
            u.setIdEstatusUnidad(1);
        } else {
            u.setIdEstatusUnidad(3);
        }

        u.setIdUnidad(unidadEdicion.getIdUnidad());
    }

    // =========================
    // GUARDAR
    // =========================
    Respuesta respuesta;

if (unidadEdicion == null) {
    respuesta = UnidadImp.registrar(u);
} else {
    respuesta = UnidadImp.editar(u);
}

if (respuesta != null && !respuesta.isError()) {

    Utilidades.mostrarAlertaSimple(
            "Éxito",
            (unidadEdicion == null)
                    ? "Registro exitoso"
                    : "Actualización exitosa",
            Alert.AlertType.INFORMATION
    );

    cerrarVentana();

} else {

    Utilidades.mostrarAlertaSimple(
            "Error",
            (respuesta != null) ? respuesta.getMensaje() : "Error desconocido",
            Alert.AlertType.ERROR
    );
}
}
    // =========================
    // CARGAR TIPOS (ASYNC)
    // =========================
    private void cargarTipos() {

        Task<List<TipoUnidad>> task = new Task<List<TipoUnidad>>() {
            @Override
            protected List<TipoUnidad> call() {

                HashMap<String, Object> respuesta = UnidadImp.obtenerTipos();

                if ((boolean) respuesta.get("error")) {
                    return null;
                }

                return (List<TipoUnidad>) respuesta.get("tipos");
            }
        };

        task.setOnSucceeded(e -> {

            List<TipoUnidad> lista = task.getValue();

            if (lista != null) {
                tipos = FXCollections.observableArrayList(lista);
                cbTipo.setItems(tipos);
            }
        });

        task.setOnFailed(e -> {
            Utilidades.mostrarAlertaSimple(
                    "Error",
                    "Error al cargar tipos",
                    Alert.AlertType.ERROR
            );
        });

        new Thread(task).start();
    }

    // =========================
    // VALIDACIÓN GENERAL
    // =========================
    private boolean validarCampos() {

        boolean valido = true;

        valido &= revisarVacio(tfMarca, lbErrorMarca);
        valido &= revisarVacio(tfModelo, lbErrorModelo);

        try {

            int anio = Integer.parseInt(tfAnio.getText());
            int actual = java.time.Year.now().getValue();

            if (anio < 1900 || anio > actual + 1) {
                lbErrorAnio.setText("Año inválido");
                lbErrorAnio.setVisible(true);
                valido = false;
            }

        } catch (Exception e) {
            lbErrorAnio.setVisible(true);
            valido = false;
        }

        if (tfVin.getText().length() != 17) {
            lbErrorVin.setVisible(true);
            valido = false;
        }

        if (cbTipo.getValue() == null) {
            lbErrorTipo.setVisible(true);
            valido = false;
        }

        return valido;
    }

    // =========================
    // UTILIDADES UI
    // =========================
    private void configurarLimpieza(TextField tf, Label lb) {

        tf.textProperty().addListener((obs, oldV, newV) -> {
            limpiarError(lb);
        });
    }

    private void limpiarError(Label lb) {
        lb.setVisible(false);
        lb.setManaged(false);
    }

    private boolean revisarVacio(TextField tf, Label lb) {

        if (tf.getText() == null || tf.getText().trim().isEmpty()) {

            lb.setText("Campo obligatorio");
            lb.setVisible(true);
            lb.setManaged(true);

            return false;
        }

        limpiarError(lb);
        return true;
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        cerrarVentana();
    }

    private void cerrarVentana() {
        ((Stage) tfMarca.getScene().getWindow()).close();
    }
}