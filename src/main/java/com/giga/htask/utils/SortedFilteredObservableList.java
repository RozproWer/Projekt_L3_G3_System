package com.giga.htask.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import java.util.List;

public class SortedFilteredObservableList<T> {
    private FilteredList<T> filteredList;
    private SortedList<T> sortedList;
    private ObservableList<T> observableList;

    public SortedFilteredObservableList( ObservableList<T> observableList, java.util.function.Predicate<T> filter) {
        updateLists(observableList, filter);
    }

    public void updateLists(ObservableList<T> observableList, java.util.function.Predicate<T> filter /*java.util.function.Function<T, Comparable> attribute*/) {
        this.filteredList = new FilteredList<T>(FXCollections.observableArrayList(observableList), filter);
        this.sortedList = new SortedList<T>(filteredList);
        this.observableList = observableList;
        this.observableList.setAll(sortedList);
    }

    public ObservableList<T> getObservableList() {
        return observableList;
    }
    //create getters for filteredList, sortedList, observableList

    public FilteredList<T> getFilteredList() {
        return filteredList;
    }
    public SortedList<T> getSortedList() {
        return sortedList;
    }
}
