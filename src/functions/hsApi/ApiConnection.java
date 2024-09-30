package functions.hsApi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.Scanner;

import java.net.HttpURLConnection;
import java.net.URL;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import static functions.database.DatabaseConnection.checkAndReconnect;
import static functions.database.DatabaseConnection.getConnection;


public class ApiConnection {
    static final String rutaBat = "resources/generarCode.bat";
    static final String rutaJson = "resources/token.json";
    static File batName = new File(rutaBat);

    public static void downloadCards(String publicKey, String privateKey, ProgressBar progressBar) {
        try {
            // Create the bat file to obtain the JSON file
            crearBat(publicKey, privateKey);

            // Obtain the token
            String sToken = getTokenFromJson();

            // Obtain all the cards (from different pages)
            JSONArray cardsArray = fetchAllCardsFromApi(sToken, progressBar);

            // Parse JSON array to ArrayList of cards
            ArrayList<Card> cards = parseCards(cardsArray);

            // Insert the cards directly into the database
            insertCardsIntoDatabase(cards);

        } catch (Exception e) {
            System.out.println("ERROR: No s'han pogut descarregar les cartes.");
        }
        
    }

    private static void crearBat(String publicKey, String privateKey) throws InterruptedException {
        try {
            // crea el fw, bw i pw
            FileWriter fw = new FileWriter(batName, false);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            // escriu
            String commandToken = String.format("curl -u %s:%s -d grant_type=client_credentials https://oauth.battle.net/token > token.json", publicKey, privateKey);
            System.out.println("INFO: " + commandToken);
            pw.println(commandToken);

            // guarda
            pw.flush();
            pw.close();
            bw.close();
            fw.close();
        } catch (IOException e) {
            System.out.println("ERROR: no s'ha pogut crear el fitxer .bat correctament");
        }
    }

    private static String getTokenFromJson() throws IOException, InterruptedException {
        File workingDirectory = new File("resources");
        ProcessBuilder jsonTokenGetter = new ProcessBuilder("cmd.exe", "/c", "generarCode.bat");
        jsonTokenGetter.directory(workingDirectory); // Set the working directory
        Process exe = jsonTokenGetter.start();

        int exitCode = exe.waitFor();
        if (exitCode != 0) {
            System.out.println("ERROR: The .bat file could not be executed correctly. Exit code: " + exitCode);
            throw new InterruptedException("ERROR: Mentres s'executava el fitxer .bat");
        } else {
            System.out.println("OK: El fitxer .bat s'ha executat correctament");
        }

        // Read the token from token.json
        File json = new File(rutaJson);
        try (Scanner sc = new Scanner(json)) {
            String token = sc.nextLine();
            JSONObject tokenJsonObject = new JSONObject(token);
            return tokenJsonObject.getString("access_token");
        }
    }

