package gamescene;

import gamelogic.TienLen; // Your specific TienLen game logic class
import imageaction.CardImage;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node; // For iterating over HBox children
import javafx.scene.Parent; // For return type of SelectGame.create
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane; // Though inherited, useful for local vars or casting
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import module.AIPlayer;
import module.StandardCard;
import module.CardCollection;
import module.Player;
import module.PlayerState;
import soundaction.ClickSound; // Assuming this is correctly imported in AbstractGamePlayScene as well

import java.util.List;

public class TienLenGameScene<T extends TienLen> extends AbstractGamePlayScene<StandardCard, T> {
    private HBox buttonBox;

    public TienLenGameScene(T game) {
        super(game);
    }

    @Override
    protected void createGameSpecificUI() {
        createActionButtons();
        createCenterPack();
    }

    private void createActionButtons() {
        Button hitButton = new Button("Hit");
        Button skipButton = new Button("Skip");
        Button backButton = createOutButton();  // Use superclass method

        // Set button sizes for hit and skip (backButton size set in superclass)
        hitButton.setPrefSize(150, 40);
        skipButton.setPrefSize(150, 40);

        // Unified button styling for hit and skip (backButton styled in superclass)
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

        // Button actions
        hitButton.setOnAction(e -> {
            if (game.isValidPlay()) {
                ClickSound.play();
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
        buttonBox.getChildren().addAll(skipButton, hitButton, backButton);
        buttonBox.setPadding(new Insets(10, 0, 10, 0));
        centerPane.getChildren().add(buttonBox);
        StackPane.setAlignment(buttonBox, Pos.BOTTOM_CENTER);  // Position at bottom
        StackPane.setMargin(buttonBox, new Insets(50, 0, 0, 0)); // Add 20px bottom margin
    }

    private void createCenterPack() {
        ImageView cardImage = CardImage.create(0, 0, isBasic);
        cardImage.setOnMouseClicked(e -> {
            ClickSound.play();
            updateScene();
        });
        centerPane.getChildren().add(cardImage);
    }

    @Override
    protected void updateScene() {
        if(pressBack == true) return;
        centerPane.getChildren().clear();  // Clear centerPane for update
        playerCardShow();
        lastPlayCardShow();
        createActionButtons();  // Re-add buttons
        ifAiTurn();
    }

    private void playerCardShow() {
        List<Player<StandardCard>> players = game.getPlayers();
        StackPane[] destinations = { bottomPane, rightPane, topPane, leftPane };
        String[] playerNames = {"Player 1", "Player 2", "Player 3", "Player 4"};

        for (int i = 0; i < players.size(); i++) {
            destinations[i].getChildren().clear();

            // Create and style the player name label
            Label nameLabel = new Label(playerNames[i]);
            nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
            nameLabel.setPrefWidth(120);

            // Set alignment and margin based on player index
            if (i == 2) { // Player 3 (topPane)
                StackPane.setAlignment(nameLabel, Pos.BOTTOM_CENTER);
                StackPane.setMargin(nameLabel, new Insets(150, 0, 0, 0)); // 10px below cards
            } else if (i == 0) {
                StackPane.setAlignment(nameLabel, Pos.TOP_CENTER);
                StackPane.setMargin(nameLabel, new Insets(0, 0, 140, 0)); // 5px above cards
            } else { // Players 2, 4 (rightPane, leftPane)
                StackPane.setAlignment(nameLabel, Pos.TOP_CENTER);
                StackPane.setMargin(nameLabel, new Insets(80, 0, 5, 0)); // 5px above cards
            }
            destinations[i].getChildren().add(nameLabel);

            // Existing card display logic
            for (int j = 0; j < players.get(i).getHandSize(); j++) {
                StandardCard card = players.get(i).getAllCards().get(j);
                int size = players.get(i).getHandSize();

                if (i == game.getCurrentPlayerIndex()) {
                    while (game.getCurrentPlayer().getState() != PlayerState.IN_ROUND) {
                        game.moveToNextPlayer();
                    }

                    if (game.getCurrentPlayer() != null && game.getCurrentPlayer().getAllCards() != null) {
                        if (game.getCurrentPlayer() instanceof AIPlayer) {
                            destinations[i].getChildren().add(CardImage.create(j, size, isBasic));
                        } else {
                            ImageView cardImg = CardImage.create(j, size, card, isBasic);
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
                            destinations[i].getChildren().add(cardImg);
                        }
                    }
                } else {
                    destinations[i].getChildren().add(CardImage.create(j, size, isBasic));
                }
            }
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
            PauseTransition pause = new PauseTransition();
            pause.setOnFinished(e -> {
                AIPlayer<StandardCard, T> tempBot = (AIPlayer<StandardCard, T>) game.getCurrentPlayer();
                ClickSound.play();
                tempBot.makeMove(game);
                if (game.isGameOver()) {
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

        String resultText = "Thứ tự xếp hạng chiến thắng: " + game.playerRankingToString();
        Label winnerLabel = new Label(resultText);
        winnerLabel.setFont(Font.font("Arial", 20));
        winnerLabel.setTextFill(Color.WHITE);

        Button newGame = new Button("New Game");
        Button backButton = new Button("Back");

        String buttonStyle = """
            -fx-background-color: linear-gradient(to bottom, #FF5722, #E64A19);
            -fx-text-fill: white;
            -fx-font-size: 16px;
            -fx-font-weight: bold;
            -fx-padding: 10 20 10 20;
            -fx-background-radius: 15;
            -fx-border-radius: 15;
            -fx-border-color: white;
            -fx-border-width: 2px;
        """;

        newGame.setStyle(buttonStyle);
        backButton.setStyle(buttonStyle);

        newGame.setOnAction(e -> {
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