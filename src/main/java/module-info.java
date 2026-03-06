module org.example.sportconnect {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.sportconnect to javafx.fxml;
    exports org.example.sportconnect;
}