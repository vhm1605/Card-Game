package main.java.edu.hust.cardgame.ui.view;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import main.java.edu.hust.cardgame.assets.imageaction.BackgroundImage;
import main.java.edu.hust.cardgame.assets.soundaction.ClickSound;
import main.java.edu.hust.cardgame.controller.GameController;
import main.java.edu.hust.cardgame.controller.GameControllerFactory;

public class GameConfigScene {

    public static Parent create(Stage primaryStage, GameOption gameOption) {
        System.out.println("Selected game option: " + gameOption.name + ", max players: " + gameOption.maxPlayers);
        VBox inputPane = new VBox(20);
        inputPane.setAlignment(Pos.CENTER);

        Label label = new Label("Cấu hình ván chơi");
        label.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");

        TextField playerTextField = createStyledTextField("Số lượng người chơi");
        TextField botTextField = createStyledTextField("Số lượng Bot");

        Label maxPlayersLabel = new Label("Tổng số người chơi (Người + Bot) tối đa: " + gameOption.maxPlayers);
        maxPlayersLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        Label warningLabel = new Label();
        warningLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");

        Label graphicsLabel = new Label("Chế độ đồ họa:");
        graphicsLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        RadioButton basicMode = new RadioButton("Cơ bản");
        RadioButton fancyMode = new RadioButton("Đẹp");

        basicMode.setStyle("-fx-text-fill: white;");
        fancyMode.setStyle("-fx-text-fill: white;");

        ToggleGroup graphicsToggle = new ToggleGroup();
        basicMode.setToggleGroup(graphicsToggle);
        fancyMode.setToggleGroup(graphicsToggle);
        fancyMode.setSelected(true);

        VBox graphicsBox = new VBox(5, graphicsLabel, fancyMode, basicMode);
        graphicsBox.setAlignment(Pos.CENTER);

        Button next = createStyledButton("Tiếp theo", "#4CAF50", "#81C784");
        next.setOnAction(e -> {
            ClickSound.play();
            int playerCount = parseInput(playerTextField.getText());
            int botCount = parseInput(botTextField.getText());
            int totalPlayers = playerCount + botCount;

            if (totalPlayers <= 1) {
                warningLabel.setText("Cần ít nhất 2 người chơi để bắt đầu.");
                return;
            }

            if (totalPlayers > gameOption.maxPlayers) {
                warningLabel.setText("Tổng số người chơi vượt quá giới hạn (" + gameOption.maxPlayers + ").");
                return;
            }

            warningLabel.setText("");
            boolean isBasic = basicMode.isSelected();

            GameController controller = GameControllerFactory.create(gameOption, playerCount, botCount);
            GameScene scene = GameSceneFactory.create(gameOption, controller);
            Parent gamePlayParent = scene.createGamePlay(primaryStage, isBasic, controller);
            primaryStage.getScene().setRoot(gamePlayParent);
        });

        Button backButton = createStyledButton("Quay lại", "#f44336", "#e57373");
        backButton.setOnAction(e -> {
            ClickSound.play();
            Parent selectGameRoot = SelectGame.create(primaryStage);
            primaryStage.getScene().setRoot(selectGameRoot);
        });

        inputPane.getChildren().addAll(label, playerTextField, botTextField, maxPlayersLabel,
                graphicsBox, next, backButton, warningLabel);

        StackPane inputRoot = new StackPane();
        inputRoot.setPrefSize(1280, 720);
        inputRoot.setBackground(BackgroundImage.set());
        inputRoot.getChildren().add(inputPane);

        inputRoot.setOnMouseClicked(event -> inputRoot.requestFocus());
        Platform.runLater(() -> inputRoot.requestFocus());

        return inputRoot;
    }

    private static TextField createStyledTextField(String prompt) {
        TextField textField = new TextField();
        textField.setPromptText(prompt);
        textField.setMaxWidth(200);
        textField.setStyle("""
            -fx-font-size: 14px;
            -fx-background-radius: 10;
            -fx-border-radius: 10;
            -fx-border-color: white;
            -fx-border-width: 2;
            -fx-padding: 5 10 5 10;
        """);

        textField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            return newText.matches("\\d*") ? change : null;
        }));

        return textField;
    }

    private static Button createStyledButton(String text, String colorStart, String colorEnd) {
        Button button = new Button(text);
        button.setPrefSize(150, 40);
        button.setStyle(String.format("""
            -fx-background-color: linear-gradient(to right, %s, %s);
            -fx-text-fill: white;
            -fx-font-size: 16px;
            -fx-font-weight: bold;
            -fx-background-radius: 20;
            -fx-border-radius: 20;
            -fx-border-color: white;
            -fx-border-width: 2;
        """, colorStart, colorEnd));
        return button;
    }

    public static int parseInput(String input) {
        if (input == null || input.trim().isEmpty())
            return 0;
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
