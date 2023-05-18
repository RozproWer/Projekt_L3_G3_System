package com.giga.htask.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.giga.htask.App;
import com.giga.htask.HibernateConnection;
import com.giga.htask.model.Context;
import com.giga.htask.model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.hibernate.SessionFactory;


/**
 * Singleton main app controller that manages TabPane tabs controllers
 *
 * @author GigaNByte
 * @since 1.0
 */
public class MainController implements Initializable {

    // Variable name must match name of Controller which is name of Class of Controller:
    // https://docs.oracle.com/javase/8/javafx/api/javafx/fxml/doc-files/introduction_to_fxml.html#nested_controllers
    // https://community.oracle.com/tech/developers/discussion/3561276/refresh-included-tab-page

    private final SessionFactory sessionFactory = HibernateConnection.getSessionFactory();

    @FXML
    private AnchorPane rootPane;


    private final static MainController instance = new MainController();


    @FXML
    private void handleLogin() throws IOException {
        String email = emailField.getText();
        String password = passwordField.getText();
        Context.getInstance().login(email, password);
        if (Context.getInstance().getLoggedUser() == null) {
            errorLabel.setText("Invalid username or password.");
            return;
        }
        errorLabel.setText("Valid");


        Parent root = App.loadViewController("MainAuthedView", MainAuthedController.getInstance());
        App.stage.setScene(new Scene(root));
    }


    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    /**
     * @return singleton instance of Context object
     */
    public static MainController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

}
