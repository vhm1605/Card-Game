package main.java.edu.hust.cardgame.ui.view;

import main.java.edu.hust.cardgame.controller.TienLenGameController;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.List;

public class TienLenGameScene extends GameScene {
    private HBox buttonBox;
    private final TienLenGameController controller;

    public TienLenGameScene(TienLenGameController controller) {
        super();
        this.controller = controller;
    }

    private void createActionButtons(boolean waitToPressShow) {
        uiLayer.getChildren().removeIf(n -> "overlay".equals(n.getUserData()));
        centerPane.getChildren().removeIf(n -> "overlay".equals(n.getUserData()));

        Button hitButton = new Button("Hit");
        Button skipButton = new Button("Skip");
        Button backButton = createOutButton();

        hitButton.setPrefSize(150, 40);
        skipButton.setPrefSize(150, 40);

        hitButton.setStyle(buttonStyle);
        skipButton.setStyle(buttonStyle);

        hitButton.setOnAction(e -> {
            controller.playClickSound();
            if (controller.isValidPlay()) {
                controller.play();
                if (controller.isGameOver()) {
                    endGame();
                } else {
                    controller.clearSelectedCards();
                    updateScene();
                }
            }
        });

        skipButton.setOnAction(e -> {
            controller.playClickSound();
            controller.passTurn();
            updateScene();
        });

        buttonBox = new HBox(20);
        buttonBox.setUserData("buttonBar");
        buttonBox.setMouseTransparent(false);

        if (waitToPressShow) {
            Button showCard = new Button("Show your Cards");
            showCard.setOnAction(e -> {
                controller.playClickSound();
                centerPane.getChildren().clear();
                showPlayerCards(false);
                showLastPlayedCards();
                createActionButtons(false);
            });
            showCard.setPrefSize(200, 40);
            showCard.setStyle(buttonStyle);
            buttonBox.getChildren().addAll(showCard, backButton);
        } else {
            buttonBox.getChildren().addAll(skipButton, hitButton, backButton);
        }

        buttonBox.setPadding(new Insets(10, 0, 10, 0));
        buttonBox.setUserData("overlay");
        uiLayer.getChildren().add(buttonBox);
        StackPane.setAlignment(buttonBox, Pos.BOTTOM_CENTER);
        StackPane.setMargin(buttonBox, new Insets(50, 0, 0, 0));
    }

    @Override
    protected void updateScene() {
        if (pressBack) return;
        uiLayer.getChildren().removeIf(n -> "overlay".equals(n.getUserData()));
        centerPane.getChildren().clear();

        if (controller.isCurrentPlayerAI()) {
            showPlayerCards(false);
            showLastPlayedCards();
            createActionButtons(false);

            int currentIndex = controller.getCurrentPlayerIndex();
            PauseTransition pause = new PauseTransition();
            pause.setOnFinished(e -> {
                controller.makeAIMove();
                if (controller.isGameOver()) {
                    StackPane seat = playerPanes.get(currentIndex);
                    seat.getChildren().clear();
                    addPlayerNameLabel(seat, controller.getPlayerName(currentIndex), currentIndex);
                    endGame();
                } else {
                    updateScene();
                }
            });
            pause.play();
        } else {
            showPlayerCards(true);
            showLastPlayedCards();
            createActionButtons(true);
        }
    }

    private void showPlayerCards(boolean waitToPressShow) {
        int totalPlayers = controller.getPlayerCount();
        for (int i = 0; i < totalPlayers; i++) {
            StackPane seat = playerPanes.get(i);
            seat.getChildren().clear();
            addPlayerNameLabel(seat, controller.getPlayerName(i), i);

            if (i == controller.getCurrentPlayerIndex() && !waitToPressShow && !controller.isCurrentPlayerAI()) {
                controller.ensureInRound();
                List<ImageView> cardViews = controller.getVisibleCards(i, isBasic, card -> {
                    controller.playClickSound();
                    if (controller.isCardSelected(card)) {
                        controller.deselectCard(card);
                        return 0;
                    } else {
                        controller.selectCard(card);
                        return -10;
                    }
                });
                seat.getChildren().addAll(cardViews);
            } else {
                List<ImageView> hidden = controller.getHiddenCardImages(i, isBasic);
                seat.getChildren().addAll(hidden);
            }
        }
    }

    private void showLastPlayedCards() {
        centerPane.getChildren().clear();
        List<ImageView> cards = controller.getLastPlayedCardImages(isBasic);
        centerPane.getChildren().addAll(cards);
    }

    private void endGame() {
        showLastPlayedCards();
        String resultText = "Ranking:\n" + controller.getRankingText();

        Label winnerLabel = new Label(resultText);
        winnerLabel.setFont(Font.font("Arial", 20));
        winnerLabel.setTextFill(Color.WHITE);

        Button newGame = new Button("New Game");
        Button backButton = new Button("Back");

        newGame.setStyle(buttonStyle);
        backButton.setStyle(buttonStyle);

        newGame.setOnAction(e -> {
            controller.playClickSound();
            updateScene();
        });

        backButton.setOnAction(e -> handleBackAction());

        HBox buttonBox = new HBox(20, newGame, backButton);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        uiLayer.getChildren().removeIf(n -> !playerPanes.containsValue(n));

        VBox vbox = new VBox(10, winnerLabel, buttonBox);
        vbox.setPadding(new Insets(20));
        vbox.setUserData("overlay");
        vbox.setMouseTransparent(false);
        uiLayer.getChildren().add(vbox);

        controller.resetGame();
    }
}
