module com.iit.cm2601.teammate {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.iit.cm2601.teammate to javafx.fxml;
    exports com.iit.cm2601.teammate;
}