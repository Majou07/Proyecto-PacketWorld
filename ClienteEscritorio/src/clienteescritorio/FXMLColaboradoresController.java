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
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    
   // private ScheduledExecutorService scheduler;
    private ObservableList<Colaborador> colaboradores = FXCollections.observableArrayList();
    @FXML
    private Button btBuscar;
   

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    configurarTabla();

    cbFiltro.setItems(FXCollections.observableArrayList(
            "Nombre", "Número de Personal", "Rol"
    ));
    cbFiltro.getSelectionModel().selectFirst();

    cargarDatos(); // async
   
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
        
        tvColaboradores.setPlaceholder(
        new Label("Cargando colaboradores...")
    );
    }
    
    
    
    private void cargarDatos() {

    Task<List<Colaborador>> task = new Task<List<Colaborador>>() {
        @Override
        protected List<Colaborador> call() {

            HashMap<String, Object> respuesta =
                    ColaboradorImp.obtenerColaboradores();

            boolean error = (boolean) respuesta.get(Constantes.KEY_ERROR);

            if (error) return null;

            return (List<Colaborador>) respuesta.get("colaboradores");
        }
    };

    task.setOnSucceeded(e -> {

        List<Colaborador> lista = task.getValue();

        colaboradores.clear();

        if (lista != null) {
            colaboradores.addAll(lista);
        }

        tvColaboradores.setItems(colaboradores);
    });

    task.setOnFailed(e -> {
        Utilidades.mostrarAlertaSimple(
                "Error",
                "No se pudo cargar la información del servidor",
                Alert.AlertType.ERROR
        );
    });

    new Thread(task).start();
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

    Colaborador seleccionado =
            tvColaboradores.getSelectionModel().getSelectedItem();

    if (seleccionado == null) {
        Utilidades.mostrarAlertaSimple(
                "Selección",
                "Selecciona un colaborador",
                Alert.AlertType.WARNING
        );
        return;
    }

    boolean confirmar = Utilidades.mostrarAlertaConfirmacion(
            "Eliminar colaborador",
            "¿Estás seguro de eliminar a " + seleccionado.getNombre() + "?"
    );

    if (!confirmar) return;

    Task<Respuesta> task = new Task<Respuesta>() {
        @Override
        protected Respuesta call() {
            return ColaboradorImp.eliminar(seleccionado.getIdColaborador());
        }
    };

    task.setOnSucceeded(e -> {

        Respuesta respuesta = task.getValue();

        if (!respuesta.isError()) {

            Utilidades.mostrarAlertaSimple(
                    "Éxito",
                    respuesta.getMensaje(),
                    Alert.AlertType.INFORMATION
            );

            cargarDatos(); // refresca async también
        } else {
            Utilidades.mostrarAlertaSimple(
                    "Error",
                    respuesta.getMensaje(),
                    Alert.AlertType.ERROR
            );
        }
    });

    task.setOnFailed(e -> {
        Utilidades.mostrarAlertaSimple(
                "Error",
                "No se pudo eliminar el colaborador",
                Alert.AlertType.ERROR
        );
    });

    new Thread(task).start();
}
    
    private void irFormulario(Colaborador colaborador) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLFormularioColaborador.fxml"));
            Parent root = loader.load();
            FXMLFormularioColaboradorController controller = loader.getController();
            controller.inicializarValores(colaborador); 
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            
            stage.setOnCloseRequest(e -> {
            });
            
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

    if (filtro.isEmpty()) {
        cargarDatos();
        return;
    }

    Task<List<Colaborador>> task = new Task<List<Colaborador>>() {
        @Override
        protected List<Colaborador> call() {

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
                    if (idRol > 0) {
                        respuesta = ColaboradorImp.buscarPorRol(idRol);
                    }
                    break;
            }

            if (respuesta == null || (boolean) respuesta.get(Constantes.KEY_ERROR)) {
                return null;
            }

            return (List<Colaborador>) respuesta.get("colaboradores");
        }
    };

    task.setOnSucceeded(e -> {

        List<Colaborador> resultados = task.getValue();

        if (resultados == null || resultados.isEmpty()) {
            Utilidades.mostrarAlertaSimple(
                    "Sin resultados",
                    "No se encontraron coincidencias",
                    Alert.AlertType.INFORMATION
            );
        } else {
            colaboradores = FXCollections.observableArrayList(resultados);
            tvColaboradores.setItems(colaboradores);
        }
    });

    task.setOnFailed(e -> {
        Utilidades.mostrarAlertaSimple(
                "Error",
                "Error al realizar la búsqueda",
                Alert.AlertType.ERROR
        );
    });

    new Thread(task).start();
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

   @FXML
private void clicAsignarUnidad(ActionEvent event) {
    Colaborador seleccionado = tvColaboradores.getSelectionModel().getSelectedItem();
    
    if (seleccionado == null) {
        Utilidades.mostrarAlertaSimple("Selección requerida", 
            "Debes seleccionar un colaborador para asignarle una unidad.", 
            Alert.AlertType.WARNING);
        return;
    }

    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLAsignarUnidad.fxml"));
        Parent root = loader.load();

        // Pasar el colaborador seleccionado al controlador de la pantalla de asignación
        FXMLAsignarUnidadController controller = loader.getController();
        controller.inicializarConductor(seleccionado);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Asignar Unidad");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        cargarDatos(); // refrescar tabla por si se asignó unidad
    } catch (IOException e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("No se pudo abrir la pantalla");
        alert.setContentText("Verifica que el archivo FXMLAsignarUnidad.fxml esté bien formado y en la ruta correcta.");
        alert.showAndWait();
    }
}

    @FXML
    private void clicDesasignarUnidad(ActionEvent event) {
        Colaborador seleccionado = tvColaboradores.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            Utilidades.mostrarAlertaSimple("Selección requerida", 
                "Debes seleccionar un colaborador", Alert.AlertType.WARNING);
            return;
        }

        if (seleccionado.getIdUnidadAsignada() == null) {
            Utilidades.mostrarAlertaSimple("Sin vehículo", 
                "Este conductor no tiene ningún vehículo asignado", Alert.AlertType.WARNING);
            return;
        }

        boolean confirmar = Utilidades.mostrarConfirmacion("Desasignar", 
            "¿Estás seguro de desasignar el vehículo de " + seleccionado.getNombre() + "?");

        if (confirmar) {
            Respuesta resp = ColaboradorImp.desasignarVehiculo(seleccionado.getIdColaborador());
            if (!resp.isError()) {
                Utilidades.mostrarAlertaSimple("Éxito", resp.getMensaje(), Alert.AlertType.INFORMATION);
                cargarDatos(); // refrescar tabla
            } else {
                Utilidades.mostrarAlertaSimple("Error", resp.getMensaje(), Alert.AlertType.ERROR);
            }
        }
    }

}
