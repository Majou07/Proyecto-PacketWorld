package clienteescritorio;

import clienteescritorio.dominio.ColaboradorImp;
import clienteescritorio.dto.Respuesta;
import clienteescritorio.pojo.Colaborador;
import clienteescritorio.pojo.Rol;
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
    @FXML private ComboBox<String> cbFiltro;
    
    private ObservableList<Colaborador> colaboradores;
    @FXML
    private Button btBuscar;
   

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarDatos();
        System.out.println("Colaboradores: " + colaboradores.size());
        
        cbFiltro.setItems(FXCollections.observableArrayList( 
                "Nombre", "Número de Personal", "Rol" )); 
        cbFiltro.getSelectionModel().selectFirst(); // opción por defecto
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
    Colaborador seleccionado = tvColaboradores.getSelectionModel().getSelectedItem();
    if(seleccionado != null){
        // Confirmación antes de eliminar
        boolean confirmar = Utilidades.mostrarAlertaConfirmacion(
            "Eliminar colaborador",
            "¿Estás seguro de eliminar al colaborador " + seleccionado.getNombre() + "?"
        );

        if(confirmar){
            Respuesta respuesta = ColaboradorImp.eliminar(seleccionado.getIdColaborador());
            if(!respuesta.isError()){
                Utilidades.mostrarAlertaSimple("Éxito", respuesta.getMensaje(), Alert.AlertType.INFORMATION);
                cargarDatos(); //  refresca la tabla después de eliminar
            } else {
                Utilidades.mostrarAlertaSimple("Error", respuesta.getMensaje(), Alert.AlertType.ERROR);
            }
        }
    } else {
        Utilidades.mostrarAlertaSimple("Selección", "Selecciona un colaborador", Alert.AlertType.WARNING);
    }
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

  @FXML
private void clicBuscar(ActionEvent event) {
    String filtro = tfBusqueda.getText().trim();
    String criterio = cbFiltro.getValue();

    // Si el campo está vacío, recargar toda la tabla
    if(filtro.isEmpty()){
        cargarDatos(); 
        return;
    }

    HashMap<String, Object> respuesta = null;

    switch (criterio) {
        case "Nombre":
            respuesta = ColaboradorImp.buscarPorNombre(filtro);
            break;

        case "Número de Personal":
            respuesta = ColaboradorImp.buscarPorNumeroPersonal(filtro);
            break;

        case "Rol":
            int idRol = obtenerIdRolPorNombre(filtro);
            if(idRol > 0){
                respuesta = ColaboradorImp.buscarPorRol(idRol);
            } else {
                Utilidades.mostrarAlertaSimple("Rol inválido", 
                        "No se reconoce el rol ingresado", 
                        Alert.AlertType.WARNING);
                return;
            }
            break;
    }

    if(respuesta != null && !(boolean) respuesta.get(Constantes.KEY_ERROR)){
        List<Colaborador> resultados = (List<Colaborador>) respuesta.get("colaboradores");

        if(resultados == null || resultados.isEmpty()){
            // Mensaje específico según el criterio
            switch (criterio) {
                case "Nombre":
                    Utilidades.mostrarAlertaSimple("Sin resultados", 
                            "No se encontraron colaboradores con ese nombre o apellidos", 
                            Alert.AlertType.INFORMATION);
                    break;
                case "Número de Personal":
                    Utilidades.mostrarAlertaSimple("Sin resultados", 
                            "No existe ningún colaborador con ese número de personal", 
                            Alert.AlertType.INFORMATION);
                    break;
                case "Rol":
                    Utilidades.mostrarAlertaSimple("Sin resultados", 
                            "No se encontraron colaboradores con ese rol", 
                            Alert.AlertType.INFORMATION);
                    break;
            }
            //  Ya no limpiamos la tabla, se queda como estaba
        } else {
            colaboradores = FXCollections.observableArrayList(resultados);
            tvColaboradores.setItems(colaboradores);
        }
    } else {
        Utilidades.mostrarAlertaSimple("Error", 
                (respuesta != null ? (String) respuesta.get(Constantes.KEY_MENSAJE) : "Error desconocido"), 
                Alert.AlertType.ERROR);
    }
}


    private int obtenerIdRolPorNombre(String nombreRol){
    HashMap<String, Object> respuesta = ColaboradorImp.obtenerRoles();
    if(!(boolean) respuesta.get(Constantes.KEY_ERROR)){
        List<Rol> roles = (List<Rol>) respuesta.get("roles");
        for(Rol r : roles){
            if(r.getNombreRol().equalsIgnoreCase(nombreRol)){
                return r.getIdRol();
            }
        }
    }
    return -1;
}

}
