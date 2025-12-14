
package clienteescritorio;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ClienteEscritorio extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try{
            Parent vista = 
                FXMLLoader.load(getClass().getResource("FXMLInicioSesion.fxml"));
            Scene escenaLogin = new Scene(vista);
            primaryStage.setScene(escenaLogin);
            primaryStage.setTitle("Inicio de Sesión - Packet World");
            primaryStage.show();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

  
    public static void main(String[] args) {
        launch(args);
    }
    
}
