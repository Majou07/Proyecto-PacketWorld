package clienteescritorio;

import clienteescritorio.dominio.ColaboradorImp;
import clienteescritorio.pojo.Colaborador;
import clienteescritorio.utilidad.Constantes;
import clienteescritorio.utilidad.Utilidades;
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

public class FXMLColaboradoresController implements Initializable {

    @FXML private TextField tfBusqueda;
    @FXML private TableView<Colaborador> tvColaboradores; 
    @FXML private TableColumn colNoPersonal;
    @FXML private TableColumn colNombre;
    @FXML private TableColumn colPaterno;
    @FXML private TableColumn colMaterno;
    @FXML private TableColumn colRol;
    @FXML private TableColumn colSucursal;
    @FXML private TableColumn colNumeroLicencia;
    @FXML private TableColumn colUnidadAsignada;
    
    private ObservableList<Colaborador> colaboradores;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarDatos();
        System.out.println("Colaboradores: " + colaboradores.size());
    }    

    private void configurarTabla() {
        colNoPersonal.setCellValueFactory(new PropertyValueFactory("numeroPersonal"));
        colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        colPaterno.setCellValueFactory(new PropertyValueFactory("apellidoPaterno"));
        colMaterno.setCellValueFactory(new PropertyValueFactory("apellidoMaterno"));
        colRol.setCellValueFactory(new PropertyValueFactory("rol"));
        colSucursal.setCellValueFactory(new PropertyValueFactory("sucursal"));
        colNumeroLicencia.setCellValueFactory(new PropertyValueFactory("numeroLicencia"));
        colUnidadAsignada.setCellValueFactory(new PropertyValueFactory("idUnidadAsignada"));
    }
    
    private void cargarDatos() {
        HashMap<String, Object> respuesta = ColaboradorImp.obtenerColaboradores();
        boolean esError = (boolean) respuesta.get(Constantes.KEY_ERROR);

        if(!esError){
            List<Colaborador> colaboradoresAPI = (List<Colaborador>) respuesta.get("colaboradores");
            // Patrón de diseño observable
            colaboradores = FXCollections.observableArrayList();
            colaboradores.addAll(colaboradoresAPI);
            tvColaboradores.setItems(colaboradores);
        } else {
            Utilidades.mostrarAlertaSimple("Error al cargar", 
                    " " + respuesta.get("mensaje"), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void clicRegistrar(ActionEvent event) {
        irFormulario(null);
    }

    @FXML
    private void clicEditar(ActionEvent event) {
        Colaborador seleccionado = tvColaboradores.getSelectionModel().getSelectedItem();
        if(seleccionado != null){
            irFormulario(seleccionado);
        } else {
            Utilidades.mostrarAlertaSimple("Selección", "Selecciona un colaborador", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void clicEliminar(ActionEvent event) {
    }
    
    private void irFormulario(Colaborador colaborador) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLFormularioColaborador.fxml"));
            Parent root = loader.load();
            FXMLFormularioColaboradorController controller = loader.getController();
            controller.inicializarValores(colaborador); 
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            cargarDatos(); 
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
