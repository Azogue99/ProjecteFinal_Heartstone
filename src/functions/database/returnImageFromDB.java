package functions.database;

import java.sql.Blob;
import java.io.ByteArrayInputStream;
import javafx.scene.image.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import static functions.database.DatabaseConnection.getConnection;
import java.io.InputStream;

public class returnImageFromDB {
    private Connection connection = getConnection();
    
    public Image getImageData(String imageURL) throws SQLException {
        String query = "SELECT image FROM fotos_cartes WHERE image_url = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            // Set the URL parameter in the prepared statement
            statement.setString(1, imageURL);
            // Execute the query
            try (ResultSet resultSet = statement.executeQuery()) {
                // Check if there is a result
                if (resultSet.next()) {
                    // Retrieve the image data as a byte array
                    Blob imageData = resultSet.getBlob("image");                    
                    // Convert the byte array to an Image object
                    InputStream is = imageData.getBinaryStream();
                    Image image = new Image(is);
                    
                    return image;
                }
            }
        }
        // Return null if no image is found or an error occurs
        return null;
    }
    
    // Method to convert byte array to JavaFX Image
    private Image convertByteArrayToImage(byte[] imageData) {
        try {
            // Create an input stream from the byte array
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);
            // Read the input stream as an image
            Image image = new Image(inputStream);
            // Return the image
            return image;
        } catch (Exception e) {
            // Handle any exceptions (e.g., invalid image data)
            e.printStackTrace();
            return null;
        }
    }

}