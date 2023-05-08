package com.giga.htask.utils;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.sql.Timestamp;
import java.util.function.Function;

public class TimestampValueFactory<T> implements Callback<TableColumn.CellDataFeatures<T, Timestamp>, ObservableValue<Timestamp>> {

    private final Function<T, Timestamp> timestampMapper;

    public TimestampValueFactory(Function<T, Timestamp> timestampMapper) {
        this.timestampMapper = timestampMapper;
    }

    @Override
    public ObservableValue<Timestamp> call(TableColumn.CellDataFeatures<T, Timestamp> features) {
        Timestamp timestamp = timestampMapper.apply(features.getValue());
        return new ReadOnlyObjectWrapper<>(timestamp);
    }
}