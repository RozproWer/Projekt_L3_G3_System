package com.giga.htask.controllers.content;

import com.giga.htask.controllers.MainAuthedController;
import com.giga.htask.model.User;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class ButtonCellFactory implements Callback<TableColumn<User, Integer>, TableCell<User, Integer>> {

    private final String buttonText;
    private final String tabTitle;
    private final String fxmlPath;
    private final Object controller;

    public ButtonCellFactory(String buttonText, String tabTitle, String fxmlPath, Object controller) {
        this.buttonText = buttonText;
        this.tabTitle = tabTitle;
        this.fxmlPath = fxmlPath;
        this.controller = controller;
    }

    @Override
    public TableCell<User, Integer> call(TableColumn<User, Integer> column) {
        final TableCell<User, Integer> cell = new TableCell<User, Integer>() {
            final Button btn = new Button(buttonText);

            @Override
            public void updateItem(Integer userId, boolean empty) {
                super.updateItem(userId, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    btn.setOnAction(event -> {
                        MainAuthedController.getInstance().addTab(tabTitle, fxmlPath, controller, true);
                    });
                    setGraphic(btn);
                }
                setText(null);
            }
        };
        return cell;
    }
}