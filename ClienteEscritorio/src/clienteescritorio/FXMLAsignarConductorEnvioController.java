package clienteescritorio;

import clienteescritorio.dominio.ColaboradorImp;
import clienteescritorio.dominio.EnvioImp;
import clienteescritorio.dto.Respuesta;
import clienteescritorio.pojo.Colaborador;
import clienteescritorio.pojo.Envio;
import clienteescritorio.utilidad.Utilidades;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class FXMLAsignarConductorEnvioController implements Initializable {
    @FXML private Label lbGuia;
    @FXML private ComboBox<Colaborador> cbConductores;
    private Envio envio;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarConductores();
    }

    public void inicializarEnvio(Envio envio) {
        this.envio = envio;
        this.lbGuia.setText("No. Guía: " + envio.getNumeroGuia());
    }

    private void cargarConductores() {
        HashMap<String, Object> respuesta = ColaboradorImp.buscarPorRol(2); 
        if (!(boolean) respuesta.get("error")) {
            List<Colaborador> lista = (List<Colaborador>) respuesta.get("colaboradores");
            cbConductores.setItems(FXCollections.observableArrayList(lista));
        }
    }

    @FXML
    private void clicAsignar(ActionEvent event) {
        Colaborador seleccionado = cbConductores.getValue();
        if (seleccionado != null) {
            // Deben implementar este método en su EnvioImp de escritorio
            Respuesta resp = EnvioImp.asignarConductor(envio.getIdEnvio(), seleccionado.getIdColaborador());
            if (!resp.isError()) {
                Utilidades.mostrarAlertaSimple("Éxito", "Conductor asignado al envío", Alert.AlertType.INFORMATION);
                cerrarVentana();
            }
        }
    }

    @FXML private void clicCancelar(ActionEvent event) { 
        cerrarVentana(); 
    }
    
    private void cerrarVentana() { 
        ((Stage) lbGuia.getScene().getWindow()).close(); 
    }
}
