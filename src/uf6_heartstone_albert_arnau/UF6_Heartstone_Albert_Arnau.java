package uf6_heartstone_albert_arnau;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.InputStream;
import javafx.scene.image.Image;

import static functions.database.Users.existsAdmin;
import static functions.database.DatabaseConnection.databaseConnection;
import static functions.database.DatabaseConnection.closeConnection;

public class UF6_Heartstone_Albert_Arnau extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
                
        databaseConnection();
        
        String goTo = existsAdmin() ? "/view/login.fxml" : "/view/crear_admin.fxml";
        Parent root = FXMLLoader.load(getClass().getResource(goTo));

        InputStream iconStream = UF6_Heartstone_Albert_Arnau.class.getResourceAsStream("/images/icon.png");
        if (iconStream != null) {
            stage.getIcons().add(new Image(iconStream));
        } else {
            System.out.println("WARNING: No s'ha trobat la icona per la finestra.");
        }
        stage.setTitle("Hearstone");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        closeConnection();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
