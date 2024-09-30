package controller;

import static functions.database.Users.checkCredentials;
import static functions.database.Users.isAdmin;
import static functions.database.Users.showAlert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;


public class login_controller implements Initializable {
    private Stage stage;
    private Scene scene;
    
    @FXML
    private PasswordField inputPassword;

    @FXML
    private TextField inputUsername;
    
    @FXML
    public void switchToUserPanel() throws IOException {
        String password = inputPassword.getText();
        String username = inputUsername.getText();
        
        if (checkCredentials(username, password)) {
            String goTo = isAdmin(username) ? "/view/panel_admin.fxml" : "/view/panel_usuario.fxml";
            Parent root = FXMLLoader.load(getClass().getResource(goTo));
            
            stage = (Stage) inputPassword.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "El usuari o la contrassenya no son correctes","Siusplau, revisa que l'usuari i la contrassenya siguin correctes, si no tens usuari pots crear un.");
        }
    }
    
    @FXML
    public void switchToNewUser(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/crear_user.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inputPassword.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    switchToUserPanel();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        inputUsername.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    switchToUserPanel();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
