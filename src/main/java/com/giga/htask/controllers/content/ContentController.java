package com.giga.htask.controllers.content;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class ContentController implements Initializable {
    @FXML
    AnchorPane contentRoot;

    @FXML
    HBox contentOutput;

    @FXML
    public Label contentTitle;

    @FXML
    VBox contentVbox;

    @FXML
    Label contentError;

    @FXML
    Label contentSuccess;

    @FXML
    Label contentInfo;

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
}
