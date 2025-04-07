package controller;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
//import gamescene.GamePlayScene;

public class GamePlayController {

    @FXML
    private StackPane topPane;

    @FXML
    private StackPane leftPane;

    @FXML
    private StackPane rightPane;

    @FXML
    private StackPane bottomPane;

    @FXML
    private StackPane centerPane;

    // Getter methods
    public StackPane getTopPane() {
        return topPane;
    }

    public StackPane getLeftPane() {
        return leftPane;
    }

    public StackPane getRightPane() {
        return rightPane;
    }

    public StackPane getBottomPane() {
        return bottomPane;
    }

    public StackPane getCenterPane() {
        return centerPane;
    }
}