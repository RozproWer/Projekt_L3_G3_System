module com.giga {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.persistence;
    requires java.sql;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires org.flywaydb.core;
    requires com.calendarfx.view;
    requires itextpdf;
    requires java.desktop;
    requires tornadofx.controls;

    opens com.giga.htask to javafx.fxml;
    opens com.giga.htask.controllers to javafx.fxml;
    opens com.giga.htask.controllers.content to javafx.fxml;


    opens com.giga.htask.controllers.content.shared to javafx.fxml;


    exports com.giga.htask.controllers;


    exports com.giga.htask.controllers.content;

    exports com.giga.htask.controllers.content.shared;
    exports com.giga.htask.model;
    exports com.giga.htask;
    opens db.migration;
    opens com.giga.htask.model;
    exports com.giga.htask.controllers.content.doctors;
    opens com.giga.htask.controllers.content.doctors to javafx.fxml;
    exports com.giga.htask.utils;
    opens com.giga.htask.utils to javafx.fxml;
    exports com.giga.htask.controllers.content.patients;
    opens com.giga.htask.controllers.content.patients to javafx.fxml;
    exports com.giga.htask.controllers.content.visits;
    opens com.giga.htask.controllers.content.visits to javafx.fxml;
    exports com.giga.htask.controllers.content.tasks;
    opens com.giga.htask.controllers.content.tasks to javafx.fxml;
}