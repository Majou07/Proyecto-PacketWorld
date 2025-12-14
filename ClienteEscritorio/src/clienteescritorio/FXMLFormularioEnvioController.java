package clienteescritorio;

import clienteescritorio.dominio.ClienteImp;
import clienteescritorio.dominio.SucursalImp;
import clienteescritorio.pojo.Cliente;
import clienteescritorio.pojo.Sucursal;
import clienteescritorio.pojo.Envio;
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

public class FXMLFormularioEnvioController implements Initializable {
    @FXML private ComboBox<Cliente> cbCliente;
    @FXML private ComboBox<Sucursal> cbSucursal;
    @FXML private TextField tfNombreDest;
    @FXML private TextField tfCalle;
    @FXML private TextField tfCiudad;
    @FXML private TextField tfCP;
    @FXML private TextField tfEstado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarCatalogos();
    }    

    private void cargarCatalogos(){
        HashMap<String, Object> respClientes = ClienteImp.obtenerClientes();
        cbCliente.setItems(FXCollections.observableArrayList((List<Cliente>)respClientes.get("clientes")));
        
        HashMap<String, Object> respSuc = SucursalImp.obtenerSucursales();
        cbSucursal.setItems(FXCollections.observableArrayList((List<Sucursal>)respSuc.get("sucursales")));
    }

    @FXML
    private void clicGuardar(ActionEvent event) {
        // Recopilar datos en objeto Envio y llamar a EnvioImp.registrar
        ((Stage) tfNombreDest.getScene().getWindow()).close();
    }
}