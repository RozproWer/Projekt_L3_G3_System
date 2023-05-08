package com.giga.htask.utils;

import com.giga.htask.controllers.MainAuthedController;
import com.giga.htask.model.User;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

public class ButtonCellActionFactory<T> implements Callback<TableColumn<User, T>, TableCell<User, T>> {

    private final String buttonText;
    private final String tabTitle;
    private final Function<T, Void> action;

    public ButtonCellActionFactory(String buttonText, String tabTitle, Function<T, Void> action) {
        this.buttonText = buttonText;
        this.tabTitle = tabTitle;
        this.action = action;
    }

    @Override
    public TableCell<User, T> call(TableColumn<User, T> column) {
        final TableCell<User, T> cell = new TableCell<User, T>() {
            final Button btn = new Button(buttonText);

            @Override
            public void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    btn.setOnAction(event -> {
                        if (action != null) {
                            action.apply(item);
                        }
                        MainAuthedController.getInstance().addTab(tabTitle, null, null, true);
                    });
                    setGraphic(btn);
                }
                setText(null);
            }
        };
        return cell;
    }
}