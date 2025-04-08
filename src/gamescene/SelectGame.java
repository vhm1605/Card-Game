package gamescene;

import imageaction.BackgroundImage;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import soundaction.ClickSound;

import java.util.List;

public class SelectGame {

    // Class nhỏ để chứa thông tin game
    public static class GameOption {
        public final String name;
        public final int id;
        public final int maxPlayers;

        public GameOption(String name, int id, int maxPlayers) {
            this.name = name;
            this.id = id;
            this.maxPlayers = maxPlayers;
        }
    }

    // Danh sách các game hỗ trợ
    public static final List<GameOption> gameOptions = List.of(
            new GameOption("Tiến lên miền Nam", 1, 4),
            new GameOption("Tiến lên miền Bắc", 2, 4)
            // 👉 Thêm game mới tại đây nếu cần
            // new GameOption("Phỏm", 3, 4)
    );

    public static Parent create(Stage primaryStage) {
        VBox selectPane = new VBox(20);
        selectPane.setAlignment(Pos.CENTER);

        Label label = new Label("Chọn Game");
        label.setStyle("-fx-font-size: 20px; -fx-font-family: 'Segoe UI'; -fx-text-fill: white;");

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
                Parent gameConfigRoot = GameConfigScene.create(primaryStage, game);
                primaryStage.getScene().setRoot(gameConfigRoot);
            });
            selectPane.getChildren().add(button);
        }

        selectPane.getChildren().add(0, label);

        StackPane inputRoot = new StackPane();
        inputRoot.setPrefSize(1280, 720);
        inputRoot.setBackground(BackgroundImage.set());
        inputRoot.getChildren().add(selectPane);

        return inputRoot;
    }
}
