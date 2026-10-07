package edu.hust.cardgame.application;

import edu.hust.cardgame.assets.soundaction.ClickSound;
import edu.hust.cardgame.assets.imageaction.BackgroundImage;
import edu.hust.cardgame.ui.view.SelectGame;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        root.setPrefSize(1280, 720);
        root.setBackground(BackgroundImage.set());

        Image image = new Image(Objects.requireNonNull(
                Main.class.getResource("/card/start_button.png"),
                "Missing start button image"
        ).toExternalForm());

        ImageView startButton = new ImageView(image);
        startButton.setPreserveRatio(true);
        startButton.setSmooth(true);
        startButton.fitWidthProperty().bind(primaryStage.widthProperty().multiply(0.25));
        startButton.setOnMouseClicked(event -> {
            ClickSound.play();
            Parent selectGameRoot = SelectGame.create(primaryStage);
            primaryStage.getScene().setRoot(selectGameRoot);
        });

        root.getChildren().add(new StackPane(startButton));

        primaryStage.setTitle("Game Bài");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
