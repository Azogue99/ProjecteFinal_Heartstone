package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import static functions.database.Users.createUser;
import static functions.database.Users.showAlert;

public class crear_user_controller implements Initializable {
    private Stage stage;
    private Scene scene;
    
    @FXML
    public void switchToLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    private PasswordField inputPassword1;

    @FXML
    private PasswordField inputPassword2;

    @FXML
    private TextField inputUsername;

    @FXML
    void createNewUser(ActionEvent event) throws IOException {
        String password1 = inputPassword1.getText();
        String password2 = inputPassword2.getText();
        String username = inputUsername.getText();
        if (password1.equals(password2) && createUser(username, password1, false)) {
            System.out.println("OK: Usuari creat");
            Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            System.out.println("WARNING: Els passwords no coincideixen");
            showAlert(Alert.AlertType.ERROR, "Error", "Els passwords no coincideixen","Els passwords han de ser iguals, revisal's siusplau.");
        }
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
