package gamescene;

import gamelogic.TienLen;
import imageaction.BackgroundImage;
import imageaction.CardImage;
import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import module.AIPlayer;
import module.Card;
import module.CardCollection;
import module.Player;
import module.PlayerState;
import soundaction.ClickSound;
import javafx.stage.Stage;
import controller.GameSceneController;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class TienLenGameScene extends AbstractGameScene<TienLen> {
    private HBox buttonBox1;
    private StackPane centerPane;
    private StackPane bottomPane;
    private StackPane topPane;
    private StackPane leftPane;
    private StackPane rightPane;

    public TienLenGameScene(TienLen gameLogic, Stage primaryStage, boolean isBasic) {
        super(gameLogic, primaryStage, isBasic);
    }

    @Override
    public Parent createScene() {
        BorderPane root;
        if (!isBasic) {
            try {
                URL fxmlLocation = getClass().getResource("GamePlayScene.fxml");
                FXMLLoader loader = new FXMLLoader(fxmlLocation);
                root = loader.load();
                GameSceneController controller = loader.getController();
                this.centerPane = controller.getCenterPane();
                this.bottomPane = controller.getBottomPane();
                this.topPane = controller.getTopPane();
                this.leftPane = controller.getLeftPane();
                this.rightPane = controller.getRightPane();
                root.setBackground(BackgroundImage.set("/resources/card/backgroundgameplay.png"));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            root = new BorderPane();
            this.centerPane = new StackPane();
            this.bottomPane = new StackPane();
            this.topPane = new StackPane();
            this.leftPane = new StackPane();
            this.rightPane = new StackPane();
            root.setCenter(centerPane);
            root.setBottom(bottomPane);
            root.setTop(topPane);
            root.setLeft(leftPane);
            root.setRight(rightPane);
            centerPane.setPrefSize(269, 200);
            bottomPane.setPrefSize(600, 100);
            topPane.setPrefSize(600, 100);
            leftPane.setPrefSize(200, 200);
            rightPane.setPrefSize(200, 200);
            root.setPrefSize(800, 500);
            root.setBackground(new Background(new BackgroundFill(Color.DARKGREEN, null, null)));
        }
        setPanePadding();
        setupUI();
        return root;
    }

    private void setPanePadding() {
        bottomPane.setPadding(new Insets(10, 0, 10, 0));
        topPane.setPadding(new Insets(10, 0, 10, 0));
        leftPane.setPadding(new Insets(80, 150, 80, 150));
        rightPane.setPadding(new Insets(80, 150, 80, 150));
    }

    @Override
    protected void setupUI() {
        createActionButtons();
        createCenterPack();
    }

    private void createActionButtons() {
        Button hitButton = new Button("Hit");
        Button skipButton = new Button("Skip");
        Button backButton = createBackButton(); // Use the common back button

        hitButton.setPrefSize(150, 40);
        skipButton.setPrefSize(150, 40);
        backButton.setPrefSize(150, 40);

        // Apply styles only to game-specific buttons
        if (!isBasic) {
            String buttonStyle =
                    "-fx-background-color: linear-gradient(to right, #FF5722, #E64A19);" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 16px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 20;" +
                            "-fx-border-radius: 20;" +
                            "-fx-border-color: white;" +
                            "-fx-border-width: 2;";
            hitButton.setStyle(buttonStyle);
            skipButton.setStyle(buttonStyle);
        } else {
            String simpleStyle = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px;";
            hitButton.setStyle(simpleStyle);
            skipButton.setStyle(simpleStyle);
        }

        hitButton.setOnAction(e -> {
            if (gameLogic.isValidPlay()) {
                hit();
            }
        });
        skipButton.setOnAction(e -> skip());

        buttonBox1 = new HBox(20);
        buttonBox1.getChildren().addAll(skipButton, hitButton, backButton);
        buttonBox1.setPadding(new Insets(10, 0, 10, 0));
        centerPane.getChildren().add(buttonBox1);
    }

    private void createCenterPack() {
        ImageView cardImage = CardImage.create(0, 0);
        cardImage.setOnMouseClicked(e -> {
            ClickSound.play();
            updateUI();
        });
        centerPane.getChildren().add(cardImage);
    }

    private void hit() {
        ClickSound.play();
        gameLogic.playGame();
        if (gameLogic.isGameOver()) {
            handleGameOver();
        } else {
            gameLogic.getSelectedCards().empty();
            updateUI();
        }
    }

    private void skip() {
        ClickSound.play();
        gameLogic.passTurn();
        updateUI();
    }

    @Override
    protected void updateUI() {
        playerCardShow();
        lastPlayCardShow();
        ifAiTurn();
    }

    private void playerCardShow() {
        List<Player> players = gameLogic.getPlayers();
        StackPane[] destinations = { bottomPane, rightPane, topPane, leftPane };

        for (int i = 0; i < players.size(); i++) {
            destinations[i].getChildren().clear();
            for (int j = 0; j < players.get(i).getHandSize(); j++) {
                Card card = players.get(i).getAllCards().get(j);
                int size = players.get(i).getHandSize();

                if (i == gameLogic.getCurrentPlayerIndex()) {
                    while (gameLogic.getCurrentPlayer().getState() != PlayerState.IN_ROUND) {
                        gameLogic.moveToNextPlayer();
                    }

                    if (gameLogic.getCurrentPlayer() != null && gameLogic.getCurrentPlayer().getAllCards() != null) {
                        if (gameLogic.getCurrentPlayer() instanceof AIPlayer) {
                            destinations[i].getChildren().add(CardImage.create(j, size));
                        } else {
                            ImageView cardImg = CardImage.create(j, size, card);
                            cardImg.setOnMouseClicked(e -> {
                                ClickSound.play();
                                if (gameLogic.getSelectedCards().getAllCards().contains(card)) {
                                    gameLogic.deselectCard(card);
                                    cardImg.setTranslateY(0);
                                } else {
                                    gameLogic.selectCard(card);
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
        CardCollection lastPlay = gameLogic.getLastPlayedCards();
        centerPane.getChildren().clear();
        centerPane.getChildren().add(buttonBox1);
        for (int i = 0; i < lastPlay.getSize(); i++) {
            centerPane.getChildren().add(CardImage.create(i, lastPlay.getSize(), lastPlay.getCardAt(i)));
        }
    }

    private void ifAiTurn() {
        if (gameLogic.getCurrentPlayer() instanceof AIPlayer) {
            PauseTransition pause = new PauseTransition();
            pause.setOnFinished(e -> {
                int k = 0;
                AIPlayer tempBot = (AIPlayer) gameLogic.getCurrentPlayer();
                if (tempBot.makeMove(gameLogic)) {
                    if (gameLogic.isGameOver()) {
                        k = 1;
                        handleGameOver();
                    }
                }
                if (k == 0) {
                    updateUI();
                }
            });
            pause.play();
        }
    }

    @Override
    protected StackPane getGamePlayPane() {
        return centerPane;
    }

    @Override
    protected String getGameOverText() {
        return "Thứ tự xếp hạng chiến thắng: " + gameLogic.playerRankingToString();
    }

    @Override
    protected void resetGame() {
        gameLogic.resetGame();
        if (!centerPane.getChildren().isEmpty() && centerPane.getChildren().size() > 1) {
            centerPane.getChildren().remove(centerPane.getChildren().size() - 1); // Remove game over UI
        }
        updateUI();
    }

    @Override
    protected void updateFinalGameStateUI() {
        lastPlayCardShow();
    }
}