module CardGameOOP {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.graphics;
    requires java.desktop;
    requires java.smartcardio;



    // Application entry point
    opens main.java.edu.hust.cardgame.application to javafx.graphics, javafx.fxml;

    // Model
    exports main.java.edu.hust.cardgame.model;
    opens main.java.edu.hust.cardgame.model to javafx.fxml, javafx.graphics;

    // UI
    exports main.java.edu.hust.cardgame.ui.view;
    opens main.java.edu.hust.cardgame.ui.view to javafx.fxml, javafx.graphics;

    // Controller
    exports main.java.edu.hust.cardgame.controller;
    opens main.java.edu.hust.cardgame.controller to javafx.fxml, javafx.graphics;

    // Logic (game rules)
    exports main.java.edu.hust.cardgame.logic.tienlen;
    opens main.java.edu.hust.cardgame.logic.tienlen to javafx.fxml;

    exports main.java.edu.hust.cardgame.logic.bacay;
    opens main.java.edu.hust.cardgame.logic.bacay to javafx.fxml;

    // Strategy
    exports main.java.edu.hust.cardgame.strategy;
    opens main.java.edu.hust.cardgame.strategy to javafx.fxml;

    // Assets (sound, image)
    exports main.java.edu.hust.cardgame.assets.imageaction;
    opens main.java.edu.hust.cardgame.assets.imageaction to javafx.fxml;

    exports main.java.edu.hust.cardgame.assets.soundaction;
    opens main.java.edu.hust.cardgame.assets.soundaction to javafx.fxml;

    // AI logic
    exports main.java.edu.hust.cardgame.ai;
    opens main.java.edu.hust.cardgame.ai to javafx.fxml;

    // Core (Deck, Game interfaces)
    exports main.java.edu.hust.cardgame.core;
    opens main.java.edu.hust.cardgame.core to javafx.fxml;
}
