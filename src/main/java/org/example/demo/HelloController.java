package org.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    public Label lbl;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/localization_db";
    private static final String DB_USER = "root";  // Change if needed
    private static final String DB_PASSWORD = "Test12";  // Change if needed

    private Connection connection;

    // Constructor to inject the connection (for testing purposes)
    public HelloController(Connection connection) {
        this.connection = connection;
    }

    // Default constructor for normal operation (without dependency injection)
    public HelloController() {
        try {
            this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the localized text from the database
     */
    @FXML
    // Change from private to public or protected
    public void loadLanguage(String locale) {
        String translation = getTranslation(locale, "greeting");
        if (translation != null) {
            lbl.setText(translation);
        } else {
            lbl.setText("Translation not found!");
        }
    }


    /**
     * Retrieves a translation from the database
     */
    // Change from private to protected or public
    protected String getTranslation(String locale, String key) {
        String query = "SELECT value FROM translations WHERE locale = ? AND key_name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, locale);
            pstmt.setString(2, key);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("value");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Default button click action
     */
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    /**
     * Language selection buttons
     */
    public void onbtnENClick(ActionEvent actionEvent) {
        loadLanguage("en_US");
    }

    public void onbtnFRClick(ActionEvent actionEvent) {
        loadLanguage("fr_FR");
    }

    public void onbtnJPClick(ActionEvent actionEvent) {
        loadLanguage("jp_JP");
    }

    public void onbtnIRClick(ActionEvent actionEvent) {
        loadLanguage("fa_IR");
    }
}
