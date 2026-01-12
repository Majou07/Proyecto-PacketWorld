package clienteescritorio;

import clienteescritorio.dominio.UnidadImp;
import clienteescritorio.dto.Respuesta;
import clienteescritorio.pojo.TipoUnidad;
import clienteescritorio.pojo.Unidad;
import clienteescritorio.utilidad.Utilidades;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    @FXML private ComboBox<TipoUnidad> cbTipo;
    @FXML private TextField tfNii;
    @FXML private Label lbErrorMarca, lbErrorModelo, lbErrorAnio, lbErrorVin, lbErrorTipo;
    
    private Unidad unidadEdicion;
    
    private ObservableList<TipoUnidad> tipos;
    @FXML
    private ComboBox<String> cbEstado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarTipos();
        configurarValidacionesDinamicas();
    }    
    
    public void inicializarValores(Unidad unidad){
        this.unidadEdicion = unidad;
        if(unidad != null){
            lbTitulo.setText("Actualizar Registro");
            // Setear valores
            tfMarca.setText(unidad.getMarca());
            tfModelo.setText(unidad.getModelo());
            tfAnio.setText(String.valueOf(unidad.getAnio()));
            tfVin.setText(unidad.getVin());
            tfNii.setText(unidad.getNii());
            
            tfVin.setDisable(true); 
            tfNii.setDisable(true);
            
            
            
            for(TipoUnidad tipo : tipos){
                if(tipo.getNombre().equals(unidad.getTipoUnidad())){
                    cbTipo.getSelectionModel().select(tipo);
                    break;
                }
            }
            
            cbEstado.setVisible(true);
            cbEstado.getItems().clear();
              cbEstado.getItems().addAll("Activo","En mantenimiento");
            
            if(unidad.getIdEstatusUnidad() == 1){
              cbEstado.getSelectionModel().select("Activo");
            }else if(unidad.getIdEstatusUnidad()==3){
                cbEstado.getSelectionModel().select("En mantenimiento");
            } 
            
            tfAnio.textProperty().addListener((obs, oldVal, newVal) ->{
                actualizarNii();
            });   
        }else{
            cbEstado.setVisible(false);
        }
    }
    
    private void configurarValidacionesDinamicas() {
        // Solo números en el año
        tfAnio.textProperty().addListener((obs, oldV, newV) -> {
            if (!newV.matches("\\d*")) tfAnio.setText(newV.replaceAll("[^\\d]", ""));
            if (newV.length() > 4) tfAnio.setText(oldV);
        });
        
        // VIN siempre en mayúsculas y máximo 17
        tfVin.textProperty().addListener((obs, oldV, newV) -> {
            tfVin.setText(newV.toUpperCase());
            if (newV.length() > 17) tfVin.setText(oldV);
            lbErrorVin.setVisible(false);
        });
    }

    @FXML
    private void clicGuardar(ActionEvent event) {
        if (!validarCampos()) {
            return; 
        }

        Unidad nuevaUnidad = new Unidad();
        nuevaUnidad.setMarca(tfMarca.getText());
        nuevaUnidad.setModelo(tfModelo.getText());
        nuevaUnidad.setAnio(Integer.parseInt(tfAnio.getText()));
        nuevaUnidad.setVin(tfVin.getText());
        nuevaUnidad.setNii(tfNii.getText());
        TipoUnidad tipoSeleccionado = cbTipo.getValue();
        nuevaUnidad.setIdTipoUnidad(tipoSeleccionado.getIdTipoUnidad());

        if(unidadEdicion == null){
            nuevaUnidad.setIdEstatusUnidad(1);
        }else{
            String estadoSeleccionado = cbEstado.getValue();
            if(estadoSeleccionado != null){
            if(estadoSeleccionado.equals("Activo")){
                nuevaUnidad.setIdEstatusUnidad(1);
            }else if(estadoSeleccionado.equals("En mantenimiento")){
                nuevaUnidad.setIdEstatusUnidad(3);
            }   

            }else{
                nuevaUnidad.setIdEstatusUnidad(unidadEdicion.getIdEstatusUnidad());
          }
    }

    Respuesta respuesta;
        if(unidadEdicion == null){
            respuesta=UnidadImp.registrar(nuevaUnidad);
        }else{
            nuevaUnidad.setIdUnidad(unidadEdicion.getIdUnidad());
            respuesta = UnidadImp.editar(nuevaUnidad);
        }

        if (!respuesta.isError()) {
        String titulo = (unidadEdicion == null) ? "Registro exitoso" : "Actualización exitosa";
        Utilidades.mostrarAlertaSimple(titulo, 
            respuesta.getMensaje(), Alert.AlertType.INFORMATION);
        cerrarVentana();
        }else{
        Utilidades.mostrarAlertaSimple("Error", 
            respuesta.getMensaje(), Alert.AlertType.ERROR);
        }

    }

    private void cargarTipos() {
        tipos = FXCollections.observableArrayList();
        HashMap<String, Object> respuesta = UnidadImp.obtenerTipos(); 
        if(!(boolean)respuesta.get("error")){
            tipos.addAll((List<TipoUnidad>)respuesta.get("tipos"));
            cbTipo.setItems(tipos);
        }
    }

    
    @FXML
     private void clicCancelar(ActionEvent event) {
        if(Utilidades.mostrarAlertaConfirmacion("Cancelar", "¿Deseas salir sin guardar los cambios?")){
            cerrarVentana();
        }
    }
    
    private void cerrarVentana(){
        ((Stage) tfMarca.getScene().getWindow()).close();
    }
    

    private boolean validarCampos() {
        boolean valido = true;
        valido &= revisarVacio(tfMarca, lbErrorMarca);
        valido &= revisarVacio(tfModelo, lbErrorModelo);
        
        // Validación Año
        try {
            int anio = Integer.parseInt(tfAnio.getText());
            int anioActual = java.time.Year.now().getValue();
            if (anio < 1900 || anio > anioActual + 1) {
                lbErrorAnio.setText("Año inválido");
                lbErrorAnio.setVisible(true);
                valido = false;
            } else {
                lbErrorAnio.setVisible(false);
            }
        } catch (Exception e) {
            lbErrorAnio.setVisible(true);
            valido = false;
        }

        if (tfVin.getText().length() != 17) {
            tfVin.setStyle("-fx-border-color: red;");
            lbErrorVin.setVisible(true);
            valido = false;
        } else {
            tfVin.setStyle("");
            lbErrorVin.setVisible(false);
        }

        if (cbTipo.getValue() == null) {
            lbErrorTipo.setVisible(true);
            valido = false;
        }

        return valido;
    }
    
    private boolean revisarVacio(TextField tf, Label lb) {
        if (tf.getText().trim().isEmpty()) {
            tf.setStyle("-fx-border-color: red;");
            lb.setVisible(true);
            return false;
        } else {
            tf.setStyle("");
            lb.setVisible(false);
            return true;
        }
    }
    
    
    private void actualizarNii() {
    String vin = tfVin.getText();

    if (vin == null || vin.length() < 4) return;

    try {
        int nuevoAnio = Integer.parseInt(tfAnio.getText());
        String parteVin = vin.substring(0, 4); 
        String nuevoNii = nuevoAnio + "-" + parteVin;
        tfNii.setText(nuevoNii);
    } catch (NumberFormatException e) {
        tfNii.setText(""); 
    }
    
    
}

}