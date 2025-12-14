package clienteescritorio;

import clienteescritorio.dominio.PaqueteImp;
import clienteescritorio.dto.Respuesta;
import clienteescritorio.pojo.Paquete;
import clienteescritorio.utilidad.Utilidades;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FXMLFormularioPaqueteController implements Initializable {

    @FXML private TextField tfDescripcion;
    @FXML private TextField tfPeso;
    @FXML private TextField tfAlto;
    @FXML private TextField tfAncho;
    @FXML private TextField tfProfundidad;
    
    private int idEnvio;

    @Override
    public void initialize(URL url, ResourceBundle rb) { }    
    
    public void setEnvio(int idEnvio){
        this.idEnvio = idEnvio;
    }

    @FXML
    private void clicGuardar(ActionEvent event) {
        try {
            Paquete paquete = new Paquete();
            paquete.setIdEnvio(idEnvio);
            paquete.setDescripcion(tfDescripcion.getText());
            paquete.setPeso(Float.parseFloat(tfPeso.getText()));
            paquete.setAlto(Float.parseFloat(tfAlto.getText()));
            paquete.setAncho(Float.parseFloat(tfAncho.getText()));
            paquete.setProfundidad(Float.parseFloat(tfProfundidad.getText()));
            
            Respuesta resp = PaqueteImp.registrar(paquete);
            if(!resp.isError()){
                Utilidades.mostrarAlertaSimple("Éxito", "Paquete agregado", Alert.AlertType.INFORMATION);
                cerrarVentana();
            } else {
                Utilidades.mostrarAlertaSimple("Error", resp.getMensaje(), Alert.AlertType.ERROR);
            }
        } catch(NumberFormatException e){
            Utilidades.mostrarAlertaSimple("Error", "Ingresa valores numéricos válidos", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        cerrarVentana();
    }
    
    private void cerrarVentana(){
        ((Stage) tfDescripcion.getScene().getWindow()).close();
    }
}