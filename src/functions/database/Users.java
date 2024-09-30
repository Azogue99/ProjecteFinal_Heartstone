package functions.database;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static functions.database.DatabaseConnection.getConnection;

public class Users {
    
    public static void showAlert(AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    public static String encryptPassword(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error encrypting password: " + e.getMessage());
        }
    }
    
    public static Boolean createUser(String username, String password, Boolean isAdmin) {
        String cryptedPassword = encryptPassword(password);
        Connection conn = getConnection();
        if (conn == null) {
            showAlert(AlertType.ERROR, "Error", "Database Connection Error", "Could not get a connection to the database.");
            return false;
        }
        try {
            String sql = "INSERT INTO usuaris (username, password, is_admin) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, username);
                pstmt.setString(2, cryptedPassword);
                pstmt.setBoolean(3, isAdmin);
                pstmt.executeUpdate();
            }
            showAlert(AlertType.INFORMATION, "Success", "User Created", String.format("User [%s] has been successfully created.", username));
            return true;
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Error", "Save Error", "Could not save the new user.");
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean checkCredentials(String username, String password) {
        String cryptedPassword = encryptPassword(password);
        DatabaseConnection.checkAndReconnect();
        Connection conn = getConnection();
        if (conn == null) {
            System.out.println("Could not get a connection to the database.");
            return false;
        }
        try {
            String sql = "SELECT password FROM usuaris WHERE username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, username);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String storedPassword = rs.getString("password");
                        return storedPassword.equals(cryptedPassword);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Could not verify the user.");
            e.printStackTrace();
        }
        return false;
    }

    public static Boolean isAdmin(String username) {
        Connection conn = getConnection();
        if (conn == null) {
            System.out.println("Could not get a connection to the database.");
            return false;
        }
        try {
            String sql = "SELECT is_admin FROM usuaris WHERE username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, username);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getBoolean("is_admin");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Could not verify if the user is an admin.");
            e.printStackTrace();
        }
        return false;
    }

    public static Boolean existsAdmin() {
        Connection conn = getConnection();
        if (conn == null) {
            System.out.println("Could not get a connection to the database.");
            return false;
        }
        try {
            String sql = "SELECT COUNT(*) FROM usuaris WHERE is_admin = true";
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error reading users: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
