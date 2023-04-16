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
    opens com.giga.htask.controllers.content to javafx.fxml;
    opens com.giga.htask.controllers.content.doctor to javafx.fxml;
    opens com.giga.htask.controllers.content.patient to javafx.fxml;

    opens com.giga.htask.controllers.content.shared to javafx.fxml;


    exports com.giga.htask.controllers;

    exports com.giga.htask.controllers.content.doctor;
    exports com.giga.htask.controllers.content.patient;
    exports com.giga.htask.controllers.content;

    exports com.giga.htask.controllers.content.shared;
    exports com.giga.htask.model;
    exports com.giga.htask;
    opens db.migration;
    opens com.giga.htask.model;
    exports com.giga.htask.controllers.content.admin;
    opens com.giga.htask.controllers.content.admin to javafx.fxml;
}