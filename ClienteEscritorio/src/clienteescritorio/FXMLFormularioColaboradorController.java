package clienteescritorio;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class FXMLFormularioColaboradorController implements Initializable {
    private Colaborador colaboradorEdicion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    public void inicializarValores(Colaborador colaborador){
        this.colaboradorEdicion = colaborador;
        if(colaborador != null){
            lbTitulo.setText("Actualizar Colaborador");
        }
    }

    @FXML
    private void clicGuardar(ActionEvent event) {
        cerrarVentana();
    }
    
    @FXML
    private void clicCancelar(ActionEvent event) {
        cerrarVentana();
    }
    
    private void cerrarVentana(){
        ((Stage) tfNombre.getScene().getWindow()).close();
    }
}