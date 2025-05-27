package main.java.edu.hust.cardgame.application;

import main.java.edu.hust.cardgame.assets.soundaction.ClickSound;
import main.java.edu.hust.cardgame.assets.imageaction.BackgroundImage;
import main.java.edu.hust.cardgame.ui.view.SelectGame;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            StackPane root = new StackPane();
            root.setPrefSize(1280, 720);

            root.setBackground(BackgroundImage.set());

            Image image = new Image(
                    Main.class.getResource("/main/resources/card/start_button.png").toExternalForm()
            );

            ImageView startButton = new ImageView(image);
            startButton.setPreserveRatio(true);
            startButton.setSmooth(true);

            startButton.fitWidthProperty().bind(
                    primaryStage.widthProperty().multiply(0.25)
            );

            StackPane buttonContainer = new StackPane(startButton);

            startButton.setOnMouseClicked(e -> {
                ClickSound.play();
                Parent selectGameRoot = SelectGame.create(primaryStage);

                Scene currentScene = primaryStage.getScene();
                currentScene.setRoot(selectGameRoot);
            });

            root.getChildren().add(buttonContainer);

            Scene mainScene = new Scene(root, 1280, 720);

            primaryStage.setTitle("Game Bài");
            primaryStage.setScene(mainScene);
//            primaryStage.setFullScreen(true);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
