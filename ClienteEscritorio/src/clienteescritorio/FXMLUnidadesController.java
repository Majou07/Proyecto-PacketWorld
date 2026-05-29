package clienteescritorio;

import clienteescritorio.utilidad.Utilidades;
import clienteescritorio.dominio.UnidadImp;
import clienteescritorio.dto.Respuesta;
import clienteescritorio.pojo.TipoUnidad;
import clienteescritorio.pojo.Unidad;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXMLUnidadesController implements Initializable {

    @FXML private TextField tfBusqueda;
    @FXML private TableView<Unidad> tvUnidades;
    @FXML private TableColumn colMarca;
    @FXML private TableColumn colModelo;
    @FXML private TableColumn colAnio;
    @FXML private TableColumn colVin;
    @FXML private TableColumn colNii;
    @FXML private TableColumn colTipoUnidad;
    @FXML private TableColumn colEstatusUnidad;
    @FXML private TableColumn colMotivoBaja;
    @FXML
    private Button btBusqueda;
    @FXML
    private ComboBox<String> cbFiltro;
    private ObservableList<Unidad> listaUnidades = FXCollections.observableArrayList();
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarDatos();
        
        cbFiltro.getItems().addAll("VIN", "Marca", "NII");
        cbFiltro.setValue("VIN"); // valor por defecto

    }    

    private void configurarTabla() {
        colMarca.setCellValueFactory(new PropertyValueFactory("marca"));
        colModelo.setCellValueFactory(new PropertyValueFactory("modelo"));
        colAnio.setCellValueFactory(new PropertyValueFactory("anio"));
        colVin.setCellValueFactory(new PropertyValueFactory("vin"));
        colNii.setCellValueFactory(new PropertyValueFactory("nii"));
        colTipoUnidad.setCellValueFactory(new PropertyValueFactory("tipoUnidad"));
        colEstatusUnidad.setCellValueFactory(new PropertyValueFactory("estatusUnidad"));
        colMotivoBaja.setCellValueFactory(new PropertyValueFactory("motivoBaja")); 
        
        tvUnidades.setPlaceholder(
        new Label("Cargando unidades...")
    );
       
    }
    
    private void cargarDatos() {
        HashMap<String, Object> respuesta = UnidadImp.obtenerUnidades();
        if(!(boolean)respuesta.get("error")){
            tvUnidades.setItems(FXCollections.observableArrayList((List<Unidad>)respuesta.get("unidades")));
        } else {
            Utilidades.mostrarAlertaSimple("Error", (String)respuesta.get("mensaje"), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicRegistrar(ActionEvent event) {
        irFormulario(null);
    }

    @FXML
    private void clicEditar(ActionEvent event) {
        Unidad seleccionada = tvUnidades.getSelectionModel().getSelectedItem();
        if(seleccionada != null){
            irFormulario(seleccionada);
        } else {
             Utilidades.mostrarAlertaSimple("Selección", "Selecciona una unidad", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicDarBaja(ActionEvent event) {
        Unidad seleccionada = tvUnidades.getSelectionModel().getSelectedItem();
        if(seleccionada == null){
            Utilidades.mostrarAlertaSimple("Selección requerida", 
                    "Debes seleccionar una unidad antes de dar de baja", 
                    Alert.AlertType.WARNING);
            return;
        }
        //Capturar motivo de baja
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Dar de baja unidad");
        dialog.setHeaderText("Motivo de baja");
        dialog.setContentText("Ingresa el motivo:");
        
        dialog.showAndWait().ifPresent(motivo->{
            if(motivo.trim().isEmpty()){
                Utilidades.mostrarAlertaSimple("Error", "Debe especificar un motivo para dar de baja la unidad", 
                        Alert.AlertType.ERROR);
                return;
            }
            Respuesta respuesta = UnidadImp.darBaja(seleccionada.getIdUnidad(), motivo);
            
            if(!respuesta.isError()){
                Utilidades.mostrarAlertaSimple("Exito", respuesta.getMensaje(), 
                        Alert.AlertType.INFORMATION);
                cargarDatos();
            }else{
                Utilidades.mostrarAlertaSimple("Error", respuesta.getMensaje(), 
                        Alert.AlertType.ERROR);
            }
        });
                   
    }
    
    private void irFormulario(Unidad unidad) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLFormularioUnidad.fxml"));
            Parent root = loader.load();
            FXMLFormularioUnidadController controller = loader.getController();
            controller.inicializarValores(unidad);
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            cargarDatos();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
private void clicBuscar(ActionEvent event) {
    String criterio = cbFiltro.getValue(); // VIN, Marca o NII
    String valor = tfBusqueda.getText().trim();

    // Caso 1: si el campo está vacío, recargar la tabla original
    if (valor.isEmpty()) {
        cargarDatos();
        return;
    }


    String vin = null, marca = null, nii = null;

    switch (criterio) {
        case "VIN": vin = valor; break;
        case "Marca": marca = valor; break;
        case "NII": nii = valor; break;
    }

    HashMap<String, Object> respuesta = UnidadImp.buscarUnidades(vin, marca, nii);
    if (!(boolean)respuesta.get("error")) {
        List<Unidad> unidades = (List<Unidad>) respuesta.get("unidades");

        // Caso 3: si no hay resultados, mostrar mensaje
        if (unidades == null || unidades.isEmpty()) {
            Utilidades.mostrarAlertaSimple("Sin resultados", "No se encontraron unidades con ese criterio", Alert.AlertType.INFORMATION);
            return;
        }

        tvUnidades.setItems(FXCollections.observableArrayList(unidades));
    } else {
        Utilidades.mostrarAlertaSimple("Error", (String)respuesta.get("mensaje"), Alert.AlertType.ERROR);
    }
}


}