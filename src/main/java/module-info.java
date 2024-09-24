module com.example.ass4 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires java.sql.rowset;

    opens com.example.ass4 to javafx.fxml;
    exports com.example.ass4;
}