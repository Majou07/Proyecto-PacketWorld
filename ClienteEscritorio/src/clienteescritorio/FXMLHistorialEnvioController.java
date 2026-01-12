package clienteescritorio;

import clienteescritorio.dominio.EnvioImp;
import clienteescritorio.pojo.HistorialEstatus;
import clienteescritorio.utilidad.Constantes;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class FXMLHistorialEnvioController implements Initializable {
    @FXML private TableView<HistorialEstatus> tvHistorial;
    @FXML private TableColumn colEstatus, colFecha, colColaborador, colComentario;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colEstatus.setCellValueFactory(new PropertyValueFactory<>("estatus"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaCambio"));
        colColaborador.setCellValueFactory(new PropertyValueFactory<>("nombreColaborador"));
        colComentario.setCellValueFactory(new PropertyValueFactory<>("observaciones"));
    }

    public void cargarHistorial(int idEnvio) {
        HashMap<String, Object> respuesta = EnvioImp.obtenerHistorial(idEnvio);
        if (!(boolean) respuesta.get(Constantes.KEY_ERROR)) {
            List<HistorialEstatus> lista = (List<HistorialEstatus>) respuesta.get("historial");
            tvHistorial.setItems(FXCollections.observableArrayList(lista));
        }
    }
}