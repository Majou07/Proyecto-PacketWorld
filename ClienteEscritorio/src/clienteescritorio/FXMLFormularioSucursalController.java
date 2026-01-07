/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package clienteescritorio;

import clienteescritorio.dominio.CatalogoCPImp;
import clienteescritorio.dominio.SucursalImp;
import clienteescritorio.dto.Respuesta;
import clienteescritorio.pojo.CatalogoCP;
import clienteescritorio.pojo.Sucursal;
import clienteescritorio.utilidad.Utilidades;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Bruno
 */
public class FXMLFormularioSucursalController implements Initializable {

    @FXML
    private Label lbTitulo;
    @FXML
    private TextField tfCodigo;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfCalle;
    @FXML
    private TextField tfNumero;
    @FXML
    private ComboBox<String> cbEstatus;
    @FXML
    private ComboBox<String> cbColonia;
    @FXML
    private TextField tfCiudad;
    @FXML
    private TextField tfEstado;
    
    private Sucursal sucursalEdicion;
    @FXML
    private TextField tfCodigoPostal;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    cbEstatus.getItems().addAll("Activa", "Inactiva");

    // Listener para cambios en el código postal
    tfCodigoPostal.textProperty().addListener((obs, oldValue, newValue) -> {
        if (newValue != null && newValue.matches("\\d{5}")) {
            // ✅ CAMBIO: solo buscar cuando tenga exactamente 5 dígitos
            cargarDatosPorCodigoPostal(newValue);
        } else if (newValue == null || newValue.isEmpty()) {
            limpiarCamposDireccion();
        }
        // Si tiene menos de 5 dígitos, no hace nada y no muestra mensaje
    });

    // Listener para cambios en la colonia seleccionada
    cbColonia.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
        if (newVal != null) {
            List<CatalogoCP> resultados = CatalogoCPImp.buscarPorCodigoPostal(tfCodigoPostal.getText());
            for (CatalogoCP cp : resultados) {
                if (cp.getColonia().equals(newVal)) {
                    String ciudad = cp.getCiudad();
        if (ciudad == null || ciudad.trim().isEmpty()) {
        ciudad = cp.getMunicipio(); // respaldo con municipio
        }
        tfCiudad.setText(ciudad);
        tfEstado.setText(cp.getEstado());

                    break;
                }
            }
        }
    });
}

    
    private void cargarDatosPorCodigoPostal(String codigoPostal) {
    List<CatalogoCP> resultados =
            CatalogoCPImp.buscarPorCodigoPostal(codigoPostal);

    if (!resultados.isEmpty()) {

        cbColonia.getItems().clear();

        for (CatalogoCP cp : resultados) {
            cbColonia.getItems().add(cp.getColonia());
        }

        cbColonia.getSelectionModel().selectFirst();

        // Usamos el primer resultado para ciudad y estado
    String ciudad = resultados.get(0).getCiudad();
    if (ciudad == null || ciudad.trim().isEmpty()) {
    ciudad = resultados.get(0).getMunicipio(); // respaldo con municipio
    }
    tfCiudad.setText(ciudad);
    tfEstado.setText(resultados.get(0).getEstado());


    } else {

        limpiarCamposDireccion();

        Utilidades.mostrarAlertaSimple(
            "Código postal",
            "No se encontraron datos para el código postal ingresado",
            Alert.AlertType.INFORMATION
        );
    }
}
    private void limpiarCamposDireccion() {
    cbColonia.getItems().clear();
    tfCiudad.clear();
    tfEstado.clear();
}


    
    
     public void inicializarValores(Sucursal sucursal){
         this.sucursalEdicion = sucursal;
        if(sucursal != null){
            lbTitulo.setText("Actualizar Sucursal");
            // Setear valores
            tfCodigo.setText(sucursal.getCodigoSucursal());
            tfCodigoPostal.setText(sucursal.getCodigoPostal());
            tfNombre.setText(sucursal.getNombreCorto());
            tfCalle.setText(sucursal.getCalle());
            tfNumero.setText(sucursal.getNumero());
            tfCiudad.setText(sucursal.getCiudad());
            tfEstado.setText(sucursal.getEstado());
            
            tfCodigo.setDisable(true); 
            cbEstatus.setDisable(true);
            
            // CAMBIO: mostrar estatus actual en el ComboBox 
            cbEstatus.getSelectionModel().select(sucursal.getEstatus());
            
            //CAMBIO: seleccionar colonia y estatus en edición 
            cbColonia.getItems().add(sucursal.getColonia()); 
            cbColonia.getSelectionModel().select(sucursal.getColonia()); 
            cbEstatus.getSelectionModel().select(sucursal.getEstatus());      
     }else{
            lbTitulo.setText("Registrar Sucursal");
        }
       
}

    @FXML
    private void clicCancelar(ActionEvent event) {
        if(Utilidades.mostrarAlertaConfirmacion("Cancelar", "¿Deseas salir sin guardar los cambios?")){
            cerrarVentana();
        }
    }
    
    private void cerrarVentana(){
        ((Stage) tfNombre.getScene().getWindow()).close();
    }

    @FXML
    private void clicGuardar(ActionEvent event) {

    if (validarCampos()) {

        Sucursal sucursal = new Sucursal();
        sucursal.setCodigoSucursal(tfCodigo.getText());
        sucursal.setNombreCorto(tfNombre.getText());
        sucursal.setCalle(tfCalle.getText());
        sucursal.setNumero(tfNumero.getText());
        sucursal.setCodigoPostal(tfCodigoPostal.getText());
        sucursal.setColonia(cbColonia.getSelectionModel().getSelectedItem());
        String ciudad = tfCiudad.getText();
        if (ciudad == null || ciudad.trim().isEmpty()) {
        ciudad = tfEstado.getText(); // respaldo con estado o municipio
        }
sucursal.setCiudad(ciudad);

        sucursal.setEstado(tfEstado.getText());
        sucursal.setEstatus(cbEstatus.getSelectionModel().getSelectedItem());

        Respuesta resp;

        if (sucursalEdicion == null) {
            // Registro nuevo
            resp = SucursalImp.registrarSucursal(sucursal);
        } else {
            // Edición
            resp = SucursalImp.editarSucursal(sucursal);
        }

        Utilidades.mostrarAlertaSimple(
            "Resultado",
            resp.getMensaje(),
            resp.isError()
                ? Alert.AlertType.ERROR
                : Alert.AlertType.INFORMATION
        );

        if (!resp.isError()) {
            cerrarVentana();
        }
    }
}
    
   private boolean validarCampos() {
    if (
        tfCodigo.getText().trim().isEmpty() ||
        tfNombre.getText().trim().isEmpty() ||
        tfCalle.getText().trim().isEmpty() ||
        tfNumero.getText().trim().isEmpty() ||
        tfCodigoPostal.getText().trim().isEmpty() ||
        cbColonia.getSelectionModel().getSelectedItem() == null ||
        tfEstado.getText().trim().isEmpty() ||
        cbEstatus.getSelectionModel().getSelectedItem() == null
    ) {
        Utilidades.mostrarAlertaSimple(
            "Campos requeridos",
            "Debes llenar todos los campos antes de guardar",
            Alert.AlertType.WARNING
        );
        return false;
    }
    return true;
}



}