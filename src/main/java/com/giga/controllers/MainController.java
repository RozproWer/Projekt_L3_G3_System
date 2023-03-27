package com.giga.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

//TODO: Main controller needs to control authentication and authorization of users and their avaible tabs (modules) and Login logout somehow
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


    @FXML
    private final static MainController instance = new MainController();
    
    @FXML
    private TabPane MainTabPane; // TODO: Tab pane is horizontal switcher THERE is alternate vertical tab pane? In our UI project we have vertical tab pane

    /**
     * Adds Tab to MainTabPane
     *
     * @param tab to be added
     * @author GigaNByte
     * @since 1.0
     */
    public void addTab(Tab tab) {
        MainTabPane.getTabs().add(tab);
        MainTabPane.getSelectionModel().select(tab);
    }

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
