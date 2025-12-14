package clienteescritorio;

import clienteescritorio.pojo.Colaborador;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class FXMLFormularioColaboradorController implements Initializable {
    
    @FXML private Label lbTitulo; 
    @FXML private TextField tfNombre;
    @FXML private TextField tfPaterno;
    @FXML private TextField tfMaterno;
    @FXML private TextField tfCurp;
    @FXML private TextField tfCorreo;
    @FXML private TextField tfNoPersonal;
    @FXML private PasswordField pfContrasena;
    @FXML private ComboBox cbRol;
    @FXML private ComboBox cbSucursal;
    @FXML private ImageView ivFoto;

    private Colaborador colaboradorEdicion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    public void inicializarValores(Colaborador colaborador){
        this.colaboradorEdicion = colaborador;
        if(colaborador != null){
            lbTitulo.setText("Actualizar Colaborador");
            tfNombre.setText(colaborador.getNombre());
            // Llenar resto de campos...
        }
    }

    @FXML
    private void clicSubirFoto(ActionEvent event) {
        // Logica foto
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