package com.giga.htask.controllers.content;

import com.giga.htask.model.Context;
import com.giga.htask.model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class ContentController implements Initializable {

    protected User user;

    @FXML
    protected AnchorPane contentRoot;

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

    //create contructor
    public ContentController(Integer userId) {
        user = Context.getInstance().getEntityById(User.class, userId);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clearMessages();
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
    protected void updateTables(){
        updateTablesIfNeeded(false);
    }
    protected void updateTables(Boolean refresh){
        updateTablesIfNeeded(refresh);
    }
    protected void updateTablesIfNeeded(Boolean refresh){
    }
}
