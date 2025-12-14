package clienteescritorio;

import clienteescritorio.pojo.Unidad;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class FXMLFormularioUnidadController implements Initializable {
    
    @FXML private Label lbTitulo;
    @FXML private TextField tfMarca;
    @FXML private TextField tfModelo;
    @FXML private TextField tfAnio;
    @FXML private TextField tfVin;
    @FXML private ComboBox cbTipo;
    @FXML private TextField tfNii;
    
    private Unidad unidadEdicion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    public void inicializarValores(Unidad unidad){
        this.unidadEdicion = unidad;
        if(unidad != null){
            lbTitulo.setText("Actualizar Registro");
            // Setear valores
            tfVin.setDisable(true); 
            tfNii.setDisable(true);
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
        ((Stage) tfMarca.getScene().getWindow()).close();
    }
}