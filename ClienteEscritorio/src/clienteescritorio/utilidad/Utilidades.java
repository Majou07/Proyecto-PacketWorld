package clienteescritorio.utilidad;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Utilidades {
    
    public static String streamToString(InputStream is) {
    if (is == null) return ""; // ✅ evita NullPointerException
    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
        return br.lines().collect(Collectors.joining("\n"));
    } catch (Exception e) {
        return "";
    }
}


    public static void mostrarAlertaSimple(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
    
    public static boolean mostrarAlertaConfirmacion(String titulo, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        Optional<ButtonType> resultado = alerta.showAndWait();
        return resultado.get() == ButtonType.OK;
    }
}