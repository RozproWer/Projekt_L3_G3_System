package com.giga.htask;

import com.giga.htask.controllers.MainAuthedController;
import com.giga.htask.controllers.MainController;

import com.giga.htask.model.Context;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;


import org.hibernate.Session;
import org.flywaydb.core.Flyway;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * JavaFX App class.
 * This is the main class of the application. It is responsible for loading the main view and setting the scene.
 * It also manages the Hibernate SessionFactory object and the Flyway migration.
 *
 * @author GigaNByte
 * @since 1.0
 */
public class App extends Application {


    public static Stage stage;
    //called when app starts up and sets root of scene to MainView.fxml
    @Override
    public void start(Stage stage) throws IOException, ClassNotFoundException {

        // create db
        SessionFactory sessionFactory = HibernateConnection.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.close();

        String url = new Configuration().configure().getProperty("hibernate.connection.url").toString();
        String username = new Configuration().configure().getProperty("hibernate.connection.username").toString();
        String password = new Configuration().configure().getProperty("hibernate.connection.password").toString();

        //migrate db using flyway
        Flyway flyway = Flyway.configure().dataSource(url, username, password).baselineOnMigrate(true).load();

        flyway.migrate();

        // Get dimensions of the screen to make it fullscreen
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        FXMLLoader loader = new FXMLLoader(App.class.getResource("view/" + "MainView"+ ".fxml"));
        Parent root = loader.load();
        MainController controller1 = new MainController();
        loader.setController(controller1);
        Scene scene = new Scene(root);
        this.stage = stage;
        stage.setScene(scene);
        stage.setX(screenBounds.getMinX());
        stage.setY(screenBounds.getMinY());
        stage.setWidth(screenBounds.getWidth());
        stage.setHeight(screenBounds.getHeight());
        stage.setOnShowing(e -> {
            loadStyles();
        });
        stage.sceneProperty().addListener((observable, oldScene, newScene) -> {
            loadStyles();
        });
        stage.show();
    }

    public static void changeTheme(){
        if(Context.getInstance().isDarkMode()){
            App.stage.getScene().getStylesheets().add(App.class.getResource("styles/darkmode.css").toExternalForm());
        }
        else{
            App.stage.getScene().getStylesheets().remove(App.class.getResource("styles/darkmode.css").toExternalForm());
        }
    }
    private static void loadStyles(){
        App.stage.getScene().getStylesheets().add(App.class.getResource("styles/styles.css").toExternalForm());

        if(Context.getInstance().isDarkMode()){
            App.stage.getScene().getStylesheets().add(App.class.getResource("styles/darkmode.css").toExternalForm());
        }
    }
    //loads fxml file and sets controller to MainController
    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("view/" + fxml + ".fxml"));
        fxmlLoader.setController(MainController.getInstance());
        return fxmlLoader.load();
    }

    public static Parent loadView(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("view/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }
    public static Parent loadViewController(String fxml, Object controller) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("view/" + fxml + ".fxml"));
        fxmlLoader.setController(controller);
        return fxmlLoader.load();
    }

    //launches app from main method in AppLauncher
    public static void main(String[] args) {
        launch();
    }

}