package com.giga.htask;

import com.giga.htask.App;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    private static Stage stage;

    @BeforeAll
    public static void setUp() {
        Platform.startup(() -> {
            stage = new Stage();
            try {
                App app = new App();
                app.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @AfterAll
    public static void tearDown() {
        Platform.runLater(() -> stage.close());
        Platform.exit();
    }

    @Test
    public void testDarkMode() {
        Platform.runLater(() -> {
            Scene scene = stage.getScene();
            assertNotNull(scene);

            // Check if dark mode is initially disabled
            assertFalse(App.stage.getScene().getStylesheets().contains(App.class.getResource("styles/darkmode.css").toExternalForm()));

            // Enable dark mode
            App.changeTheme();

            // Check if dark mode is enabled
            assertTrue(App.stage.getScene().getStylesheets().contains(App.class.getResource("styles/darkmode.css").toExternalForm()));

            // Disable dark mode
            App.changeTheme();

            // Check if dark mode is disabled
            assertFalse(App.stage.getScene().getStylesheets().contains(App.class.getResource("styles/darkmode.css").toExternalForm()));
        });
    }
}