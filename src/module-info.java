module CardGameOOP {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.graphics;
    requires java.desktop;
    requires java.smartcardio;

    opens main.java.edu.hust.cardgame.application to javafx.graphics, javafx.fxml;
    exports main.java.edu.hust.cardgame.model;
    opens main.java.edu.hust.cardgame.model to javafx.fxml, javafx.graphics;
    exports main.java.edu.hust.cardgame.ui.view;
    opens main.java.edu.hust.cardgame.ui.view to javafx.fxml, javafx.graphics;
}
