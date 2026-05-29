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
    private Paquete paqueteEdicion;
    private boolean esEdicion = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) { }    
    
    public void setEnvio(int idEnvio){
        this.idEnvio = idEnvio;
        this.esEdicion = false;
    }
    
    
    public void prepararFormulario(Paquete paquete) {
        this.paqueteEdicion = paquete;
        this.idEnvio = paquete.getIdEnvio();
        this.esEdicion = true;
        
        // Llenamos los campos con la información del paquete seleccionado
        tfDescripcion.setText(paquete.getDescripcion());
        tfPeso.setText(paquete.getPeso().toString());
        tfAlto.setText(paquete.getAlto().toString());
        tfAncho.setText(paquete.getAncho().toString());
        tfProfundidad.setText(paquete.getProfundidad().toString());
    }
    

    @FXML
    private void clicGuardar(ActionEvent event) {
        try {
            if (tfDescripcion.getText().isEmpty() || tfPeso.getText().isEmpty()) {
                Utilidades.mostrarAlertaSimple("Campos vacíos", 
                    "Por favor, completa la descripción y el peso.", Alert.AlertType.WARNING);
                return;
            }

            Paquete paquete = (esEdicion) ? paqueteEdicion : new Paquete();
            paquete.setIdEnvio(idEnvio);
            paquete.setDescripcion(tfDescripcion.getText());
            paquete.setPeso(Float.parseFloat(tfPeso.getText()));
            paquete.setAlto(Float.parseFloat(tfAlto.getText()));
            paquete.setAncho(Float.parseFloat(tfAncho.getText()));
            paquete.setProfundidad(Float.parseFloat(tfProfundidad.getText()));

            Respuesta resp;
            if (esEdicion) {
                resp = PaqueteImp.editar(paquete);
            } else {
                resp = PaqueteImp.registrar(paquete);
            }

            if(!resp.isError()){
                Utilidades.mostrarAlertaSimple("Éxito", resp.getMensaje(), Alert.AlertType.INFORMATION);

                try {
                    Stage stage = (Stage) tfDescripcion.getScene().getWindow();
                } catch (Exception e) {
                    System.out.println("No se pudo refrescar automáticamente");
                }

                cerrarVentana();
            } else {
                Utilidades.mostrarAlertaSimple("Error", resp.getMensaje(), Alert.AlertType.ERROR);
            }
        } catch(NumberFormatException e){
            Utilidades.mostrarAlertaSimple("Error", 
                "Ingresa valores numéricos válidos (ej. 10.5)", Alert.AlertType.WARNING);
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