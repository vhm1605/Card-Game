module edu.hust.cardgame {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.graphics;
    exports edu.hust.cardgame.application;
    opens edu.hust.cardgame.application to javafx.graphics;

    exports edu.hust.cardgame.ui.view;
    opens edu.hust.cardgame.ui.view to javafx.fxml, javafx.graphics;

    exports edu.hust.cardgame.controller;
    exports edu.hust.cardgame.logic.tienlen;
    exports edu.hust.cardgame.logic.bacay;
    exports edu.hust.cardgame.strategy;
    exports edu.hust.cardgame.assets.imageaction;
    exports edu.hust.cardgame.assets.soundaction;
    exports edu.hust.cardgame.ai;
    exports edu.hust.cardgame.core;
}
