module com.schenplayground.fight_map_editor {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;


    opens com.schenplayground.fight_map_editor to javafx.fxml;
    exports com.schenplayground.fight_map_editor;
}