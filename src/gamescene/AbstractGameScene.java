package gamescene;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import soundaction.ClickSound;

public abstract class AbstractGameScene<T> {
    protected T gameLogic;
    protected Stage primaryStage;
    protected boolean isBasic;

    public AbstractGameScene(T gameLogic, Stage primaryStage, boolean isBasic) {
        this.gameLogic = gameLogic;
        this.primaryStage = primaryStage;
        this.isBasic = isBasic;
    }

    public abstract Parent createScene();

    protected abstract void setupUI();

    protected abstract void updateUI();

    // Create a standardized "Back" button
    protected Button createBackButton() {
        Button backButton = new Button("Back");
        applyButtonStyle(backButton);
        backButton.setOnAction(e -> {
            ClickSound.play();
            Parent selectGameRoot = SelectGame.create(primaryStage);
            primaryStage.getScene().setRoot(selectGameRoot);
        });
        return backButton;
    }

    // Create a "New Game" button with common behavior
    protected Button createNewGameButton() {
        Button newGame = new Button("New Game");
        applyButtonStyle(newGame);
        newGame.setOnAction(e -> {
            ClickSound.play();
            resetGame();
        });
        return newGame;
    }

    // Apply consistent styling to buttons based on isBasic
    private void applyButtonStyle(Button button) {
        if (!isBasic) {
            button.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, #FF5722, #E64A19);" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 16px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-padding: 10 20 10 20;" +
                            "-fx-background-radius: 15;" +
                            "-fx-border-radius: 15;" +
                            "-fx-border-color: white;" +
                            "-fx-border-width: 2px;"
            );
        } else {
            button.setStyle(
                    "-fx-background-color: #4CAF50;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 16px;"
            );
        }
    }

    // Generalized handleGameOver method
    protected void handleGameOver() {
        updateFinalGameStateUI(); // Update the UI with the final game state
        StackPane gamePlayPane = getGamePlayPane();
        String resultText = getGameOverText();

        Label winnerLabel = new Label(resultText);
        winnerLabel.setFont(Font.font("Arial", 20));
        winnerLabel.setTextFill(Color.WHITE);

        Button newGame = createNewGameButton();
        Button backButton = createBackButton();
        HBox buttonBox = new HBox(20, newGame, backButton);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        VBox gameOverBox = new VBox(10, winnerLabel, buttonBox);
        gameOverBox.setPadding(new Insets(20));
        gameOverBox.setStyle("-fx-background-color: rgb(90, 0, 140);");

        gamePlayPane.getChildren().add(gameOverBox); // Add as overlay
    }

    // Abstract method to get the main gameplay pane
    protected abstract StackPane getGamePlayPane();

    // Abstract method to get game-specific game over text
    protected abstract String getGameOverText();

    // Abstract method to reset the game
    protected abstract void resetGame();

    // Abstract method to update UI before showing game over
    protected abstract void updateFinalGameStateUI();
}