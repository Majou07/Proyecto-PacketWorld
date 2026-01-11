package clienteescritorio;

import clienteescritorio.dominio.EnvioImp;
import clienteescritorio.dto.Respuesta;
import clienteescritorio.pojo.Envio;
import clienteescritorio.utilidad.Utilidades;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class FXMLModalEstatusController implements Initializable {

    @FXML private TextArea taComentario;
    private Envio envioSeleccionado;
    private int idNuevoEstatus = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) { }    

    public void inicializarEnvio(Envio envio){
        this.envioSeleccionado = envio;
    }

    @FXML
    private void clicTransito(ActionEvent event) { 
        idNuevoEstatus = 3; 
    } // IDs según BD
    
    @FXML
    private void clicDetenido(ActionEvent event) { 
        idNuevoEstatus = 4; 
    }
    
    @FXML
    private void clicEntregado(ActionEvent event) { 
        idNuevoEstatus = 5; 
    }
    
    @FXML
    private void clicCancelado(ActionEvent event) { 
        idNuevoEstatus = 6; 
    }

    @FXML
    private void clicActualizar(ActionEvent event) {
        if(idNuevoEstatus == 0){
            Utilidades.mostrarAlertaSimple("Selección", "Selecciona un botón de estatus primero", Alert.AlertType.WARNING);
            return;
        }
            if((idNuevoEstatus == 4 || idNuevoEstatus == 6) && taComentario.getText().trim().isEmpty()){
             Utilidades.mostrarAlertaSimple("Comentario requerido", 
                     "Es obligatorio agregar un motivo para el estatus Detenido o Cancelado", 
                     Alert.AlertType.WARNING);
             return;
        }
        int idColaboradorSesion = 1; 

        Respuesta resp = EnvioImp.actualizarEstatus(
                envioSeleccionado.getIdEnvio(), 
                idNuevoEstatus, 
                taComentario.getText(), 
                idColaboradorSesion
        );

        if(!resp.isError()){
            Utilidades.mostrarAlertaSimple("Éxito", resp.getMensaje(), Alert.AlertType.INFORMATION);
            ((Stage) taComentario.getScene().getWindow()).close();
        } else {
            Utilidades.mostrarAlertaSimple("Error", resp.getMensaje(), Alert.AlertType.ERROR);
        }
    }
}