module CardGameOOP {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.graphics;
    requires java.desktop;
    requires java.smartcardio;

    opens application to javafx.graphics, javafx.fxml;
    opens gamescene to javafx.graphics, javafx.fxml;

    exports gamescene;
}