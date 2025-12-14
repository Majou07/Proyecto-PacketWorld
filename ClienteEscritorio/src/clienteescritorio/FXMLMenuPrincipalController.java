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

public class FXMLMenuPrincipalController implements Initializable {

    @FXML private BorderPane bpPrincipal;
    @FXML private Label lbNombrePersonal;

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

    @FXML private void btnColaboradores(ActionEvent event) { cargarPantalla("FXMLColaboradores"); }
    @FXML private void btnUnidades(ActionEvent event) { cargarPantalla("FXMLUnidades"); }
    @FXML private void btnSucursales(ActionEvent event) { cargarPantalla("FXMLSucursales"); }
    @FXML private void btnClientes(ActionEvent event) { cargarPantalla("FXMLClientes"); }
    @FXML private void btnEnvios(ActionEvent event) { cargarPantalla("FXMLEnvios"); }
    @FXML private void btnCerrarSesion(ActionEvent event) { 
    }
}