package clienteescritorio;

import clienteescritorio.dominio.ColaboradorImp;
import clienteescritorio.dominio.SucursalImp;
import clienteescritorio.dto.Respuesta;
import clienteescritorio.pojo.Colaborador;
import clienteescritorio.pojo.Rol;
import clienteescritorio.pojo.Sucursal;
import clienteescritorio.utilidad.Constantes;
import clienteescritorio.utilidad.Utilidades;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class FXMLFormularioColaboradorController implements Initializable {

    @FXML private Label lbTitulo;

    @FXML private TextField tfNombre;
    @FXML private TextField tfPaterno;
    @FXML private TextField tfMaterno;
    @FXML private TextField tfCurp;
    @FXML private TextField tfCorreo;
    @FXML private TextField tfNoPersonal;
    @FXML private TextField tfNumeroLicencia;

    @FXML private PasswordField pfContrasena;

    @FXML private ComboBox<Rol> cbRol;
    @FXML private ComboBox<Sucursal> cbSucursal;

    @FXML private ImageView ivFoto;

    @FXML private Label lbErrorNombre;
    @FXML private Label lbErrorPaterno;
    @FXML private Label lbErrorCurp;
    @FXML private Label lbErrorCorreo;
    @FXML private Label lbErrorNoPersonal;
    @FXML private Label lbErrorPassword;
    @FXML private Label lbErrorRol;
    @FXML private Label lbErrorSucursal;
    @FXML private Label lbErrorLicencia;

    private Colaborador colaboradorEdicion;
    private File archivoFoto;

    private ObservableList<Rol> roles;
    private ObservableList<Sucursal> sucursales;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        cargarRoles();
        cargarSucursales();

        configurarValidacionesDinamicas();
        
        // Hints / PromptText
        tfNombre.setPromptText("Ej. Juan");
        tfPaterno.setPromptText("Ej. Pérez");
        tfMaterno.setPromptText("Ej. López");

        tfCurp.setPromptText("18 caracteres");
        tfCorreo.setPromptText("ejemplo@correo.com");

        tfNoPersonal.setPromptText("Ej. EMP001");

        pfContrasena.setPromptText("Mínimo 8 caracteres");

        tfNumeroLicencia.setPromptText("Ej. LIC123456");

        tfNumeroLicencia.setDisable(true);
        
        lbErrorNombre.setManaged(false);
        lbErrorPaterno.setManaged(false);
        lbErrorCurp.setManaged(false);
        lbErrorCorreo.setManaged(false);
        lbErrorNoPersonal.setManaged(false);
        lbErrorPassword.setManaged(false);
        lbErrorRol.setManaged(false);
        lbErrorSucursal.setManaged(false);
        lbErrorLicencia.setManaged(false);
        
        

        // Ocultar mensajes al iniciar
        lbErrorNombre.setVisible(false);
        lbErrorPaterno.setVisible(false);
        lbErrorCurp.setVisible(false);
        lbErrorCorreo.setVisible(false);
        lbErrorNoPersonal.setVisible(false);
        lbErrorPassword.setVisible(false);
        lbErrorRol.setVisible(false);
        lbErrorSucursal.setVisible(false);
        lbErrorLicencia.setVisible(false);

        cbRol.setOnAction(e -> {

            boolean esConductor = cbRol.getValue() != null
                    && cbRol.getValue().getNombreRol()
                            .equalsIgnoreCase(Constantes.ROL_CONDUCTOR);

            tfNumeroLicencia.setDisable(!esConductor);

            if (!esConductor) {

                tfNumeroLicencia.clear();
                tfNumeroLicencia.setStyle(Constantes.ESTILO_NORMAL);

                lbErrorLicencia.setVisible(false);
            }

            lbErrorRol.setVisible(false);
        });

        cbSucursal.setOnAction(e -> {
            lbErrorSucursal.setVisible(false);
        });
    }

    public void inicializarValores(Colaborador colaborador) {

        this.colaboradorEdicion = colaborador;

        if (colaborador != null) {
            

            lbTitulo.setText("Actualizar Colaborador");

            tfNombre.setText(colaborador.getNombre());
            tfPaterno.setText(colaborador.getApellidoPaterno());
            tfMaterno.setText(colaborador.getApellidoMaterno());
            tfCurp.setText(colaborador.getCurp());
            tfCorreo.setText(colaborador.getCorreoElectronico());
            tfNoPersonal.setText(colaborador.getNumeroPersonal());
            pfContrasena.setText(colaborador.getContrasena());

            seleccionarRol(colaborador.getIdRol());
            seleccionarSucursal(colaborador.getCodigoSucursal());
            
            // Hints
            tfCurp.setPromptText("18 caracteres");
            tfCorreo.setPromptText("ejemplo@correo.com");
            tfNoPersonal.setPromptText("EMP001");
            pfContrasena.setPromptText("Mínimo 8 caracteres");
            tfNumeroLicencia.setPromptText("LIC123456");
            
            //Bloquear campos que no deben editarse
            tfNoPersonal.setDisable(true);
            cbRol.setDisable(true);

             // Validación conductor
        boolean esConductor = cbRol.getValue() != null
                && cbRol.getValue()
                        .getNombreRol()
                        .equalsIgnoreCase(Constantes.ROL_CONDUCTOR);

        tfNumeroLicencia.setDisable(!esConductor);

        if (esConductor) {

            tfNumeroLicencia.setText(
                    colaborador.getNumeroLicencia() != null
                    ? colaborador.getNumeroLicencia()
                    : ""
            );

        } else {

            tfNumeroLicencia.clear();
        }

        // Quitar errores visuales al abrir
        limpiarErrores();

        // Cargar foto
        cargarFotoServidor(colaborador.getIdColaborador());
    }
}

    private void cargarRoles() {

    Task<List<Rol>> task = new Task<List<Rol>>() {
        @Override
        protected List<Rol> call() {
            HashMap<String, Object> respuesta = ColaboradorImp.obtenerRoles();

            if ((boolean) respuesta.get(Constantes.KEY_ERROR)) {
                return null;
            }

            return (List<Rol>) respuesta.get("roles");
        }
    };

    task.setOnSucceeded(e -> {
        List<Rol> lista = task.getValue();

        if (lista != null) {
            roles = FXCollections.observableArrayList(lista);
            cbRol.setItems(roles);
        }
    });

    new Thread(task).start();
}

    private void cargarSucursales() {

    Task<List<Sucursal>> task = new Task<List<Sucursal>>() {
        @Override
        protected List<Sucursal> call() {

            HashMap<String, Object> respuesta = SucursalImp.obtenerSucursales();

            if ((boolean) respuesta.get(Constantes.KEY_ERROR)) {
                return null;
            }

            return (List<Sucursal>) respuesta.get("sucursales");
        }
    };

    task.setOnSucceeded(e -> {
        List<Sucursal> lista = task.getValue();

        if (lista != null) {
            sucursales = FXCollections.observableArrayList(lista);
            cbSucursal.setItems(sucursales);
        }
    });

    new Thread(task).start();
}

    @FXML
    private void clicSubirFoto(ActionEvent event) {

        FileChooser dialogo = new FileChooser();

        dialogo.setTitle("Selecciona una foto");

        FileChooser.ExtensionFilter filtroImg
                = new FileChooser.ExtensionFilter(
                        "Archivos de imagen (*.jpg, *.png)",
                        "*.jpg",
                        "*.png"
                );

        dialogo.getExtensionFilters().add(filtroImg);

        archivoFoto = dialogo.showOpenDialog(
                tfNombre.getScene().getWindow()
        );

        if (archivoFoto != null) {

            try {

                BufferedImage bufferImg = ImageIO.read(archivoFoto);

                Image imagen = SwingFXUtils.toFXImage(bufferImg, null);

                ivFoto.setImage(imagen);

            } catch (IOException e) {

                Utilidades.mostrarAlertaSimple(
                        "Error",
                        "No se pudo cargar la imagen seleccionada",
                        Alert.AlertType.ERROR
                );
            }
        }
    }

    private void mostrarFotoServidor(String base64) {

        try {

            byte[] fotoBytes = Base64.getDecoder()
                    .decode(base64.replaceAll("\\n", ""));

            ByteArrayInputStream stream
                    = new ByteArrayInputStream(fotoBytes);

            Image imagen = new Image(stream);

            ivFoto.setImage(imagen);

        } catch (Exception e) {

            System.out.println(
                    "Error al decodificar imagen: " + e.getMessage()
            );
        }
    }

    @FXML
    private void clicGuardar(ActionEvent event) {

        if (!validarCampos()) {
            return;
        }

        Colaborador colaborador = new Colaborador();

        colaborador.setNombre(tfNombre.getText().trim());
        colaborador.setApellidoPaterno(tfPaterno.getText().trim());
        colaborador.setApellidoMaterno(tfMaterno.getText().trim());
        colaborador.setCurp(tfCurp.getText().trim());
        colaborador.setCorreoElectronico(tfCorreo.getText().trim());
        colaborador.setNumeroPersonal(tfNoPersonal.getText().trim());
        colaborador.setContrasena(pfContrasena.getText());

        if (cbRol.getValue() != null) {

            colaborador.setIdRol(
                    cbRol.getSelectionModel()
                            .getSelectedItem()
                            .getIdRol()
            );
        }

        if (cbSucursal.getValue() != null) {

            colaborador.setCodigoSucursal(
                    cbSucursal.getSelectionModel()
                            .getSelectedItem()
                            .getCodigoSucursal()
            );
        }

        if (!tfNumeroLicencia.isDisabled()) {

            colaborador.setNumeroLicencia(
                    tfNumeroLicencia.getText().trim()
            );
        }

        if (archivoFoto != null) {

            try {

                byte[] bytesFoto = Files.readAllBytes(
                        archivoFoto.toPath()
                );

                colaborador.setFoto(bytesFoto);

            } catch (IOException ex) {

                Utilidades.mostrarAlertaSimple(
                        "Error",
                        "Error al procesar la foto para envío",
                        Alert.AlertType.ERROR
                );

                return;
            }
        }

        if (colaboradorEdicion == null) {

            registrarColaborador(colaborador);

        } else {

            colaborador.setIdColaborador(
                    colaboradorEdicion.getIdColaborador()
            );

            actualizarColaborador(colaborador);
        }
    }

    private void registrarColaborador(Colaborador colaborador) {

    Task<Respuesta> task = new Task<Respuesta>() {
        @Override
        protected Respuesta call() {
            return ColaboradorImp.registrar(colaborador);
        }
    };

    task.setOnSucceeded(e -> {

        Respuesta respuesta = task.getValue();

        if (!respuesta.isError()) {

            Utilidades.mostrarAlertaSimple(
                    "Éxito",
                    respuesta.getMensaje(),
                    Alert.AlertType.INFORMATION
            );

            cerrarVentana();

        } else {

            Utilidades.mostrarAlertaSimple(
                    "Error",
                    respuesta.getMensaje(),
                    Alert.AlertType.ERROR
            );
        }
    });

    new Thread(task).start();
}

   private void actualizarColaborador(Colaborador colaborador) {

    Task<Respuesta> task = new Task<Respuesta>() {
        @Override
        protected Respuesta call() {
            return ColaboradorImp.editar(colaborador);
        }
    };

    task.setOnSucceeded(e -> {

        Respuesta respuesta = task.getValue();

        if (!respuesta.isError()) {

            Utilidades.mostrarAlertaSimple(
                    "Éxito",
                    respuesta.getMensaje(),
                    Alert.AlertType.INFORMATION
            );

            cerrarVentana();

        } else {

            Utilidades.mostrarAlertaSimple(
                    "Error",
                    respuesta.getMensaje(),
                    Alert.AlertType.ERROR
            );
        }
    });

    new Thread(task).start();
}

    private void cargarFotoServidor(int idColaborador) {

    Task<Colaborador> task = new Task<Colaborador>() {
        @Override
        protected Colaborador call() {
            return ColaboradorImp.obtenerFoto(idColaborador);
        }
    };

    task.setOnSucceeded(e -> {

        Colaborador c = task.getValue();

        if (c != null && c.getFotografia() != null) {
            mostrarFotoServidor(c.getFotografia());
        } else {
            ivFoto.setImage(null);
        }
    });

    new Thread(task).start();
}

    private boolean validarCampos() {

    boolean valido = true;

    valido &= revisarVacio(tfNombre, lbErrorNombre);
    valido &= revisarVacio(tfPaterno, lbErrorPaterno);
    valido &= revisarVacio(tfNoPersonal, lbErrorNoPersonal);
    valido &= revisarVacio(pfContrasena, lbErrorPassword);

    // CURP
    if (!tfCurp.getText().matches(Constantes.REGEX_CURP)) {

        tfCurp.setStyle(Constantes.ESTILO_ERROR);

        lbErrorCurp.setText("CURP inválida");
        lbErrorCurp.setVisible(true);
        lbErrorCurp.setManaged(true);

        valido = false;

    } else {

        tfCurp.setStyle(Constantes.ESTILO_NORMAL);

        lbErrorCurp.setVisible(false);
        lbErrorCurp.setManaged(false);
    }

    // CORREO
    if (!tfCorreo.getText().matches(Constantes.REGEX_CORREO)) {

        tfCorreo.setStyle(Constantes.ESTILO_ERROR);

        lbErrorCorreo.setText("Correo inválido");
        lbErrorCorreo.setVisible(true);
        lbErrorCorreo.setManaged(true);

        valido = false;

    } else {

        tfCorreo.setStyle(Constantes.ESTILO_NORMAL);

        lbErrorCorreo.setVisible(false);
        lbErrorCorreo.setManaged(false);
    }

    // NUMERO PERSONAL
    if (!tfNoPersonal.getText()
            .matches(Constantes.REGEX_NUMERO_PERSONAL)) {

        tfNoPersonal.setStyle(Constantes.ESTILO_ERROR);

        lbErrorNoPersonal.setText("Formato: EMP001");
        lbErrorNoPersonal.setVisible(true);
        lbErrorNoPersonal.setManaged(true);

        valido = false;

    } else {

        tfNoPersonal.setStyle(Constantes.ESTILO_NORMAL);

        lbErrorNoPersonal.setVisible(false);
        lbErrorNoPersonal.setManaged(false);
    }

    // PASSWORD
    if (pfContrasena.getText().length() < 8) {

        pfContrasena.setStyle(Constantes.ESTILO_ERROR);

        lbErrorPassword.setText("Mínimo 8 caracteres");
        lbErrorPassword.setVisible(true);
        lbErrorPassword.setManaged(true);

        valido = false;

    } else {

        pfContrasena.setStyle(Constantes.ESTILO_NORMAL);

        lbErrorPassword.setVisible(false);
        lbErrorPassword.setManaged(false);
    }

    // ROL
    if (cbRol.getValue() == null) {

        lbErrorRol.setText("Selecciona un rol");

        lbErrorRol.setVisible(true);
        lbErrorRol.setManaged(true);

        valido = false;

    } else {

        lbErrorRol.setVisible(false);
        lbErrorRol.setManaged(false);
    }

    // SUCURSAL
    if (cbSucursal.getValue() == null) {

        lbErrorSucursal.setText("Selecciona una sucursal");

        lbErrorSucursal.setVisible(true);
        lbErrorSucursal.setManaged(true);

        valido = false;

    } else {

        lbErrorSucursal.setVisible(false);
        lbErrorSucursal.setManaged(false);
    }

    // LICENCIA
    if (!tfNumeroLicencia.isDisabled()) {

        valido &= revisarVacio(
                tfNumeroLicencia,
                lbErrorLicencia
        );

        if (!tfNumeroLicencia.getText()
                .matches(Constantes.REGEX_NUMERO_LICENCIA)) {

            tfNumeroLicencia.setStyle(Constantes.ESTILO_ERROR);

            lbErrorLicencia.setText("Formato inválido");

            lbErrorLicencia.setVisible(true);
            lbErrorLicencia.setManaged(true);

            valido = false;

        } else {

            tfNumeroLicencia.setStyle(Constantes.ESTILO_NORMAL);

            lbErrorLicencia.setVisible(false);
            lbErrorLicencia.setManaged(false);
        }
    }

    return valido;
}

    private boolean revisarVacio(TextField tf, Label lb) {
        System.out.println("TF: " + tf);
    System.out.println("LB: " + lb);

    if (tf.getText() == null || tf.getText().trim().isEmpty()) {

        tf.setStyle(Constantes.ESTILO_ERROR);

        lb.setText("Campo obligatorio");
        lb.setVisible(true);
        lb.setManaged(true);

        return false;

    } else {

        tf.setStyle(Constantes.ESTILO_NORMAL);

        lb.setVisible(false);
        lb.setManaged(false);

        return true;
    }
}

   private boolean revisarVacio(PasswordField pf, Label lb) {
       
       

    if (pf.getText() == null || pf.getText().trim().isEmpty()) {


        pf.setStyle(Constantes.ESTILO_ERROR);

        lb.setText("Campo obligatorio");
        lb.setVisible(true);
        lb.setManaged(true);

        return false;

    } else {

        pf.setStyle(Constantes.ESTILO_NORMAL);

        lb.setVisible(false);
        lb.setManaged(false);

        return true;
    }
}

    private void configurarValidacionesDinamicas() {

    configurarLimpieza(tfNombre, lbErrorNombre);
    configurarLimpieza(tfPaterno, lbErrorPaterno);
    configurarLimpieza(tfCorreo, lbErrorCorreo);
    configurarLimpieza(tfNoPersonal, lbErrorNoPersonal);
    configurarLimpieza(tfNumeroLicencia, lbErrorLicencia);
    configurarLimpieza(tfCurp, lbErrorCurp);

    configurarLimpieza(pfContrasena, lbErrorPassword);

    // LIMPIAR ERROR ROL
    cbRol.valueProperty().addListener((obs, oldV, newV) -> {

        lbErrorRol.setVisible(false);
        lbErrorRol.setManaged(false);
    });

    // LIMPIAR ERROR SUCURSAL
    cbSucursal.valueProperty().addListener((obs, oldV, newV) -> {

        lbErrorSucursal.setVisible(false);
        lbErrorSucursal.setManaged(false);
    });

    tfCurp.textProperty().addListener((obs, oldV, newV) -> {

        String texto = newV.toUpperCase();

        if (texto.length() > 18) {

            texto = texto.substring(0, 18);
        }

        if (!texto.equals(tfCurp.getText())) {

            tfCurp.setText(texto);
        }
    });

    tfNoPersonal.textProperty().addListener((obs, oldV, newV) -> {

        String texto = newV.toUpperCase();

        if (!texto.equals(tfNoPersonal.getText())) {

            tfNoPersonal.setText(texto);
        }
    });

    tfNumeroLicencia.textProperty().addListener((obs, oldV, newV) -> {

        String texto = newV.toUpperCase();

        if (!texto.equals(tfNumeroLicencia.getText())) {

            tfNumeroLicencia.setText(texto);
        }
    });
}

   private void configurarLimpieza(TextField tf, Label lb) {

    tf.textProperty().addListener((obs, oldV, newV) -> {

        tf.setStyle(Constantes.ESTILO_NORMAL);

        lb.setVisible(false);
        lb.setManaged(false);
    });
}

    private void configurarLimpieza(PasswordField pf, Label lb) {

    pf.textProperty().addListener((obs, oldV, newV) -> {

        pf.setStyle(Constantes.ESTILO_NORMAL);

        lb.setVisible(false);
        lb.setManaged(false);
    });
}
    
    private void limpiarErrores() {

    tfNombre.setStyle(Constantes.ESTILO_NORMAL);
    tfPaterno.setStyle(Constantes.ESTILO_NORMAL);
    tfMaterno.setStyle(Constantes.ESTILO_NORMAL);
    tfCurp.setStyle(Constantes.ESTILO_NORMAL);
    tfCorreo.setStyle(Constantes.ESTILO_NORMAL);
    tfNoPersonal.setStyle(Constantes.ESTILO_NORMAL);
    tfNumeroLicencia.setStyle(Constantes.ESTILO_NORMAL);
    pfContrasena.setStyle(Constantes.ESTILO_NORMAL);

    lbErrorNombre.setVisible(false);
    lbErrorPaterno.setVisible(false);
    lbErrorCurp.setVisible(false);
    lbErrorCorreo.setVisible(false);
    lbErrorNoPersonal.setVisible(false);
    lbErrorPassword.setVisible(false);
    lbErrorRol.setVisible(false);
    lbErrorSucursal.setVisible(false);
    lbErrorLicencia.setVisible(false);
    
    lbErrorNombre.setManaged(false);
    lbErrorPaterno.setManaged(false);
    lbErrorCurp.setManaged(false);
    lbErrorCorreo.setManaged(false);
    lbErrorNoPersonal.setManaged(false);
    lbErrorPassword.setManaged(false);
    lbErrorRol.setManaged(false);
    lbErrorSucursal.setManaged(false);
    lbErrorLicencia.setManaged(false);
}

    private void seleccionarRol(int idRol) {

        for (Rol r : cbRol.getItems()) {

            if (r.getIdRol() == idRol) {

                cbRol.getSelectionModel().select(r);
                break;
            }
        }
    }

    private void seleccionarSucursal(String codigoSucursal) {

        for (Sucursal s : cbSucursal.getItems()) {

            if (s.getCodigoSucursal().equals(codigoSucursal)) {

                cbSucursal.getSelectionModel().select(s);
                break;
            }
        }
    }

    @FXML
    private void clicCancelar(ActionEvent event) {

        if (Utilidades.mostrarAlertaConfirmacion(
                "Cancelar",
                "¿Deseas salir sin guardar los cambios?"
        )) {

            cerrarVentana();
        }
    }

    private void cerrarVentana() {

        ((Stage) tfNombre.getScene().getWindow()).close();
    }
}