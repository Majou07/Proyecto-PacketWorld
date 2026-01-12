package clienteescritorio;

import clienteescritorio.dominio.EnvioImp;
import clienteescritorio.dominio.PaqueteImp;
import clienteescritorio.dto.Respuesta;
import clienteescritorio.pojo.Envio;
import clienteescritorio.pojo.Paquete;
import clienteescritorio.utilidad.Constantes;
import clienteescritorio.utilidad.Utilidades;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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

public class FXMLEnviosController implements Initializable {

    @FXML private TextField tfBusquedaEnvio;
    @FXML private TableView<Envio> tvEnvios;
    @FXML private TableColumn<Envio, String> colGuia;
    @FXML private TableColumn<Envio, String> colCliente;
    @FXML private TableColumn<Envio, String> colOrigen;
    @FXML private TableColumn<Envio, String> colDestino;
    @FXML private TableColumn<Envio, String> colConductor;
    @FXML private TableColumn<Envio, String> colEstado;
    
    @FXML private TableView<Paquete> tvPaquetes;
    @FXML private TableColumn<Paquete, String> colDescripcion;
    @FXML private TableColumn<Paquete, Float> colPeso;
    @FXML private TableColumn<Paquete, String> colDimensiones;
    @FXML private TableColumn<Paquete, Integer> colEnvioPertence;

    private ObservableList<Envio> envios;
    private ObservableList<Paquete> paquetes;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarEnvios();
        
