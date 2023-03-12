package com.giga;

import com.giga.controllers.MainController;
import org.flywaydb.core.Flyway;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import org.hibernate.Session;
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
        // session.createQuery("PRAGMA foreign_keys = ON;");
        session.close();

        //migrate db using flyway
        Flyway flyway = Flyway.configure().dataSource("jdbc:sqlite:sqlite/db/htask.db", null, null).baselineOnMigrate(true).load();
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