package controller;

import classes.Carta_admin_table;


import java.io.IOException;
import javafx.event.ActionEvent;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static functions.database.DatabaseConnection.getConnection;
import static functions.database.Users.showAlert;
import static functions.hsApi.ApiConnection.downloadCards;



public class panel_admin_controller implements Initializable {

    private Stage stage;
    private Scene scene;

    @FXML
    private TextField inputPublicKey;

    @FXML
    private TextField inputPrivateKey;

    @FXML
    public ProgressBar progressBar;

    @FXML
    private TableColumn<Carta_admin_table, String> nomColumn;

    @FXML
    private TableColumn<Carta_admin_table, Integer> classeColumn;

    @FXML
    private TableColumn<Carta_admin_table, Integer> manaColumn;

    @FXML
    private TableColumn<Carta_admin_table, Integer> atacColumn;

    @FXML
    private TableColumn<Carta_admin_table, Integer> vidaColumn;

    @FXML
    private TableColumn<Carta_admin_table, Integer> raresaColumn;

    @FXML
    private TableColumn<Carta_admin_table, Integer> tipusColumn;

    @FXML
    private TableView<Carta_admin_table> taula_admin;

    public ObservableList<Carta_admin_table> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeTableColumns();
        loadCardsFromDatabase();
    }

    private void initializeTableColumns() {
        // Initialize table columns
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        classeColumn.setCellValueFactory(new PropertyValueFactory<>("classe"));
        manaColumn.setCellValueFactory(new PropertyValueFactory<>("mana"));
        atacColumn.setCellValueFactory(new PropertyValueFactory<>("atac"));
        vidaColumn.setCellValueFactory(new PropertyValueFactory<>("vida"));
        raresaColumn.setCellValueFactory(new PropertyValueFactory<>("raresa"));
        tipusColumn.setCellValueFactory(new PropertyValueFactory<>("tipus"));
    }

    @FXML
    void updateCards(ActionEvent event) {
        Task<Void> downloadTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> updateProgress(0.10, 1)); // Update progress to 10% on FX thread
                String publicKey = inputPublicKey.getText();
                String privateKey = inputPrivateKey.getText();
                downloadCards(publicKey, privateKey, progressBar);
                return null;
            }
        };

        progressBar.progressProperty().bind(downloadTask.progressProperty());
        Thread downloadThread = new Thread(downloadTask);
        downloadThread.setDaemon(true); // Set as daemon thread to stop when the application exits
        downloadThread.start();

        downloadTask.setOnSucceeded(e -> {
            Platform.runLater(() -> {
                loadCardsFromDatabase();
                showAlert(Alert.AlertType.INFORMATION, "Correcte!", "S'han descarregat les cartes!",
                        "El process de descarrega de les cartes ha finalitzat amb exit!");
                progressBar.progressProperty().unbind(); // Unbind progress property
                progressBar.setProgress(0); // Reset progress bar to 0
            });
        });

        downloadTask.setOnFailed(e -> {
            Platform.runLater(() -> {
                showAlert(Alert.AlertType.ERROR, "Error", "No s'ha pogut descarregar les cartes",
                        "Si us plau, revisa que les claus son correctes i tens acces a internet.");
                progressBar.progressProperty().unbind(); // Unbind progress property
                progressBar.setProgress(0); // Reset progress bar to 0 in case of error
            });
        });
    }

    public void switchToLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void loadCardsFromDatabase() {
        String query = "SELECT name, class, mana_cost, attack, health, rarity, type FROM cartes";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            data.clear(); // Clear the existing data

            while (rs.next()) {
                String nom = rs.getString("name");
                int classe = rs.getInt("class");
                int mana = rs.getInt("mana_cost");
                int atac = rs.getInt("attack");
                int vida = rs.getInt("health");
                int raresa = rs.getInt("rarity");
                int tipus = rs.getInt("type");

                Carta_admin_table carta = new Carta_admin_table(nom, classe, mana, atac, vida, raresa, tipus);
                data.add(carta);
            }
            taula_admin.setItems(data); // Set the items to the TableView
            taula_admin.refresh(); // Refresh the TableView
        } catch (SQLException ex) {
            System.out.println("ERROR: no s'ha pogut carregar les cartes desde la bbdd: ");
            ex.printStackTrace();
        }
    }
}
