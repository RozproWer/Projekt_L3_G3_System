package com.giga.htask.controllers.content;

import com.giga.htask.model.Context;
import com.giga.htask.model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class ContentController implements Initializable {

    protected User user;
    protected Tab currentTab;
    protected TabPane tabPane;
    @FXML
    protected AnchorPane contentRoot;

    @FXML
    protected ScrollPane contentScrollPane;

    @FXML
    protected HBox contentOutput;

    @FXML
    protected Label contentTitle;

    @FXML
    protected VBox contentVbox;

    @FXML
    protected Label contentError;

    @FXML
    protected Label contentSuccess;

    @FXML
    protected Label contentInfo;


    public void setCurrentTab(Tab currentTab) {
        this.currentTab = currentTab;
    }


    public void setTabPane(TabPane tabPane) {
        this.tabPane = tabPane;
    }

    //create contructor
    public ContentController(Integer userId) {
        user = Context.getInstance().getEntityById(User.class, userId);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (contentScrollPane != null) {
            contentScrollPane.setFitToWidth(true);
            contentScrollPane.setFitToHeight(true);
            contentScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        }


        clearMessages();
        contentRoot.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                // The anchor pane has become active
                System.out.println("AnchorPane +" + this.toString() + "+ has become active");
            } else {
                // The anchor pane has lost focus
                System.out.println("AnchorPane " + this.toString() + "has lost focus");
            }
        });
    }

    //set error message, set success message, set info message
    public void setError(String error) {
        contentOutput.setVisible(true);
        contentError.setText(error);
    }

    public void setSuccess(String success) {
        contentOutput.setVisible(true);
        contentSuccess.setText(success);
    }

    public void setInfo(String info) {
        contentOutput.setVisible(true);
        contentInfo.setText(info);
    }

    public void clearMessages() {
        contentOutput.setVisible(false);
        contentError.setText("");
        contentSuccess.setText("");
        contentInfo.setText("");
    }

    //because there is no default arguments in java we have 3 fookin methods
    protected void updateTables() {
        updateTablesIfNeeded(false);
    }

    protected void updateTables(Boolean refresh) {
        updateTablesIfNeeded(refresh);
    }

    protected void updateTablesIfNeeded(Boolean refresh) {
        if (refresh) {
            updateTables();
        }
    }

    public void closeTab() {
        tabPane.getTabs().remove(currentTab);
        System.out.println("Tab closed");
    }

    public void onTabSelected() {
        System.out.println("Tab piri");
        updateTablesIfNeeded(true);
    }
}
