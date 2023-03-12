
package com.giga.model;

import com.giga.HibernateConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
//TODO: EM now we have user roles so i dont know how to refactor Context class to handle it
// i thought about about creating different Context like ContextAdmin, ContextUser, ContextGuest classes but its is not as agile as possible

/**
 * Singleton class that handles queries and stores state shared across all fxml tab controllers in app
 *
 * @author GigaNByte
 * @since 1.0
 */
public class Context {
    private SessionFactory sessionFactory = HibernateConnection.getSessionFactory();

    /*
    private ObservableList<FireTest> fireTestTable = FXCollections.observableArrayList();
    private ObservableList<Vehicle> vehicleTable = FXCollections.observableArrayList();
    private ObservableList<Gun> gunTable = FXCollections.observableArrayList();
    private FilteredList<Gun> filteredGunTable = new FilteredList<>(gunTable, p -> true);
    private FilteredList<FireTest> filteredFireTestTable = new FilteredList<>(fireTestTable, p -> true);
    private FilteredList<Vehicle> filteredVehicleTable = new FilteredList<>(vehicleTable, p -> true);
    private SortedList<Gun> sortedGunTable =  new SortedList<>(filteredGunTable);
    private SortedList<FireTest> sortedFireTestTable =  new SortedList<>(filteredFireTestTable);
    private SortedList<Vehicle> sortedVehicleTable =  new SortedList<>(filteredVehicleTable);
    */


    private final static Context instance = new Context();

    /**
     * @return singleton instance of Context object
     */
    public static Context getInstance() {
        return instance;
    }


}