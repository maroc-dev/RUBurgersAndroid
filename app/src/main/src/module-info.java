module model {
    requires javafx.controls;
    requires javafx.fxml;

    requires junit;

    opens model to javafx.fxml;
    opens controller to javafx.fxml;
    exports model;
    exports test;
    exports controller;
}