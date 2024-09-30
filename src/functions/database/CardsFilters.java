package functions.database;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import static functions.database.DatabaseConnection.getConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardsFilters {
    private Connection connection = getConnection();
    
    public List<String> getAllCardImages() throws SQLException {
        List<String> urls = new ArrayList<>();
        String query = "SELECT IMAGE_URL FROM CARTES";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    urls.add(resultSet.getString("IMAGE_URL"));
                }
            }
        }
        return urls;
    }
    
    public List<String> filterCards(String selectedClass, int selectedAttack, int selectedCost, int selectedHealth, String selectedRarity, String selectedType) throws SQLException {
        
        System.out.println(selectedClass+ selectedAttack+selectedCost+selectedHealth+selectedRarity+selectedType);
        
        List<String> urls = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder("SELECT IMAGE_URL FROM CARTES WHERE 1=1");

        // Add selected filters to the query dynamically
        if (!selectedClass.equals("Todos") && !selectedClass.equals("Classe")) {
            queryBuilder.append(" AND CLASS = ?");
        }
        if (selectedAttack >= 0) {
            queryBuilder.append(" AND ATTACK = ?");
        }
        if (selectedCost >= 0) {
            queryBuilder.append(" AND MANA_COST = ?");
        }
        if (selectedHealth >= 0) {
            queryBuilder.append(" AND HEALTH = ?");
        }
        if (!selectedRarity.equals("Cualquiera") && !selectedRarity.equals("Raresa")) {
            queryBuilder.append(" AND RARITY = ?");
        }
        if (!selectedType.equals("Cualquiera") && !selectedType.equals("Tipus")) {
            queryBuilder.append(" AND TYPE = ?");
        }

        try (PreparedStatement statement = connection.prepareStatement(queryBuilder.toString())) {
            int parameterIndex = 1;

            // Set parameters based on selected filters
            if (!selectedClass.equals("Todos") && !selectedClass.equals("Classe")) {
                statement.setInt(parameterIndex++, getClassId(selectedClass));
            }
            if (selectedAttack >= 0) {
                statement.setInt(parameterIndex++, selectedAttack);
            }
            if (selectedCost >= 0) {
                statement.setInt(parameterIndex++, selectedCost);
            }
            if (selectedHealth >= 0) {
                statement.setInt(parameterIndex++, selectedHealth);
            }
            if (!selectedRarity.equals("Cualquiera") && !selectedRarity.equals("Raresa")) {
                statement.setInt(parameterIndex++, getRarityId(selectedRarity));
            }
            if (!selectedType.equals("Cualquiera") && !selectedType.equals("Tipus")) {
                statement.setInt(parameterIndex, getTypeID(selectedType));
            }
            
            System.out.println(statement);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    urls.add(resultSet.getString("IMAGE_URL"));
                }
            }
        }
        return urls;
    }

    
    // Function to filter cards by class
    public List<String> filterByClass(String selectedClass) throws SQLException {
        List<String> urls = new ArrayList<>();
        String query = "SELECT IMAGE_URL FROM CARTES WHERE CLASS = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            if (!selectedClass.equals("Todos")) {
                int classId = getClassId(selectedClass);
                statement.setInt(1, classId);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    urls.add(resultSet.getString("IMAGE_URL"));
                }
            }
        }
        return urls;
    }

    // Function to filter cards by attack
    public List<String> filterByAttack(int selectedAttack) throws SQLException {
        List<String> urls = new ArrayList<>();
        String query = "SELECT IMAGE_URL FROM CARTES WHERE ATTACK = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            if (selectedAttack >= 0) {
                statement.setInt(1, selectedAttack);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    urls.add(resultSet.getString("IMAGE_URL"));
                }
            }
        }
        return urls;
    }

    // Function to filter cards by cost
    public List<String> filterByCost(int selectedCost) throws SQLException {
        List<String> urls = new ArrayList<>();
        String query = "SELECT IMAGE_URL FROM CARTES WHERE MANA_COST = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            if (selectedCost >= 0) {
                statement.setInt(1, selectedCost);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    urls.add(resultSet.getString("IMAGE_URL"));
                }
            }
        }
        return urls;
    }

    // Function to filter cards by health
    public List<String> filterByHealth(int selectedHealth) throws SQLException {
        List<String> urls = new ArrayList<>();
        String query = "SELECT IMAGE_URL FROM CARTES WHERE HEALTH = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            if (selectedHealth >= 0) {
                statement.setInt(1, selectedHealth);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    urls.add(resultSet.getString("IMAGE_URL"));
                }
            }
        }
        return urls;
    }

    // Function to filter cards by rarity
    public List<String> filterByRarity(String selectedRarity) throws SQLException {
        List<String> urls = new ArrayList<>();
        String query = "SELECT IMAGE_URL FROM CARTES WHERE RARITY = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            if (!selectedRarity.equals("Cualquiera")) {
                int rarityId = getRarityId(selectedRarity);
                statement.setInt(1, rarityId);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    urls.add(resultSet.getString("IMAGE_URL"));
                }
            }
        }
        return urls;
    }

    // Function to filter cards by type
    public List<String> filterByType(String selectedType) throws SQLException {
        List<String> urls = new ArrayList<>();
        String query = "SELECT IMAGE_URL FROM CARTES WHERE TYPE = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            if (!selectedType.equals("Cualquiera")) {
                int typeId = getTypeID(selectedType);
                statement.setInt(1, typeId);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    urls.add(resultSet.getString("IMAGE_URL"));
                }
            }
        }
        return urls;
    }
    
    // Helper function to get class ID from its name
    private int getClassId(String className) throws SQLException {
        Map<String, Integer> classMap = new HashMap<>();
        classMap.put("Caballero de la muerte", 1);
        classMap.put("Druida", 2);
        classMap.put("Cazador", 3);
        classMap.put("Mago", 4);
        classMap.put("Paladin", 5);
        classMap.put("Sacerdote", 6);
        classMap.put("Picaro", 7);
        classMap.put("Chamán", 8);
        classMap.put("Brujo", 9);
        classMap.put("Guerrero", 10);
        classMap.put("Neutral", 12);
        classMap.put("Cazador de demonios", 14);
        
        return classMap.getOrDefault(className, 0);
    }

    // Helper function to get rarity ID from its name
    private int getRarityId(String rarityName) {
        Map<String, Integer> rarityMap = new HashMap<>();
        rarityMap.put("Gratis", 1);
        rarityMap.put("Comun", 2);
        rarityMap.put("Rara", 3);
        rarityMap.put("Epica", 4);
        rarityMap.put("Legendaria", 5);
        
        return rarityMap.getOrDefault(rarityName, 0);
    }

    // Helper function to get type ID from its name
    private int getTypeID(String typeName) {
        Map<String, Integer> typeMap = new HashMap<>();
        typeMap.put("Heroe", 1);
        typeMap.put("Minion", 2);
        typeMap.put("Hechizo", 3);
        typeMap.put("Arma", 4);
        typeMap.put("Location", 5);
        
        return typeMap.getOrDefault(typeName, 0);
    }
    
}
