package clienteescritorio;

import clienteescritorio.dominio.ColaboradorImp;
import clienteescritorio.dominio.SucursalImp;
import clienteescritorio.dto.Respuesta;
import clienteescritorio.pojo.Colaborador;
import clienteescritorio.pojo.Rol;
import clienteescritorio.pojo.Sucursal;
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
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
    
    @FXML private Label lbErrorNombre, lbErrorPaterno, lbErrorCurp, lbErrorCorreo, 
            lbErrorNoPersonal, lbErrorPassword, lbErrorRol, lbErrorSucursal, lbErrorLicencia;
    

    private Colaborador colaboradorEdicion;
    private File archivoFoto;
    private ObservableList<Rol> roles;
    private ObservableList<Sucursal> sucursales;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarRoles();
        cargarSucursales();
        
        cbRol.setOnAction(e -> {
        boolean esConductor = cbRol.getValue() != null
                && cbRol.getValue().getNombreRol().equalsIgnoreCase("Conductor");

        tfNumeroLicencia.setDisable(!esConductor);
        if (!esConductor) tfNumeroLicencia.clear();
    });
    }    
    
    public void inicializarValores(Colaborador colaborador){
        this.colaboradorEdicion = colaborador;
        if(colaborador != null){
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
            
            tfNoPersonal.setDisable(true);
            cbRol.setDisable(true);
            
            if(cbRol.getValue() !=null && cbRol.getValue().getNombreRol().equalsIgnoreCase("Conductor")){
            tfNumeroLicencia.setDisable(false);
            tfNumeroLicencia.setText(colaborador.getNumeroLicencia() != null ? colaborador.getNumeroLicencia(): "");
            
        }else{
                tfNumeroLicencia.setDisable(true);
                tfNumeroLicencia.clear();
            }
            
            
            
            cargarFotoServidor(colaborador.getIdColaborador());
        }
    }

    private void cargarRoles() {
        roles = FXCollections.observableArrayList();
        HashMap<String, Object> respuesta = ColaboradorImp.obtenerRoles(); 
        if(!(boolean)respuesta.get("error")){
            roles.addAll((List<Rol>)respuesta.get("roles"));
            cbRol.setItems(roles);
        }
    }

    private void cargarSucursales() {
        sucursales = FXCollections.observableArrayList();
        HashMap<String, Object> respuesta = SucursalImp.obtenerSucursales(); 
        if(!(boolean)respuesta.get("error")){
            sucursales.addAll((List<Sucursal>)respuesta.get("sucursales"));
            cbSucursal.setItems(sucursales);
        }
    }

    @FXML
    private void clicSubirFoto(ActionEvent event) {
        FileChooser dialogo = new FileChooser();
        dialogo.setTitle("Selecciona una foto");
        FileChooser.ExtensionFilter filtroImg = new FileChooser.ExtensionFilter("Archivos de imagen (*.jpg, *.png)", "*.jpg", "*.png");
        dialogo.getExtensionFilters().add(filtroImg);
        
        archivoFoto = dialogo.showOpenDialog(tfNombre.getScene().getWindow());
        
        if(archivoFoto != null){
            try {
                BufferedImage bufferImg = ImageIO.read(archivoFoto);
                Image imagen = SwingFXUtils.toFXImage(bufferImg, null);
                ivFoto.setImage(imagen);
            } catch (IOException e) {
                Utilidades.mostrarAlertaSimple("Error", "No se pudo cargar la imagen seleccionada", Alert.AlertType.ERROR);
            }
        }
    }
    
    private void mostrarFotoServidor(String base64) {
        try {
            byte[] fotoBytes = Base64.getDecoder().decode(base64.replaceAll("\\n", ""));
            ByteArrayInputStream stream = new ByteArrayInputStream(fotoBytes);
            Image imagen = new Image(stream);
            ivFoto.setImage(imagen);
        } catch (Exception e) {
            System.out.println("Error al decodificar imagen: " + e.getMessage());
        }
    }

   @FXML
    private void clicGuardar(ActionEvent event) {
    if (!validarCampos()) return;

    Colaborador colaborador = new Colaborador();
    colaborador.setNombre(tfNombre.getText());
    colaborador.setApellidoPaterno(tfPaterno.getText());
    colaborador.setApellidoMaterno(tfMaterno.getText());
    colaborador.setCurp(tfCurp.getText());
    colaborador.setCorreoElectronico(tfCorreo.getText());
    colaborador.setNumeroPersonal(tfNoPersonal.getText());
    colaborador.setContrasena(pfContrasena.getText());

    if(cbRol.getSelectionModel().getSelectedItem() != null)
        colaborador.setIdRol(cbRol.getSelectionModel().getSelectedItem().getIdRol());

    if(cbSucursal.getSelectionModel().getSelectedItem() != null)
        colaborador.setCodigoSucursal(cbSucursal.getSelectionModel().getSelectedItem().getCodigoSucursal());

    if (!tfNumeroLicencia.isDisabled())
        colaborador.setNumeroLicencia(tfNumeroLicencia.getText());

    // ================= Foto =================
    if (archivoFoto != null) {
        try {
            byte[] bytesFoto = Files.readAllBytes(archivoFoto.toPath());
            colaborador.setFoto(bytesFoto);
        } catch (IOException ex) {
            Utilidades.mostrarAlertaSimple("Error", "Error al procesar la foto para envío", Alert.AlertType.ERROR);
            return;
        }
    }

    if (colaboradorEdicion == null) {
        // Registrar colaborador
        Respuesta respuesta = ColaboradorImp.registrar(colaborador);
        if (!respuesta.isError()) {
            HashMap<String, Object> resultado = ColaboradorImp.buscarPorNumeroPersonal(colaborador.getNumeroPersonal());
            if (!(boolean) resultado.get("error")) {
                List<Colaborador> lista = (List<Colaborador>) resultado.get("colaboradores");
                if (!lista.isEmpty()) {
                    int id = lista.get(0).getIdColaborador();
                    colaborador.setIdColaborador(id);

                    if (colaborador.getFoto() != null) {
                        Respuesta respFoto = ColaboradorImp.subirFoto(id, colaborador.getFoto());
                        if (respFoto.isError()) {
                            Utilidades.mostrarAlertaSimple("Advertencia", "Colaborador registrado pero la foto no se pudo subir", Alert.AlertType.WARNING);
                        }
                    }
                }
            }
            Utilidades.mostrarAlertaSimple("Éxito", respuesta.getMensaje(), Alert.AlertType.INFORMATION);
            cerrarVentana();
        } else {
            Utilidades.mostrarAlertaSimple("Error", respuesta.getMensaje(), Alert.AlertType.ERROR);
        }

        } else {
            colaborador.setIdColaborador(colaboradorEdicion.getIdColaborador());
            Respuesta respuesta = ColaboradorImp.editar(colaborador);
            if (!respuesta.isError()) {
                if (archivoFoto != null && colaborador.getFoto() != null) {
                    Respuesta respFoto = ColaboradorImp.subirFoto(colaborador.getIdColaborador(), colaborador.getFoto());
                    if (respFoto.isError()) {
                        Utilidades.mostrarAlertaSimple("Advertencia", "Colaborador actualizado pero la foto no se pudo subir", Alert.AlertType.WARNING);
                    }
                }
                Utilidades.mostrarAlertaSimple("Éxito", respuesta.getMensaje(), Alert.AlertType.INFORMATION);
                cerrarVentana();
            } else {
                Utilidades.mostrarAlertaSimple("Error", respuesta.getMensaje(), Alert.AlertType.ERROR);
            }
        }
    }

    
    
    private void registrarColaborador(Colaborador colaborador){
    Respuesta respuesta = ColaboradorImp.registrar(colaborador);
    if(!respuesta.isError()){
        HashMap<String, Object> resultado = ColaboradorImp.buscarPorNumeroPersonal(colaborador.getNumeroPersonal());
        if(!(boolean)resultado.get("error")){
            List<Colaborador> lista = (List<Colaborador>)resultado.get("colaboradores");
            if(!lista.isEmpty()){
                int id = lista.get(0).getIdColaborador();
                colaborador.setIdColaborador(id);

                // Subir foto si existe
                if(colaborador.getFoto() != null){
                    Respuesta respFoto = ColaboradorImp.subirFoto(id, colaborador.getFoto());
                    if(respFoto.isError()){
                        Utilidades.mostrarAlertaSimple("Advertencia", "Colaborador registrado pero la foto no se pudo subir", Alert.AlertType.WARNING);
                    }
                }
            }
        }
        Utilidades.mostrarAlertaSimple("Éxito", respuesta.getMensaje(), Alert.AlertType.INFORMATION);
        cerrarVentana();
        } else {
            Utilidades.mostrarAlertaSimple("Error", respuesta.getMensaje(), Alert.AlertType.ERROR);
        }
    }


    private void actualizarColaborador(Colaborador colaborador){
        Respuesta respuesta = ColaboradorImp.editar(colaborador);
        if(!respuesta.isError()){
            //  Subir foto si existe
            if(colaborador.getFoto() != null){
                Respuesta respFoto = ColaboradorImp.subirFoto(colaborador.getIdColaborador(), colaborador.getFoto());
                if(respFoto.isError()){
                    Utilidades.mostrarAlertaSimple("Advertencia", "Colaborador actualizado pero la foto no se pudo subir", Alert.AlertType.WARNING);
                }
            }
            Utilidades.mostrarAlertaSimple("Éxito", respuesta.getMensaje(), Alert.AlertType.INFORMATION);
            cerrarVentana();
        } else {
            Utilidades.mostrarAlertaSimple("Error", respuesta.getMensaje(), Alert.AlertType.ERROR);
        }
    }

    private void cargarFotoServidor(int idColaborador){
        Colaborador c = ColaboradorImp.obtenerFoto(idColaborador);
        if(c != null && c.getFotografia() != null){
            mostrarFotoServidor(c.getFotografia());
        } else {
            ivFoto.setImage(null); 
        }
    }

    

    private boolean validarCampos() {
        boolean valido = true;
        valido &= revisarVacio(tfNombre, lbErrorNombre);
        valido &= revisarVacio(tfPaterno, lbErrorPaterno);
        valido &= revisarVacio(pfContrasena, lbErrorPassword);

        // Validación CURP 18 caracteres
        if (tfCurp.getText().length() != 18) {
            tfCurp.setStyle("-fx-border-color: red;");
            lbErrorCurp.setVisible(true);
            valido = false;
        } else {
            tfCurp.setStyle("");
            lbErrorCurp.setVisible(false);
        }

        if (!tfCorreo.getText().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            tfCorreo.setStyle("-fx-border-color: red;");
            lbErrorCorreo.setVisible(true);
            valido = false;
        } else {
            tfCorreo.setStyle("");
            lbErrorCorreo.setVisible(false);
        }

        valido &= revisarVacio(tfNoPersonal, lbErrorNoPersonal);

        if (cbRol.getValue() == null) { lbErrorRol.setVisible(true); valido = false; } else { lbErrorRol.setVisible(false); }
        if (cbSucursal.getValue() == null) { lbErrorSucursal.setVisible(true); valido = false; } else { lbErrorSucursal.setVisible(false); }

        if (!tfNumeroLicencia.isDisabled()) {
            valido &= revisarVacio(tfNumeroLicencia, lbErrorLicencia);
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
    

    private void configurarValidacionesDinamicas() {
        tfCurp.textProperty().addListener((obs, oldV, newV) -> {
            tfCurp.setText(newV.toUpperCase());
            if (newV.length() > 18) tfCurp.setText(oldV);
            lbErrorCurp.setVisible(false);
        });

        tfNoPersonal.textProperty().addListener((obs, oldV, newV) -> {
            if (!newV.matches("\\d*")) tfNoPersonal.setText(newV.replaceAll("[^\\d]", ""));
            lbErrorNoPersonal.setVisible(false);
        });

        tfCorreo.textProperty().addListener((obs, oldV, newV) -> lbErrorCorreo.setVisible(false));
    }
    
    private void seleccionarRol(int idRol) {
        for(Rol r : cbRol.getItems()){
            if(r.getIdRol() == idRol){
                cbRol.getSelectionModel().select(r);
                break;
            }
        }
    }
    
    private void seleccionarSucursal(String codigoSucursal) {
        for(Sucursal s : cbSucursal.getItems()){
            if(s.getCodigoSucursal().equals(codigoSucursal)){
                cbSucursal.getSelectionModel().select(s);
                break;
            }
        }
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        if(Utilidades.mostrarAlertaConfirmacion("Cancelar", "¿Deseas salir sin guardar los cambios?")){
            cerrarVentana();
        }
    }
    
    private void cerrarVentana(){
        ((Stage) tfNombre.getScene().getWindow()).close();
    }
}