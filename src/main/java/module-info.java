module com.cozyspace.librarymanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires password4j;


    opens com.cozyspace.librarymanagement to javafx.fxml;
    exports com.cozyspace.librarymanagement;
}