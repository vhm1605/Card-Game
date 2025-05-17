package gamescene;

import imageaction.BackgroundImage;
import soundaction.ClickSound;
import module.CardType;
import controller.GameSceneController;
import gamelogic.CardGame;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.Pane;
//import game.engine.CardGame; // Placeholder, replace with your actual game engine's base class/interface
//import game.engine.CardType; // Placeholder

public abstract class AbstractGamePlayScene<C extends CardType, G extends CardGame<C>> {
    protected G game;
    protected StackPane centerPane;
    protected StackPane bottomPane;
    protected StackPane topPane;
    protected StackPane leftPane;
    protected StackPane rightPane;
    protected Stage primaryStage;
    protected boolean isBasic;

    public AbstractGamePlayScene(G game) {
        this.game = game;
    }

    public Parent createGamePlay(Stage primaryStage, boolean isBasic) {
        this.primaryStage = primaryStage;
        this.isBasic = isBasic;
        try {
            URL fxmlLocation = getClass().getResource("GamePlayScene.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();
            GameSceneController controller = loader.getController();

            this.centerPane = controller.getCenterPane();
            this.bottomPane = controller.getBottomPane();
            this.topPane = controller.getTopPane();
            this.leftPane = controller.getLeftPane();
            this.rightPane = controller.getRightPane();

            ((Pane) root).setBackground(BackgroundImage.set("/resources/card/backgroundgameplay.png"));

            setPanePadding();
            createGameSpecificUI();

            return root;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setPanePadding() {
        bottomPane.setPadding(new Insets(10, 0, 10, 0));
        topPane.setPadding(new Insets(10, 0, 10, 0));
        leftPane.setPadding(new Insets(80, 150, 80, 150));
        rightPane.setPadding(new Insets(80, 150, 80, 150));
    }

    protected Button createBackButton() {
        Button backButton = new Button("Back");
        backButton.setPrefSize(150, 40);
        String buttonStyle = """
            -fx-background-color: linear-gradient(to right, #FF5722, #E64A19);
            -fx-text-fill: white;
            -fx-font-size: 16px;
            -fx-font-weight: bold;
            -fx-background-radius: 20;
            -fx-border-radius: 20;
            -fx-border-color: white;
            -fx-border-width: 2;
        """;
        backButton.setStyle(buttonStyle);
        backButton.setOnAction(e -> handleBackAction());
        return backButton;
    }

    protected void handleBackAction() {
        ClickSound.play();
        Parent selectGameRoot = SelectGame.create(primaryStage);
        primaryStage.getScene().setRoot(selectGameRoot);
    }

    protected abstract void createGameSpecificUI();
    protected abstract void updateScene();
}