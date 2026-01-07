package clienteescritorio;

import clienteescritorio.utilidad.Utilidades;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class FXMLMenuPrincipalController implements Initializable {

    @FXML
    private BorderPane bpPrincipal;
    @FXML
    private Label lbNombreUsuario;
    @FXML
    private StackPane pnlContenido;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarPantalla("FXMLColaboradores"); 
    }    

    private void cargarPantalla(String nombreFXML) {
        try {
            Parent vista = FXMLLoader.load(getClass().getResource(nombreFXML + ".fxml"));
            bpPrincipal.setCenter(vista);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicColaboradores(ActionEvent event) {
        cargarPantalla("FXMLColaboradores");
    }

    @FXML
    private void clicUnidades(ActionEvent event) {
        cargarPantalla("FXMLUnidades");
    }
    
    @FXML
    private void clicSucursales(ActionEvent event) {
        cargarPantalla("FXMLSucursales");
    }
    
    @FXML
    private void clicClientes(ActionEvent event) {
        cargarPantalla("FXMLClientes"); 
    }

    @FXML
    private void clicEnvios(ActionEvent event) {
        cargarPantalla("FXMLEnvios");
    }
    

    @FXML
    private void clicCerrarSesion(ActionEvent event) {
    try {
        // Carga el formulario de inicio de sesión
        Parent loginRoot = FXMLLoader.load(getClass().getResource("FXMLInicioSesion.fxml"));

        // Crea nueva escena y la muestra
        Stage loginStage = new Stage();
        loginStage.setTitle("Inicio de Sesión");
        loginStage.setScene(new javafx.scene.Scene(loginRoot));
        loginStage.show();

        // Cierra la ventana actual (menú principal)
        Stage actualStage = (Stage) bpPrincipal.getScene().getWindow();
        actualStage.close();

    } catch (IOException ex) {
        ex.printStackTrace();
        Utilidades.mostrarAlertaSimple("Error", "No se pudo cargar el formulario de inicio de sesión", Alert.AlertType.ERROR);
    }
}

}