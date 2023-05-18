package com.giga.htask.utils;

import com.giga.htask.controllers.MainAuthedController;
import com.giga.htask.controllers.content.ContentController;
import com.giga.htask.model.User;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.lang.reflect.InvocationTargetException;

public class ButtonCellAddTabFactory<T> implements Callback<TableColumn<User, T>, TableCell<User, T>> {


    private final String tabTitle;
    private final String fxmlPath;
    private final Class<? extends ContentController> controllerClass;

    public ButtonCellAddTabFactory(String tabTitle, String fxmlPath, Class<? extends ContentController> controllerClass) {
        this.tabTitle = tabTitle;
        this.fxmlPath = fxmlPath;
        this.controllerClass = controllerClass;
    }

    @Override
    public TableCell<User, T> call(TableColumn<User, T> column) {
        final TableCell<User, T> cell = new TableCell<User, T>() {
            final Button btn = new Button(column.getText());

            @Override
            public void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    try {
                        ContentController controller = controllerClass.getDeclaredConstructor(Integer.class).newInstance((Integer) item);
                        btn.setOnAction(event -> {
                            MainAuthedController.getInstance().addTab(tabTitle, fxmlPath, controller, true);
                        });
                    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    setGraphic(btn);
                }
                setText(null);
            }
        };
        return cell;
    }
}