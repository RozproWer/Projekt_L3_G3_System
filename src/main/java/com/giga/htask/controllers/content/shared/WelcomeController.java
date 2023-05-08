package com.giga.htask.controllers.content.shared;

import com.giga.htask.App;
import com.giga.htask.controllers.content.ContentController;
import com.giga.htask.model.Context;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.util.Duration;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class WelcomeController  extends ContentController implements Initializable {

    @FXML
    private Label timeLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label userLabel;

    public WelcomeController(Integer userId) {
        super(userId);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        userLabel.setText("Welcome, " + Context.getInstance().getLoggedUser().getName());
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateFormat timeFormat = new SimpleDateFormat("HH:mm");
            DateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy");
            Date currentTime = new Date();
            String formattedTime = timeFormat.format(currentTime);
            String formattedDate = dateFormat.format(currentTime);
            timeLabel.setText(formattedTime);
            dateLabel.setText(formattedDate);
        }),
                new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }
}

