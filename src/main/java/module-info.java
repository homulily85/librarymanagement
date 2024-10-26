module com.cozyspace.librarymanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires password4j;
    requires java.mail;

    opens com.cozyspace.librarymanagement to javafx.fxml;
    exports com.cozyspace.librarymanagement;
    exports com.cozyspace.librarymanagement.controller;
    opens com.cozyspace.librarymanagement.controller to javafx.fxml;
}