package com.giga.htask.controllers;

import java.sql.Timestamp;

import com.giga.htask.model.User;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class TimestampValueFactory<S> implements Callback<TableColumn.CellDataFeatures<S, Timestamp>, ObservableValue<Timestamp>> {

    @Override
    public ObservableValue<Timestamp> call(TableColumn.CellDataFeatures<S, Timestamp> features) {
        // Get the Timestamp property value for the User object
        Timestamp timestamp = ((User) features.getValue()).getCreatedOn();
        return new ReadOnlyObjectWrapper<>(timestamp);
    }
}