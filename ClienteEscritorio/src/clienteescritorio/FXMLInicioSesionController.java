package clienteescritorio;

import clienteescritorio.utilidad.Utilidades;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class FXMLInicioSesionController implements Initializable {

    @FXML
    private TextField tfNoPersonal;
    @FXML
    private PasswordField pfContrasena;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Todo
    }    

    @FXML
    private void clicIniciarSesion(ActionEvent event) {
        String noPersonal = tfNoPersonal.getText();
        String password = pfContrasena.getText();
        
        if(noPersonal.isEmpty() || password.isEmpty()){
            Utilidades.mostrarAlertaSimple("Campos vacíos", "Por favor ingresa tus credenciales", Alert.AlertType.WARNING);
        } else {

        }
    }
}
