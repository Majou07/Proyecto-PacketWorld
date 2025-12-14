package clienteescritorio;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class FXMLMenuPrincipalController implements Initializable {

    @FXML
    private BorderPane bpPrincipal;
    @FXML
    private Label lbNombreUsuario;
    @FXML
    private StackPane pnlContenido;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarPantalla("FXMLColaboradores"); // Pantalla por defecto
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
    private void clicCerrarSesion(ActionEvent event) {
        // Lógica para cerrar ventana y volver al login
    }
}