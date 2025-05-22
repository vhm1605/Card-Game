package main.java.edu.hust.cardgame.ui.view;

import main.java.edu.hust.cardgame.logic.tienlen.TienLen; // Your specific TienLen game logic class
import main.java.edu.hust.cardgame.assets.imageaction.CardImage;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane; // Though inherited, useful for local vars or casting
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import main.java.edu.hust.cardgame.ai.AIPlayer;
import main.java.edu.hust.cardgame.model.StandardCard;
import main.java.edu.hust.cardgame.model.CardCollection;
import main.java.edu.hust.cardgame.model.Player;
import main.java.edu.hust.cardgame.model.PlayerState;
import main.java.edu.hust.cardgame.assets.soundaction.ClickSound; // Assuming this is correctly imported in AbstractGamePlayScene as well

import java.util.List;

public class TienLenGameScene<T extends TienLen> extends AbstractGamePlayScene<StandardCard, T> {
    private HBox buttonBox;
    private String buttonStyle = """
            -fx-background-color: linear-gradient(to right, #FF5722, #E64A19);
            -fx-text-fill: white;
            -fx-font-size: 16px;
            -fx-font-weight: bold;
            -fx-background-radius: 20;
            -fx-border-radius: 20;
            -fx-border-color: white;
            -fx-border-width: 2;
        """;

    public TienLenGameScene(T game) {
        super(game);
    }



    private void createActionButtons(boolean waiting) {
        Button hitButton = new Button("Hit");
        Button skipButton = new Button("Skip");
        Button backButton = createOutButton();  // Use superclass method


        // Set button sizes for hit and skip (backButton size set in superclass)
        hitButton.setPrefSize(150, 40);
        skipButton.setPrefSize(150, 40);

        // Unified button styling for hit and skip (backButton styled in superclass)

        hitButton.setStyle(buttonStyle);
        skipButton.setStyle(buttonStyle);
        // Button actions
        hitButton.setOnAction(e -> {
            ClickSound.play();
            if (game.isValidPlay()) {
                game.playGame();
                if (game.isGameOver()) {
                    endGame();
                } else {
                    game.getSelectedCards().empty();
                    updateScene();
                }
            }
        });

        skipButton.setOnAction(e -> {
            ClickSound.play();
            game.passTurn();
            updateScene();
        });

        // backButton action already set in createBackButton()

        // Create HBox and add all buttons
        buttonBox = new HBox(20);  // Spacing between buttons
        if(waiting) {

            Button showCard = new Button("Show your Cards");
            showCard.setOnAction(e -> {

                ClickSound.play();
                centerPane.getChildren().clear();  // Clear centerPane for update
                playerCardShow(false);
                lastPlayCardShow();
                createActionButtons(false);  // Re-add buttons
            });
            showCard.setPrefSize(200, 40);
            showCard.setStyle(buttonStyle);
            buttonBox.getChildren().addAll(showCard, backButton);
        }
        else buttonBox.getChildren().addAll(skipButton, hitButton, backButton);
        buttonBox.setPadding(new Insets(10, 0, 10, 0));
        centerPane.getChildren().add(buttonBox);
        StackPane.setAlignment(buttonBox, Pos.BOTTOM_CENTER);  // Position at bottom
        StackPane.setMargin(buttonBox, new Insets(50, 0, 0, 0)); // Add 20px bottom margin
    }



    @Override
    protected void updateScene() {
        if(pressBack == true) return;
        centerPane.getChildren().clear();  // Clear centerPane for update
        if (game.getCurrentPlayer() instanceof AIPlayer<?, ?>) {
            playerCardShow(false);
            lastPlayCardShow();
            createActionButtons(false);  // Re-add buttons
            ifAiTurn();
        }
        else {
            playerCardShow(true);
            lastPlayCardShow();
            createActionButtons(true);
        }
    }

