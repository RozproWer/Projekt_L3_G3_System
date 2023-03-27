module com.giga {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.persistence;
    requires java.sql;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires org.flywaydb.core;

    opens com.giga.htask to javafx.fxml;
    opens com.giga.htask.controllers to javafx.fxml;
    exports com.giga.htask.controllers;
    exports com.giga.htask.model;
    exports com.giga.htask;
    opens db.migration;
}