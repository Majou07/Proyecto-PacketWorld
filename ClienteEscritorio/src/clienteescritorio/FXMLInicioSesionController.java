package clienteescritorio;

import clienteescritorio.dominio.InicioSesionImp;
import clienteescritorio.dto.RSAutenticacionColaborador;
import clienteescritorio.utilidad.Utilidades;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FXMLInicioSesionController implements Initializable {

    @FXML private TextField tfNoPersonal;
    @FXML private PasswordField pfContrasena;

    @Override
    public void initialize(URL url, ResourceBundle rb) { }    

    @FXML
    private void clicIniciarSesion(ActionEvent event) {
        String noPersonal = tfNoPersonal.getText();
        String password = pfContrasena.getText();
        
        if(noPersonal.isEmpty() || password.isEmpty()){
            Utilidades.mostrarAlertaSimple("Campos vacíos", "Por favor ingresa tus credenciales", Alert.AlertType.WARNING);
        } else {
            // Llamada a la implementación corregida
            RSAutenticacionColaborador respuesta = InicioSesionImp.verificarCredenciales(noPersonal, password);
            
            if(!respuesta.isError()){
                Utilidades.mostrarAlertaSimple("Bienvenido", 
                        "Bienvenido(a) " + respuesta.getColaborador().getNombre(), 
                        Alert.AlertType.INFORMATION);
                irMenuPrincipal();
            } else {
                Utilidades.mostrarAlertaSimple("Error de acceso", respuesta.getMensaje(), Alert.AlertType.ERROR);
            }
        }
    }
    
    private void irMenuPrincipal() {
        try {
            Stage stage = (Stage) tfNoPersonal.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("FXMLMenuPrincipal.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Sistema Packet World");
            stage.centerOnScreen();
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}