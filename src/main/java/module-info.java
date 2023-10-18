module com.example.bibliotheque {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.example.bibliotheque to javafx.fxml;
    exports com.example.bibliotheque;
}