    private void playerCardShow(boolean waiting) {
        List<Player<StandardCard>> players = game.getPlayers();
        StackPane[] destinations = { bottomPane, rightPane, topPane, leftPane };
        String[] playerNames = {"Player 1", "Player 2", "Player 3", "Player 4"};

        for (int i = 0; i < players.size(); i++) {
            destinations[i].getChildren().clear();
            addPlayerNameLabel(destinations[i], playerNames[i], i);

            if (i == game.getCurrentPlayerIndex() && !waiting) {
                while (game.getCurrentPlayer().getState() != PlayerState.IN_ROUND) {
                    game.moveToNextPlayer();
                }
                showPlayerCards(i, destinations[i]);
            } else {
                showHiddenCards(i, destinations[i]);
            }
        }
    }

    private void showPlayerCards(int playerIndex, StackPane pane) {
        Player<StandardCard> player = game.getPlayers().get(playerIndex);
        int handSize = player.getHandSize();

        if (player instanceof AIPlayer) {
            for (int j = 0; j < handSize; j++) {
                pane.getChildren().add(CardImage.create(j, handSize, isBasic));
            }
        } else {
            for (int j = 0; j < handSize; j++) {
                StandardCard card = player.getAllCards().get(j);
                ImageView cardImg = CardImage.create(j, handSize, card, isBasic);

                cardImg.setOnMouseClicked(e -> {
                    ClickSound.play();
                    if (game.getSelectedCards().getAllCards().contains(card)) {
                        game.deselectCard(card);
                        cardImg.setTranslateY(0);
                    } else {
                        game.selectCard(card);
                        cardImg.setTranslateY(-10);
                    }
                });

                pane.getChildren().add(cardImg);
            }
        }
    }
    private void showHiddenCards(int playerIndex, StackPane pane) {
        Player<StandardCard> player = game.getPlayers().get(playerIndex);
        int handSize = player.getHandSize();

        for (int j = 0; j < handSize; j++) {
            pane.getChildren().add(CardImage.create(j, handSize, isBasic));
        }
    }


    private void lastPlayCardShow() {
        CardCollection<StandardCard> lastPlay = game.getLastPlayedCards();
        centerPane.getChildren().clear();
        for (int i = 0; i < lastPlay.getSize(); i++) {
            centerPane.getChildren().add(CardImage.create(i, lastPlay.getSize(), lastPlay.getCardAt(i), isBasic));
        }
    }

    private void ifAiTurn() {
        if (game.getCurrentPlayer() instanceof AIPlayer<?, ?>) {
            int preplayer = game.getCurrentPlayerIndex();
            PauseTransition pause = new PauseTransition();
            pause.setOnFinished(e -> {
                AIPlayer<StandardCard, T> tempBot = (AIPlayer<StandardCard, T>) game.getCurrentPlayer();
                ClickSound.play();
                tempBot.makeMove(game);
                if (game.isGameOver()) {

                    StackPane[] destinations = { bottomPane, rightPane, topPane, leftPane };

                    destinations[preplayer].getChildren().clear();

                    endGame();
                } else {
                    updateScene();
                }
            });
            pause.play();
        }
    }

    private void endGame() {
        lastPlayCardShow();

        String resultText = "Ranking:\n" + game.playerRankingToString();
        Label winnerLabel = new Label(resultText);
        winnerLabel.setFont(Font.font("Arial", 20));
        winnerLabel.setTextFill(Color.WHITE);

        Button newGame = new Button("New Game");
        Button backButton = new Button("Back");


        newGame.setStyle(buttonStyle);
        backButton.setStyle(buttonStyle);

        newGame.setOnAction(e -> {
            ClickSound.play();
            updateScene();
        });
        backButton.setOnAction(e -> handleBackAction());

        HBox buttonBox = new HBox(20);
        buttonBox.getChildren().addAll(newGame, backButton);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(winnerLabel, buttonBox);
        vbox.setPadding(new Insets(20));
        centerPane.getChildren().addAll(vbox);

        game.resetGame();
    }
}
