package gamescene;

import imageaction.BackgroundImage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import soundaction.ClickSound;

import java.util.List;

public class SelectGame {

    // Class nhỏ để chứa thông tin game
    static class GameOption {
        String name;
        int id;

        public GameOption(String name, int id) {
            this.name = name;
            this.id = id;
        }
    }

    public static Scene create(Stage primaryStage) {
        VBox selectPane = new VBox(20);
        selectPane.setAlignment(Pos.CENTER);

        Label label = new Label("Chọn Game");
        label.setStyle("-fx-font-size: 20px; -fx-font-family: 'Segoe UI'; -fx-text-fill: white;");


        List<GameOption> gameOptions = List.of(
                new GameOption("Tiến lên miền Nam", 1),
                new GameOption("Tiến lên miền Bắc", 2)

                // 👉 Chỉ cần thêm dòng này nếu có game mới
                // new GameOption("Phỏm", 3)
        );

        for (GameOption game : gameOptions) {
            Button button = new Button(game.name);
            button.setPrefSize(300, 50);
            button.setStyle("""
                -fx-background-color: linear-gradient(to right, #FFD700, #FFA500);
                -fx-text-fill: white;
                -fx-font-size: 16px;
                -fx-font-weight: bold;
                -fx-background-radius: 25;
                -fx-border-radius: 25;
                -fx-border-color: white;
                -fx-border-width: 2;
            """);

            button.setOnAction(e -> {
                ClickSound.play();
                primaryStage.setScene(GameConfigScene.create(primaryStage, game.id));
            });
            selectPane.getChildren().add(button);
        }

        selectPane.getChildren().add(0, label);

        StackPane inputRoot = new StackPane();
        inputRoot.setPrefSize(1280, 720);
        inputRoot.setBackground(BackgroundImage.set());
        inputRoot.getChildren().add(selectPane);

        return new Scene(inputRoot, 1280, 720);
    }
}
