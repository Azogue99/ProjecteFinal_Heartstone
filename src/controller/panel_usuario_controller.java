package controller;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;

import classes.Carta_user_table;
import java.util.List;

import functions.database.CardsFilters;
import functions.database.returnImageFromDB;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;



public class panel_usuario_controller implements Initializable {
    
    private final int MAX_ITEMS = 100;

    private Stage stage;
    private Scene scene;
    
    @FXML
    private ChoiceBox<String> Tipo;

    @FXML
    private ChoiceBox<String> ataque;

    @FXML
    private ChoiceBox<String> coste;

    @FXML
    private ChoiceBox<String> rareza;

    @FXML
    private ChoiceBox<String> vida;

    @FXML
    private ChoiceBox<String> Clase;

    @FXML
    private TableColumn<Carta_user_table, ImageView> col1Column;
    
    @FXML
    private TableColumn<Carta_user_table, ImageView> col2Column;
        
    @FXML
    private TableColumn<Carta_user_table, ImageView> col3Column;

    @FXML
    private TableView<Carta_user_table> taula_user;

    public ObservableList<Carta_user_table> userData = FXCollections.observableArrayList();

    public void switchToLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setColumnPropierties();       
        
        Tipo.setOnAction(this::Tipo);
        vida.setOnAction(this::disableAtaqueVida);
        ataque.setOnAction(this::disableAtaqueVida);
        InformacionChoiceBox();
        
        getAllCards();
    }
    
    private void getAllCards() {
        CardsFilters cardsFilters = new CardsFilters();

        try {
            updateTableView(cardsFilters.getAllCardImages());
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }
           
    }
    
    private void setColumnPropierties() {
        col1Column.setCellValueFactory(new PropertyValueFactory<>("imageColumn1"));
        col2Column.setCellValueFactory(new PropertyValueFactory<>("imageColumn2"));
        col3Column.setCellValueFactory(new PropertyValueFactory<>("imageColumn3"));
    }

    public void InformacionChoiceBox() {

        String classes[] = {"Todos", "Caballero de la muerte", "Druida", "Cazador", "Mago", "Paladin", "Sacerdote", "Picaro", "ChamÃ¡n", "Brujo", "Guerrero", "Neutral", "Cazador de demonios"};
        Clase.getItems().addAll(classes);

        String[] ataques = {"Cualquiera", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11+"};
        ataque.getItems().addAll(ataques);

        String[] costes = {"Cualquiera", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11+"};
        coste.getItems().addAll(costes);

        String[] vidas = {"Cualquiera", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11+"};
        vida.getItems().addAll(vidas);

        String tipos[] = {"Cualquiera", "Heroe", "Minion", "Hechizo", "Arma", "Location"};
        Tipo.getItems().addAll(tipos);

        String Rareza[] = {"Cualquiera", "Gratis", "Comun", "Rara", "Epica", "Legendaria"};
        rareza.getItems().addAll(Rareza);
    }

    @FXML
    public void FiltrarCartas(ActionEvent event) throws IOException {
        taula_user.getItems().clear();
        taula_user.refresh();
        userData.clear();
        String sClase = Clase.getSelectionModel().getSelectedItem();
        int indexAtaque = ataque.getSelectionModel().getSelectedIndex();
        int indexCostes = coste.getSelectionModel().getSelectedIndex();
        int indexVida = vida.getSelectionModel().getSelectedIndex();
        String sTipo = Tipo.getSelectionModel().getSelectedItem();
        String sRareza = rareza.getSelectionModel().getSelectedItem();

        try {
            CardsFilters cardsFilters = new CardsFilters();
            List<String> filteredUrls = cardsFilters.filterCards(sClase,
                                                                 indexAtaque - 1,
                                                                 indexCostes - 1,
                                                                 indexVida - 1,
                                                                 sRareza,
                                                                 sTipo);
            
            System.out.println(filteredUrls.size());
            
            updateTableView(filteredUrls);
        } catch (SQLException e) {
            // Handle SQLException
            e.printStackTrace();
        }

//        taula_user.setItems(data);
    }


    public void Tipo(ActionEvent event) {

        if (Tipo.getSelectionModel().getSelectedIndex() == 3) {
            if (!ataque.getSelectionModel().isEmpty() || !vida.getSelectionModel().isEmpty()) {
                ataque.disableProperty().set(true);
                ataque.valueProperty().set("Cualquiera");
                vida.disableProperty().set(true);
                vida.valueProperty().set("Cualquiera");
            }
        } else {

            ataque.disableProperty().set(false);
            vida.disableProperty().set(false);
        }
    }

    public void disableAtaqueVida(ActionEvent event) {
        if (Tipo.getSelectionModel().getSelectedIndex() == 3) {
            ataque.disableProperty().set(true);
            vida.disableProperty().set(true);
        }
    }
    
    private void updateTableView(List<String> urls) {
        int counter = 1;
        if (urls.size() > MAX_ITEMS) {
            urls = urls.subList(0, MAX_ITEMS);
        }
        returnImageFromDB objectImageData = new returnImageFromDB();
        ArrayList<ImageView> items = new ArrayList();
        for (String url : urls) {
            try {
                Image imageData = objectImageData.getImageData(url);
                
                if (imageData != null) {
                    ImageView iv = new ImageView(objectImageData.getImageData(url));
                    iv.setFitHeight(imageData.getHeight()/2);
                    iv.setFitWidth(imageData.getWidth()/2);
                    items.add(iv);
                    if (counter == 3) {
                        counter = 0;
                        Carta_user_table cut = new Carta_user_table(items.get(0), items.get(1), items.get(2));
                        userData.add(cut);
                        items.clear();
                    }
                    
                    counter++;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        /*
            TODO: ADD LAST CARDS (las cartas que no llegan a completar una row no se estan añadiendo).
            Estan guardadas en la lista ITEMS y puede que haya 0, 1 o 2
        */
        
        taula_user.setItems(userData);
        taula_user.refresh();
        
    } 
}