    public static JSONArray fetchAllCardsFromApi(String token, ProgressBar progressBar) throws IOException, URISyntaxException {
        JSONArray allCardsArray = new JSONArray();

        // Create a task for downloading cards asynchronously
        Task<Void> downloadTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                int pageCount = 0;
                int currentPage = 1;

                // Use a single-element array for the progress variable
                final double[] progress = {0.0};

                do {
                    String apiUrl = "https://us.api.blizzard.com/hearthstone/cards?locale=es_ES&page=" + currentPage + "&pageSize=500&access_token=" + token;
                    System.out.println(apiUrl);
                    JSONObject jsonObject = fetchJsonFromApi(apiUrl);
                    JSONArray cardsArray = jsonObject.getJSONArray("cards");
                    for (int i = 0; i < cardsArray.length(); i++) {
                        allCardsArray.put(cardsArray.getJSONObject(i));
                    }
                    pageCount = jsonObject.getInt("pageCount");
                    currentPage++;

                    if (pageCount != 0) {
                        progress[0] = (double) currentPage / pageCount;
                        progress[0] = progress[0] * 0.7 + 0.2;
                        Platform.runLater(() -> updateProgress(progress[0], 1)); // Ensure updateProgress runs on the FX thread
                    }
                } while (currentPage <= pageCount);

                Platform.runLater(() -> updateProgress(1.0, 1)); // Update progress to 100% on FX thread

                return null;
            }
        };

        // Bind the progress bar to the task's progress property
        Platform.runLater(() -> progressBar.progressProperty().bind(downloadTask.progressProperty()));

        // Create a new thread to execute the task
        Thread downloadThread = new Thread(downloadTask);
        downloadThread.setDaemon(true); // Set as daemon thread to stop when the application exits
        downloadThread.start();

        try {
            downloadThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return allCardsArray;
    }

    private static JSONObject fetchJsonFromApi(String apiUrl) throws IOException, URISyntaxException {
        URL url = new URI(apiUrl).toURL();
        URLConnection urlc = url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
        StringBuilder responseBuilder = new StringBuilder();
        String inputLine;
        while ((inputLine = br.readLine()) != null) {
            responseBuilder.append(inputLine);
        }
        br.close();
        return new JSONObject(responseBuilder.toString());
    }

    private static ArrayList<Card> parseCards(JSONArray cardsArray) {
        ArrayList<Card> cards = new ArrayList<>();
        for (int i = 0; i < cardsArray.length(); i++) {
            JSONObject cardObj = cardsArray.getJSONObject(i);
            Card card = new Card();
            card.setId(cardObj.optInt("id", -1));
            //card.setCollectible(cardObj.optInt("collectible", -1));
            //card.setSlug(cardObj.optString("slug", ""));
            card.setClassId(cardObj.optInt("classId", -1));
            //card.setMultiClassIds(toArrayList(cardObj.optJSONArray("multiClassIds")));
            //card.setSpellSchoolId(cardObj.optInt("spellSchoolId", -1));
            card.setCardTypeId(cardObj.optInt("cardTypeId", -1));
            //card.setCardSetId(cardObj.optInt("cardSetId", -1));
            card.setRarityId(cardObj.optInt("rarityId", -1));
            //card.setArtistName(cardObj.optString("artistName", ""));
            card.setManaCost(cardObj.optInt("manaCost", -1));
            card.setName(cardObj.optString("name", ""));
            card.setText(cardObj.optString("text", ""));
            card.setImage(cardObj.optString("image", ""));
            //card.setImageGold(cardObj.optString("imageGold", ""));
            //card.setFlavorText(cardObj.optString("flavorText", ""));
            //card.setCropImage(cardObj.optString("cropImage", ""));
            //card.setKeywordIds(toArrayList(cardObj.optJSONArray("keywordIds")));
            //card.setZilliaxFunctionalModule(cardObj.optBoolean("isZilliaxFunctionalModule", false));
            //card.setZilliaxCosmeticModule(cardObj.optBoolean("isZilliaxCosmeticModule", false));
            //card.setDuels(parseDuels(cardObj.optJSONObject("duels")));
            //card.setCopyOfCardId(cardObj.optInt("copyOfCardId", -1));
            card.setHealth(cardObj.optInt("health", -1));
            card.setAttack(cardObj.optInt("attack", -1));
            //card.setMinionTypeId(cardObj.optInt("minionTypeId", -1));
            //card.setChildIds(toArrayList(cardObj.optJSONArray("childIds")));
            cards.add(card);
        }
        return cards;
    }
    
    private static void insertCardsIntoDatabase(ArrayList<Card> cards) {
        checkAndReconnect();
        Connection conn = getConnection();
        if (conn == null) {
            System.out.println("ERROR: No estàs connectat a la BBDD.");
            return;
        }

        String verificaSiExisteixCartaSQL = "SELECT COUNT(*) FROM cartes WHERE name = ?";
        String insertCartaSQL = "INSERT INTO cartes (name, class, mana_cost, attack, health, text, image_url, type, rarity) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String verificaSiExisteixImageSQL = "SELECT COUNT(*) FROM fotos_cartes WHERE image_url = ?";
        String insertImageSQL = "INSERT INTO fotos_cartes (image_url, image) VALUES (?, ?)";

        try (PreparedStatement checkCardStatement = conn.prepareStatement(verificaSiExisteixCartaSQL);
             PreparedStatement cartaStatement = conn.prepareStatement(insertCartaSQL);
             PreparedStatement checkImageStatement = conn.prepareStatement(verificaSiExisteixImageSQL);
             PreparedStatement imageStatement = conn.prepareStatement(insertImageSQL)) {
            
            int maxLen = cards.size();
            int counter = 0;

            for (Card card : cards) {
                counter++;
                checkCardStatement.setString(1, card.getName());
                try (ResultSet rs = checkCardStatement.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        System.out.println(String.format("INFO: La carta [%s] ja existeix", card.getName()));
                        continue;
                    }
                }

                checkImageStatement.setString(1, card.getImage());
                boolean imageExists = false;
                try (ResultSet rs = checkImageStatement.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        imageExists = true;
                    }
                }

                if (!imageExists) {
                    cartaStatement.setString(1, card.getName());
                    cartaStatement.setInt(2, card.getClassId());
                    cartaStatement.setInt(3, card.getManaCost());
                    cartaStatement.setInt(4, card.getAttack());
                    cartaStatement.setInt(5, card.getHealth());
                    cartaStatement.setString(6, card.getText());
                    cartaStatement.setString(7, card.getImage());
                    cartaStatement.setInt(8, card.getCardTypeId());
                    cartaStatement.setInt(9, card.getRarityId());

                    try {
                        byte[] imageBlob = downloadImage(card.getImage());
                        imageStatement.setString(1, card.getImage());
                        imageStatement.setBytes(2, imageBlob);
                        imageStatement.addBatch();
                        cartaStatement.addBatch(); // Only add card if the image download succeeds
                        
                        System.out.println("Carta afegida " + counter + " de " + maxLen); // mostra per pantalla el progres
                        
                    } catch (Exception e) {
                        System.out.printf("No s'ha afegit la imatge per la carta %s%n", card.getName());
                    }
                } else {
                    //System.out.println(String.format("INFO: La imatge per la carta [%s] ja existeix", card.getName()));
                }
            }

            cartaStatement.executeBatch();
            imageStatement.executeBatch();

        } catch (SQLException ex) {
            System.out.println("ERROR: No s'ha pogut insertar les cartes a la BBDD.");
            ex.printStackTrace();
        }
    }
    
    public static byte[] downloadImage(String imageUrl) throws IOException {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(imageUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            try (InputStream inputStream = connection.getInputStream();
                 ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                return outputStream.toByteArray();
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
