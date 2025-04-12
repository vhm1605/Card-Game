package gamescene;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import controller.GameSceneController;
import gamelogic.TienLen;
import imageaction.CardImage;
import imageaction.BackgroundImage;
import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import module.AIPlayer;
import module.Card;
import module.CardCollection;
import module.Player;
import module.PlayerState;
import soundaction.ClickSound;

public class GamePlayScene<T extends TienLen> {
    private T gameType;
    private StackPane centerPane;
    private StackPane bottomPane;
    private StackPane topPane;
    private StackPane leftPane;
    private StackPane rightPane;
    private HBox buttonBox1;
    private Stage primaryStage;

    public GamePlayScene(T gameType) {
        this.gameType = gameType;
    }

    public Parent createGamePlayParent(Stage primaryStage) {
        this.primaryStage = primaryStage;
        try {
            URL fxmlLocation = getClass().getResource("GamePlayScene.fxml");
            System.out.println("GamePlayScene FXML loaded successfully");
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
            createActionButtons();
            createCenterPack();

            // Add this line to initialize the game state immediately
            updateScene(centerPane, bottomPane, topPane, leftPane, rightPane);

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

    private void createActionButtons() {
        Button hitButton = new Button("Hit");
        Button skipButton = new Button("Skip");
        Button backButton = new Button("Back");

        hitButton.setPrefSize(150, 40);
        skipButton.setPrefSize(150, 40);
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

        hitButton.setStyle(buttonStyle);
        skipButton.setStyle(buttonStyle);
        backButton.setStyle(buttonStyle);

        buttonBox1 = new HBox(20);
        buttonBox1.getChildren().addAll(skipButton, hitButton, backButton);
        buttonBox1.setPadding(new Insets(10, 0, 10, 0));

        hitButton.setOnAction(e -> {
            if (gameType.isValidPlay()) {
                hit();
            }
        });

        skipButton.setOnAction(e -> skip());
        backButton.setOnAction(e -> handleBackAction());

        centerPane.getChildren().add(buttonBox1);
    }

    private void hit() {
        ClickSound.play();
        gameType.playGame();

        if (gameType.isGameOver()) {
            endGame();
        } else {
            gameType.getSelectedCards().empty();
            updateScene(centerPane, bottomPane, topPane, leftPane, rightPane);
        }
    }

    private void skip() {
        ClickSound.play();
        gameType.passTurn();
        updateScene(centerPane, bottomPane, topPane, leftPane, rightPane);
    }

    private void handleBackAction() {
        ClickSound.play();
        Parent selectGameRoot = SelectGame.create(primaryStage);
        primaryStage.getScene().setRoot(selectGameRoot);
    }

    private void createCenterPack() {
        ImageView cardImage = CardImage.create(0, 0);
        cardImage.setOnMouseClicked(e -> {
            ClickSound.play();
            updateScene(centerPane, bottomPane, topPane, leftPane, rightPane);
        });
        centerPane.getChildren().add(cardImage);
    }

    public void updateScene(StackPane centerPane, StackPane bottomPane, StackPane topPane,
                            StackPane leftPane, StackPane rightPane) {

        playerCardShow();
        lastPlayCardShow();
        ifAiTurn();
        createActionButtons();
    }

    private void playerCardShow() {
        List<Player> players = gameType.getPlayers();
        StackPane[] destinations = { bottomPane, rightPane, topPane, leftPane };

        for (int i = 0; i < players.size(); i++) {
            destinations[i].getChildren().clear();
            for (int j = 0; j < players.get(i).getHandSize(); j++) {
                Card card = players.get(i).getAllCards().get(j);
                int size = players.get(i).getHandSize();

                if (i == gameType.getCurrentPlayerIndex()) {
                    while (gameType.getCurrentPlayer().getState() != PlayerState.IN_ROUND) {
                        gameType.moveToNextPlayer();
                    }

                    if (gameType.getCurrentPlayer() != null && gameType.getCurrentPlayer().getAllCards() != null) {
                        if (gameType.getCurrentPlayer() instanceof AIPlayer) {
                            destinations[i].getChildren().add(CardImage.create(j, size));
                        } else {
                            ImageView cardImg = CardImage.create(j, size, card);
                            cardImg.setOnMouseClicked(e -> {
                                ClickSound.play();
                                if (gameType.getSelectedCards().getAllCards().contains(card)) {
                                    gameType.deselectCard(card);
                                    cardImg.setTranslateY(0);
                                } else {
                                    gameType.selectCard(card);
                                    cardImg.setTranslateY(-10);
                                }
                            });

                            destinations[i].getChildren().add(cardImg);
                        }
                    }
                } else {
                    destinations[i].getChildren().add(CardImage.create(j, size));
                }
            }
        }
    }

    private void lastPlayCardShow() {
        CardCollection lastPlay = gameType.getLastPlayedCards();
        centerPane.getChildren().clear();
        for (int i = 0; i < lastPlay.getSize(); i++) {
            centerPane.getChildren().add(CardImage.create(i, lastPlay.getSize(), lastPlay.getCardAt(i)));
        }
    }

    private void ifAiTurn() {
        if (gameType.getCurrentPlayer() instanceof AIPlayer) {
            PauseTransition pause = new PauseTransition();
            pause.setOnFinished(e -> {
                int k = 0;
                AIPlayer tempBot = (AIPlayer) gameType.getCurrentPlayer();
                if (tempBot.makeMove(gameType)) {
                    if (gameType.isGameOver()) {
                        k = 1;
                        endGame();
                    }
                }
                if (k == 0) {
                    updateScene(centerPane, bottomPane, topPane, leftPane, rightPane);
                }
            });
            pause.play();
        }
    }

    private void endGame() {
        lastPlayCardShow();

        String resultText = "Thứ tự xếp hạng chiến thắng: " + gameType.playerRankingToString();
        Label winnerLabel = new Label(resultText);
        winnerLabel.setFont(Font.font("Arial", 20));
        winnerLabel.setTextFill(Color.WHITE);

        Button newGame = new Button("New Game");
        newGame.setStyle("""
            -fx-background-color: linear-gradient(to bottom, #FF5722, #E64A19);
            -fx-text-fill: white;
            -fx-font-size: 16px;
            -fx-font-weight: bold;
            -fx-padding: 10 20 10 20;
            -fx-background-radius: 15;
            -fx-border-radius: 15;
            -fx-border-color: white;
            -fx-border-width: 2px;
        """);

        newGame.setOnAction(e -> updateScene(centerPane, bottomPane, topPane, leftPane, rightPane));

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(winnerLabel, newGame);
        centerPane.getChildren().addAll(vbox);

        gameType.resetGame();
    }
}
