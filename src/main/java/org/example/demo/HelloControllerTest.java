package org.example.demo;

import org.example.demo.HelloController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;
import java.sql.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

public class HelloControllerTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    private HelloController helloController;

    @BeforeEach
    void setUp() throws SQLException {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Inject mocked connection into the controller
        helloController = new HelloController(mockConnection);

        // Mock the database interaction
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
    }

    @Test
    void testLoadLanguage_English() throws SQLException {
        // Simulate the database response for English translation
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("value")).thenReturn("Hello!");

        // Call the onbtnENClick method, which internally calls loadLanguage
        helloController.onbtnENClick(null);

        // Verify that the label text is updated with the correct translation
        assertEquals("Hello!", helloController.lbl.getText());
    }

    @Test
    void testGetTranslation_English() throws Exception {
        // Get the private method using reflection
        Method method = HelloController.class.getDeclaredMethod("getTranslation", String.class, String.class);
        method.setAccessible(true);  // Make the private method accessible

        // Create a mock connection and other mocks as usual
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("value")).thenReturn("Hello!");

        // Invoke the private method using reflection
        String translation = (String) method.invoke(helloController, "en_US", "greeting");

        // Verify the result
        assertEquals("Hello!", translation);
    }

    @Test
    void testGetTranslation_French() throws SQLException {
        // Mock behavior for French translation
        when(mockResultSet.next()).thenReturn(true);  // Simulate data found
        when(mockResultSet.getString("value")).thenReturn("Bonjour!");

        // Call method
        String translation = helloController.getTranslation("fr_FR", "greeting");

        // Verify the translation returned is correct
        assertEquals("Bonjour!", translation);
    }

    @Test
    void testGetTranslation_NoResult() throws SQLException {
        // Simulate no translation found
        when(mockResultSet.next()).thenReturn(false);  // No data found

        // Call method
        String translation = helloController.getTranslation("es_ES", "greeting");

        // Verify no translation is returned
        assertNull(translation);
    }

    @Test
    void testLoadLanguage() throws SQLException {
        // Simulate that the translation exists
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("value")).thenReturn("Hello!");

        // Call the loadLanguage method
        helloController.loadLanguage("en_US");

        // Verify that it was updated with the correct translation
        assertEquals("Hello!", helloController.lbl.getText());
    }

    @Test
    void testLoadLanguage_NoTranslation() throws SQLException {
        // Simulate no translation found
        when(mockResultSet.next()).thenReturn(false);

        // Call the loadLanguage method
        helloController.loadLanguage("es_ES");

        // Verify that it was updated with the "Translation not found!" message
        assertEquals("Translation not found!", helloController.lbl.getText());
    }

    @Test
    void testOnbtnENClick() throws SQLException {
        // Simulate the database response for English translation
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("value")).thenReturn("Hello!");

        // Call the onbtnENClick method, which should invoke loadLanguage
        helloController.onbtnENClick(null); // Pass a mock or null as ActionEvent

        // Verify that the label text is updated with the correct translation
        assertEquals("Hello!", helloController.lbl.getText());
    }

    @Test
    void testOnbtnFRClick() throws SQLException {
        // Simulate the database response for French translation
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("value")).thenReturn("Bonjour!");

        // Call the onbtnFRClick method, which should invoke loadLanguage
        helloController.onbtnFRClick(null);

        // Verify that the label text is updated with the correct translation
        assertEquals("Bonjour!", helloController.lbl.getText());
    }
}
