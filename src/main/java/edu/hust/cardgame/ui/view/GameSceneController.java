package main.java.edu.hust.cardgame.ui.view;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class GameSceneController {
    @FXML
    private BorderPane borderPane;
    @FXML
    private StackPane centerPane;
    @FXML
    private Pane playerLayer;
    @FXML
    private Pane uiLayer;

    public void initialize() {
        /* no-op */ }

    public BorderPane getBorderPane() {
        return borderPane;
    }

    public StackPane getCenterPane() {
        return centerPane;
    }

    public Pane getUiLayer() {
        return uiLayer;
    }
}