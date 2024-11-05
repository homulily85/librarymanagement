module com.cozyspace.librarymanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires password4j;
    requires java.mail;
    requires java.desktop;

    opens com.cozyspace.librarymanagement to javafx.fxml;
    exports com.cozyspace.librarymanagement;
    exports com.cozyspace.librarymanagement.controller;
    opens com.cozyspace.librarymanagement.controller to javafx.fxml;
    exports com.cozyspace.librarymanagement.controller.login;
    opens com.cozyspace.librarymanagement.controller.login to javafx.fxml;
    exports com.cozyspace.librarymanagement.controller.reset_password;
    opens com.cozyspace.librarymanagement.controller.reset_password to javafx.fxml;
    exports com.cozyspace.librarymanagement.controller.create_new_account;
    opens com.cozyspace.librarymanagement.controller.create_new_account to javafx.fxml;
    exports com.cozyspace.librarymanagement.controller.librarian;
    opens com.cozyspace.librarymanagement.controller.librarian to javafx.fxml;
    exports com.cozyspace.librarymanagement.controller.librarian.document;
    opens com.cozyspace.librarymanagement.controller.librarian.document to javafx.fxml;
}