package com.giga;

import com.giga.controllers.MainController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.Flushable;
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

    //root of scene graph for app
    private static Scene scene;

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

        //load main scene
        scene = new Scene(loadFXML("MainView"));
        stage.setScene(scene);
        stage.show();
        stage.setTitle("Hospital Tasks App");
    }

    //sets root of scene to fxml file
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    //loads fxml file and sets controller to MainController
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("view/" + fxml + ".fxml"));
        fxmlLoader.setController(MainController.getInstance());
        return fxmlLoader.load();
    }

    //launches app from main method in AppLauncher
    public static void main(String[] args) {
        launch();
    }

}