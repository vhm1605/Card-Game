package gamescene;

import imageaction.BackgroundImage;
import imageaction.CardImage;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import soundaction.ClickSound;
import module.CardType;
import gamelogic.CardGame;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
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
    protected boolean pressBack = false;

    public AbstractGamePlayScene(G game) {
        this.game = game;
    }

    public Parent createGamePlay(Stage primaryStage, boolean isBasic) {
        this.primaryStage = primaryStage;
        this.isBasic = isBasic;
        BorderPane root;
        if (!isBasic) {
            try {
                URL fxmlLocation = getClass().getResource("GamePlayScene.fxml");
                FXMLLoader loader = new FXMLLoader(fxmlLocation);
                root = loader.load();
                GameSceneController controller = loader.getController();
                this.centerPane = controller.getCenterPane();
                this.bottomPane = controller.getBottomPane();
                this.topPane = controller.getTopPane();
                this.leftPane = controller.getLeftPane();
                this.rightPane = controller.getRightPane();
                root.setBackground(BackgroundImage.set("/resources/card/backgroundgameplay.png"));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            root = new BorderPane();
            this.centerPane = new StackPane();
            this.bottomPane = new StackPane();
            this.topPane = new StackPane();
            this.leftPane = new StackPane();
            this.rightPane = new StackPane();
            root.setCenter(centerPane);
            root.setBottom(bottomPane);
            root.setTop(topPane);
            root.setLeft(leftPane);
            root.setRight(rightPane);
            centerPane.setPrefSize(269, 200);
            bottomPane.setPrefSize(600, 100);
            topPane.setPrefSize(600, 100);
            leftPane.setPrefSize(200, 200);
            rightPane.setPrefSize(200, 200);
            root.setPrefSize(800, 500);
            root.setBackground(new Background(new BackgroundFill(Color.DARKGREEN, null, null)));
        }
        setPanePadding();
        updateScene();
        return root;
    }

    protected void addPlayerNameLabel(StackPane pane, String name, int index) {
        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        nameLabel.setPrefWidth(Region.USE_COMPUTED_SIZE);
        nameLabel.setMinWidth(Region.USE_PREF_SIZE);

        if (index == 2) {
            StackPane.setAlignment(nameLabel, Pos.BOTTOM_CENTER);
            StackPane.setMargin(nameLabel, new Insets(150, 0, 0, 0));
        } else if (index == 0) {
            StackPane.setAlignment(nameLabel, Pos.TOP_CENTER);
            StackPane.setMargin(nameLabel, new Insets(0, 0, 140, 0));
        } else {
            StackPane.setAlignment(nameLabel, Pos.TOP_CENTER);
            StackPane.setMargin(nameLabel, new Insets(20, 0, 5, 0));
        }

        pane.getChildren().add(nameLabel);
    }
    private void setPanePadding() {
        bottomPane.setPadding(new Insets(10, 0, 10, 0));
        topPane.setPadding(new Insets(10, 0, 10, 0));
        leftPane.setPadding(new Insets(80, 150, 80, 150));
        rightPane.setPadding(new Insets(80, 150, 80, 150));
    }

    protected Button createOutButton() {
        Button backButton = new Button("Out");
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
        pressBack = true;
        Parent selectGameRoot = SelectGame.create(primaryStage);
        primaryStage.getScene().setRoot(selectGameRoot);
    }

    protected abstract void updateScene();
}