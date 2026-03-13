module org.example.sportconnect {

    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;

    requires java.naming;

    requires org.hibernate.orm.core;
    requires jakarta.persistence;

    opens org.example.sportconnect to javafx.fxml;
    opens org.example.sportconnect.controller to javafx.fxml;

    opens org.example.sportconnect.model to org.hibernate.orm.core, javafx.base;

    exports org.example.sportconnect;
    exports org.example.sportconnect.controller;
    exports org.example.sportconnect.dao;
    exports org.example.sportconnect.model;
    exports org.example.sportconnect.service;
    exports org.example.sportconnect.util;
}