        tvEnvios.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cargarPaquetesPorEnvio(newVal.getIdEnvio());
            }
        });

        tvEnvios.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2 && tvEnvios.getSelectionModel().getSelectedItem() != null){
                abrirModalEstatus(tvEnvios.getSelectionModel().getSelectedItem());
            }
        });
    }

    private void configurarTabla() {
        // Envíos
        colGuia.setCellValueFactory(new PropertyValueFactory<>("numeroGuia"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("nombreCliente"));
        colOrigen.setCellValueFactory(new PropertyValueFactory<>("sucursalOrigen"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estatusEnvio"));
        
        // Paquetes
        colDestino.setCellValueFactory(cellData -> {
            Envio e = cellData.getValue();
            String estado = (e.getDestinoEstado() != null) ? e.getDestinoEstado() : "No disponible";
            return new SimpleStringProperty(estado);
        });
        
        colConductor.setCellValueFactory(new PropertyValueFactory<>("nombreConductor"));
        
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));
        
        colDimensiones.setCellValueFactory(cellData -> {
            Paquete p = cellData.getValue();
            if (p != null) {
                String dimensiones = String.format("%.0f cm x %.0f cm x %.0f cm", 
                                     p.getAlto(), p.getAncho(), p.getProfundidad());
                return new SimpleStringProperty(dimensiones);
            }
            return new SimpleStringProperty("");
        });
        
        colEnvioPertence.setCellValueFactory(new PropertyValueFactory<>("idEnvio"));
    }

    private void cargarEnvios() {
        HashMap<String, Object> respuesta = EnvioImp.obtenerEnvios();
        if(!(boolean)respuesta.get("error")){
            envios = FXCollections.observableArrayList((List<Envio>)respuesta.get("envios"));
            tvEnvios.setItems(envios);
            configurarBusqueda();
        } else {
            Utilidades.mostrarAlertaSimple("Error", (String)respuesta.get("mensaje"), Alert.AlertType.ERROR);
        }
    }
    
    
    private void configurarBusqueda() {
        if (envios != null) {
            FilteredList<Envio> filtroEnvio = new FilteredList<>(envios, p -> true);

            tfBusquedaEnvio.textProperty().addListener((obs, oldV, newV) -> {
                filtroEnvio.setPredicate(envio -> {
                    if (newV == null || newV.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newV.toLowerCase();
                    return envio.getNumeroGuia() != null && 
                           envio.getNumeroGuia().toLowerCase().contains(lowerCaseFilter);
                });
            });

            tvEnvios.setItems(filtroEnvio);
        }
    }



    @FXML 
    private void btnHistorial(ActionEvent event) {
        Envio seleccionado = tvEnvios.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLHistorialEnvio.fxml"));
                Parent root = loader.load();

                FXMLHistorialEnvioController controller = loader.getController();
                controller.cargarHistorial(seleccionado.getIdEnvio());

                Stage stage = new Stage();
                stage.setTitle("Historial - Guía: " + seleccionado.getNumeroGuia());
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            Utilidades.mostrarAlertaSimple("Selección", "Selecciona un envío para ver su historial.", 
                    Alert.AlertType.WARNING);
        }
    }

    @FXML 
    private void btnRegistrarEnvio(ActionEvent event) {
        irFormularioEnvio(null);
    }

    @FXML 
    private void btnActualizarEnvio(ActionEvent event) {
        Envio seleccionado = tvEnvios.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            irFormularioEnvio(seleccionado);
        }
    }

    @FXML 
    private void btnEliminarEnvio(ActionEvent event) {
        Envio seleccionado = tvEnvios.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            boolean confirmar = Utilidades.mostrarConfirmacion("Eliminar", "¿Deseas eliminar permanentemente el envío " + seleccionado.getNumeroGuia() + "?");
            if (confirmar) {
                Respuesta resp = EnvioImp.eliminarEnvio(seleccionado.getIdEnvio());
                if(!resp.isError()){
                    Utilidades.mostrarAlertaSimple("Éxito", "Envío eliminado correctamente", Alert.AlertType.INFORMATION);
                    cargarEnvios(); // Esto quita el registro de la vista
                } else {
                    Utilidades.mostrarAlertaSimple("Error", resp.getMensaje(), Alert.AlertType.ERROR);
                }
            }
        } else {
            Utilidades.mostrarAlertaSimple("Selección", "Primero selecciona un envío.", Alert.AlertType.WARNING);
        }
    }

    @FXML 
    private void btnAsignarConductor(ActionEvent event) {
        Envio seleccionado = tvEnvios.getSelectionModel().getSelectedItem();

        if (seleccionado != null) {
            HashMap<String, Object> respuestaConductor = clienteescritorio.dominio.ColaboradorImp.buscarPorRol(2);

            if (!(boolean) respuestaConductor.get(clienteescritorio.utilidad.Constantes.KEY_ERROR)) {
                List<clienteescritorio.pojo.Colaborador> conductores = (List<clienteescritorio.pojo.Colaborador>) respuestaConductor.get("colaboradores");

                if (conductores != null && !conductores.isEmpty()) {
                    ChoiceDialog<clienteescritorio.pojo.Colaborador> dialog = new ChoiceDialog<>(conductores.get(0), conductores);
                    dialog.setTitle("Asignación de Conductor");
                    dialog.setHeaderText("Asignar conductor al envío: " + seleccionado.getNumeroGuia());
                    dialog.setContentText("Seleccione un conductor:");

                    java.util.Optional<clienteescritorio.pojo.Colaborador> resultado = dialog.showAndWait();

                    if (resultado.isPresent()) {
                        clienteescritorio.pojo.Colaborador conductorElegido = resultado.get();

                        clienteescritorio.dto.Respuesta resp = EnvioImp.asignarConductor(
                                seleccionado.getIdEnvio(), 
                                conductorElegido.getIdColaborador()
                        );

                        if (!resp.isError()) {
                            Utilidades.mostrarAlertaSimple("Éxito", "Conductor asignado correctamente", Alert.AlertType.INFORMATION);
                            cargarEnvios(); 
                        } else {
                            Utilidades.mostrarAlertaSimple("Error", resp.getMensaje(), Alert.AlertType.ERROR);
                        }
                    }
                } else {
                    Utilidades.mostrarAlertaSimple("Sin personal", "No se encontraron colaboradores con el rol de Conductor.", Alert.AlertType.WARNING);
                }
            } else {
                Utilidades.mostrarAlertaSimple("Error", "No se pudo obtener la lista de conductores.", Alert.AlertType.ERROR);
            }
        } else {
            Utilidades.mostrarAlertaSimple("Selección requerida", "Por favor, selecciona un envío de la tabla primero.", Alert.AlertType.WARNING);
        }
    }
    
    
    private void irFormularioEnvio(Envio envio) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLFormularioEnvio.fxml"));
            Parent root = loader.load();

            if (envio != null) {
                FXMLFormularioEnvioController controller = loader.getController();
                controller.prepararFormulario(envio); 
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(envio == null ? "Registrar Envío" : "Actualizar Envío");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            cargarEnvios(); 
        } catch (IOException ex) {
            ex.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", "No se pudo cargar el formulario", Alert.AlertType.ERROR);
        }
    }

    
    
    // --- PAQUETES ---
    
    
    @FXML 
    private void btnAgregarPaquete(ActionEvent event) {
        Envio envioSeleccionado = tvEnvios.getSelectionModel().getSelectedItem();
        if (envioSeleccionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLFormularioPaquete.fxml"));
                Parent root = loader.load();
                FXMLFormularioPaqueteController ctrl = loader.getController();
                ctrl.setEnvio(envioSeleccionado.getIdEnvio());
                
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
                
                cargarPaquetesPorEnvio(envioSeleccionado.getIdEnvio());
                
            } catch(Exception e) { e.printStackTrace(); }
        } else {
            Utilidades.mostrarAlertaSimple("Selección", "Selecciona un envío", Alert.AlertType.WARNING);
        }
    }

    @FXML 
    private void btnEditarPaquete(ActionEvent event) {
        Paquete seleccionado = tvPaquetes.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLFormularioPaquete.fxml"));
                Parent root = loader.load();
                FXMLFormularioPaqueteController ctrl = loader.getController();
                ctrl.prepararFormulario(seleccionado); 

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.showAndWait();
                cargarPaquetesPorEnvio(seleccionado.getIdEnvio());
            } catch(IOException e) { e.printStackTrace(); }
        }
    }

    @FXML 
    private void btnEliminarPaquete(ActionEvent event) {
        Paquete selecc = tvPaquetes.getSelectionModel().getSelectedItem();
        if (selecc != null) {
            if (Utilidades.mostrarConfirmacion("Eliminar", "¿Deseas quitar este paquete?")) {
                PaqueteImp.eliminarPaquete(selecc.getIdPaquete());
                cargarPaquetesPorEnvio(selecc.getIdEnvio()); 
            }
        } else {
            Utilidades.mostrarAlertaSimple("Selección", "Elige un paquete", Alert.AlertType.WARNING);
        }
    }
 
    
    
    private void cargarPaquetesPorEnvio(int idEnvio) {
        HashMap<String, Object> respuesta = PaqueteImp.obtenerPaquetesPorEnvio(idEnvio);

        if (!(boolean) respuesta.get(Constantes.KEY_ERROR)) {
            List<Paquete> listaApi = (List<Paquete>) respuesta.get("paquetes");

            paquetes = FXCollections.observableArrayList(listaApi);
            tvPaquetes.setItems(paquetes);
            tvPaquetes.refresh();
        } else {
            tvPaquetes.getItems().clear();
        }
    }
    
    

    private void abrirModalEstatus(Envio envio) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLModalEstatus.fxml"));
            Parent root = loader.load();
            FXMLModalEstatusController controller = loader.getController();
            controller.inicializarEnvio(envio);
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            cargarEnvios(); 
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}