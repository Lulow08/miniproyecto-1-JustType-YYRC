module com.lulow.justtype {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.lulow.justtype to javafx.fxml;
    exports com.lulow.justtype;
}