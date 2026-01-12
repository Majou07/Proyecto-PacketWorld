package clienteescritorio;

import clienteescritorio.dominio.PaqueteImp;
import clienteescritorio.dominio.EnvioImp;
import clienteescritorio.pojo.Envio;
import clienteescritorio.pojo.Paquete;
import clienteescritorio.pojo.HistorialEstatus;
import clienteescritorio.utilidad.Utilidades;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class FXMLDetalleEnvioController implements Initializable {
    @FXML private Label lbGuia;
    @FXML private TableView<Paquete> tvPaquetes;
    @FXML private TableColumn colDescripcion, colPeso, colDimensiones;
    
    @FXML private TableView<HistorialEstatus> tvHistorial;
    @FXML private TableColumn colEstatus, colFecha, colColaborador, colComentario;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTablas();
    }

    private void configurarTablas() {
        // Configura tabla de paquetes
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));
        colDimensiones.setCellValueFactory(new PropertyValueFactory<>("dimensiones"));

        // Configura tabla de historial
        colEstatus.setCellValueFactory(new PropertyValueFactory<>("estatus"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaCambio"));
        colColaborador.setCellValueFactory(new PropertyValueFactory<>("nombreColaborador"));
        colComentario.setCellValueFactory(new PropertyValueFactory<>("comentario"));
        
        tvHistorial.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && tvHistorial.getSelectionModel().getSelectedItem() != null) {
                HistorialEstatus seleccionado = tvHistorial.getSelectionModel().getSelectedItem();

                if (seleccionado.getComentario() != null && !seleccionado.getComentario().isEmpty()) {
                    mostrarModalComentario(seleccionado.getEstatus(), seleccionado.getComentario());
                }
            }
        });
    }
    
    private void mostrarModalComentario(String estatus, String comentario) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Detalle de Observación");
        alerta.setHeaderText("Comentario del estatus: " + estatus);

        TextArea textArea = new TextArea(comentario);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefHeight(150);
        textArea.setPrefWidth(350);

        alerta.getDialogPane().setContent(textArea);
        alerta.showAndWait();
    }
    
    private void cargarHistorial(int idEnvio) {
    HashMap<String, Object> respuesta = EnvioImp.obtenerHistorial(idEnvio);
    
    if (!(boolean) respuesta.get("error")) {
        List<HistorialEstatus> listaHistorial = (List<HistorialEstatus>) respuesta.get("historial");
        
        tvHistorial.setItems(FXCollections.observableArrayList(listaHistorial));
    } else {
        Utilidades.mostrarAlertaSimple("Error de carga", 
                (String) respuesta.get("mensaje"), 
                Alert.AlertType.ERROR);
    }
}

    public void inicializarDetalles(Envio envio) {
    this.lbGuia.setText("No. Guía: " + envio.getNumeroGuia()); 
    
    HashMap<String, Object> respPaquetes = PaqueteImp.obtenerPaquetesPorEnvio(envio.getIdEnvio());
    
    if (!(boolean) respPaquetes.get("error")) {
        List<Paquete> lista = (List<Paquete>) respPaquetes.get("paquetes");
        tvPaquetes.setItems(FXCollections.observableArrayList(lista)); 
    } else {
        Utilidades.mostrarAlertaSimple("Error", (String) respPaquetes.get("mensaje"), Alert.AlertType.ERROR);
        }

        cargarHistorial(envio.getIdEnvio()); 
    }
}