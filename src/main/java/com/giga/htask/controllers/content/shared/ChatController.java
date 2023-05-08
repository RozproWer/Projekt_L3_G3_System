package com.giga.htask.controllers.content.shared;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ChatController {

    @FXML
    private ScrollPane chatScrollPane;

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField textPrompt;

    @FXML
    private Button sendButton;

    @FXML
    public void initialize() {
        // Automatically scroll chat area to bottom
        chatArea.scrollTopProperty().addListener((observable, oldValue, newValue) -> {
            chatArea.setScrollTop(Double.MAX_VALUE);
        });
    }

    @FXML
    public void sendMessage() {
        String message = textPrompt.getText().trim();
        if (!message.isEmpty()) {
            // Append message to chat area
            chatArea.appendText(message + "\n");
            textPrompt.clear();
        }
    }
}