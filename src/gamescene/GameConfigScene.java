package gamescene;

import imageaction.BackgroundImageView;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import soundeffect.ClickSound;
import gamelogic.TienLen;
import gamelogic.TienLenMienBac;
import gamelogic.TienLenMienNam;

public class GameConfigScene {

    public static Scene create(Stage primaryStage, int gameType) {
        VBox inputPane = new VBox(20);
        inputPane.setAlignment(Pos.CENTER);

        Label label = new Label("Cấu hình ván chơi");
        label.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");

        TextField playerTextField = new TextField();
        playerTextField.setPromptText("Số lượng người chơi");
        playerTextField.setMaxWidth(200);
        playerTextField.setStyle("""
				-fx-font-size: 14px;
				-fx-background-radius: 10;
				-fx-border-radius: 10;
				-fx-border-color: white;
				-fx-border-width: 2;
				-fx-padding: 5 10 5 10;
		""");

        TextField botTextField = new TextField();
        botTextField.setPromptText("Số lượng Bot");
        botTextField.setMaxWidth(200);
        botTextField.setStyle("""
				-fx-font-size: 14px;
				-fx-background-radius: 10;
				-fx-border-radius: 10;
				-fx-border-color: white;
				-fx-border-width: 2;
				-fx-padding: 5 10 5 10;
		""");

        // Chọn chế độ đồ họa
        Label graphicsLabel = new Label("Chế độ đồ họa:");
        graphicsLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        RadioButton basicMode = new RadioButton("Cơ bản");
        RadioButton fancyMode = new RadioButton("Đẹp");

        basicMode.setStyle("-fx-text-fill: white;");
        fancyMode.setStyle("-fx-text-fill: white;");

        ToggleGroup graphicsToggle = new ToggleGroup();
        basicMode.setToggleGroup(graphicsToggle);
        fancyMode.setToggleGroup(graphicsToggle);
        fancyMode.setSelected(true); // Mặc định chọn "Đẹp"

        VBox graphicsBox = new VBox(5, graphicsLabel, fancyMode, basicMode);
        graphicsBox.setAlignment(Pos.CENTER);

        // Nút tiếp theo
        Button next = new Button("Tiếp theo");
        next.setPrefSize(150, 40);
        next.setStyle("""
				-fx-background-color: linear-gradient(to right, #4CAF50, #81C784);
				-fx-text-fill: white;
				-fx-font-size: 16px;
				-fx-font-weight: bold;
				-fx-background-radius: 20;
				-fx-border-radius: 20;
				-fx-border-color: white;
				-fx-border-width: 2;
		""");

        next.setOnAction(e -> {
            ClickSound.play();

            int playerCount = parseInput(playerTextField.getText());
            int botCount = parseInput(botTextField.getText());
            int numOfPlayer = playerCount + botCount;

            if (numOfPlayer <= 1 || numOfPlayer > 4) return;

            boolean isBasic = basicMode.isSelected();

            if (gameType == 1) { // Tiến lên miền Nam
                TienLen game = new TienLenMienNam(numOfPlayer, botCount);
                if (isBasic) {
                    GameMainSceneBasic<TienLenMienNam> scene = new GameMainSceneBasic<>((TienLenMienNam) game, primaryStage);
                    primaryStage.setScene(scene.createScene());
                } else {
                    GameMainScene<TienLenMienNam> scene = new GameMainScene<>((TienLenMienNam) game);
                    primaryStage.setScene(scene.createScene());
                }
            } else if (gameType == 2) { // Tiến lên miền Bắc
                TienLen game = new TienLenMienBac(numOfPlayer, botCount);
                if (isBasic) {
                    GameMainSceneBasic<TienLenMienBac> scene = new GameMainSceneBasic<>((TienLenMienBac) game, primaryStage);
                    primaryStage.setScene(scene.createScene());
                } else {
                    GameMainScene<TienLenMienBac> scene = new GameMainScene<>((TienLenMienBac) game);
                    primaryStage.setScene(scene.createScene());
                }
            }
        });

        // Nút quay lại
        Button backButton = new Button("Quay lại");
        backButton.setPrefSize(150, 40);
        backButton.setStyle("""
				-fx-background-color: linear-gradient(to right, #f44336, #e57373);
				-fx-text-fill: white;
				-fx-font-size: 16px;
				-fx-font-weight: bold;
				-fx-background-radius: 20;
				-fx-border-radius: 20;
				-fx-border-color: white;
				-fx-border-width: 2;
		""");
        backButton.setOnAction(event -> {
            ClickSound.play();
            primaryStage.setScene(SelectGame.create(primaryStage));
        });

        // Add everything to layout
        inputPane.getChildren().addAll(
                label,
                playerTextField,
                botTextField,
                graphicsBox,
                next,
                backButton
        );

        StackPane inputRoot = new StackPane();
        inputRoot.setPrefSize(1280, 720);
        inputRoot.setBackground(BackgroundImageView.set());
        inputRoot.getChildren().add(inputPane);

        // Remove focus on text field when clicking outside
        inputRoot.setOnMouseClicked(event -> {
            playerTextField.getParent().requestFocus();
        });

        return new Scene(inputRoot, 1280, 720);
    }

    public static int parseInput(String input) {
        if (input == null || input.trim().isEmpty()) return 0;
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
