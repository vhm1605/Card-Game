// src/gamescene/ScoringGameScene.java
package main.java.edu.hust.cardgame.ui.view;

import main.java.edu.hust.cardgame.logic.bacay.BaCay;
import main.java.edu.hust.cardgame.assets.imageaction.CardImage;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import main.java.edu.hust.cardgame.model.StandardCard;
import main.java.edu.hust.cardgame.model.Player;
import main.java.edu.hust.cardgame.assets.soundaction.ClickSound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A little UI wrapper around any game that
 *   1) is a CardGame<C> (so we can deal/reset/getPlayers)
 *   2) is a ScoringGame<C> (so we can getScoreFor/getRanking)
 */
public class ScoringGameScene<T extends BaCay> extends AbstractGamePlayScene<StandardCard, T> {
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
    Map<Integer, Integer> scores = new HashMap<>();
    private int turn = 0;
    public ScoringGameScene(T game) {
        super(game);
    }
    @Override
    protected void updateScene() {
        centerPane.getChildren().clear();
        playerCardShow();
        createActionButtons();
    }
    private void playerCardShow() {
        List<Player<StandardCard>> players = game.getPlayers();
        StackPane[] destinations = {bottomPane, rightPane, topPane, leftPane};
        String[] playerNames = {"Player 1", "Player 2", "Player 3", "Player 4"};

        for (int i = 0; i < game.getPlayers().size(); i++) {

            destinations[i].getChildren().clear();
            addPlayerNameLabel(destinations[i], playerNames[i], i);
            Player<StandardCard> p = game.getPlayers().get(i);
            List<StandardCard> cardofp = p.getAllCards();
            for(int j = 0; j < 3; j++)
            {
                if(i<turn) destinations[i].getChildren().add(CardImage.create(j, 3, cardofp.get(j),  isBasic));
                else destinations[i].getChildren().add(CardImage.create(j, 3,  isBasic));
            }
            int score = game.getScoreFor(p);
            scores.put(i+1, score);

        }
    }


    private void createActionButtons() {
        if(turn < game.getPlayers().size()) {
            Button showButton = new Button("Show Card");
            Button backButton = createOutButton();


            // Set button sizes for hit and skip (backButton size set in superclass)
            showButton.setPrefSize(150, 40);
            showButton.setStyle(buttonStyle);
            showButton.setOnAction(e -> {
                turn++;
                updateScene();
            });
            buttonBox = new HBox(20);
            buttonBox.getChildren().addAll(showButton, backButton);
            buttonBox.setPadding(new Insets(10, 0, 10, 0));
            centerPane.getChildren().add(buttonBox);
            StackPane.setAlignment(buttonBox, Pos.BOTTOM_CENTER);  // Position at bottom
            StackPane.setMargin(buttonBox, new Insets(50, 0, 0, 0));
        }
        else
        {
            Button showButton = new Button("Show Results");
            Button backButton = createOutButton();


            // Set button sizes for hit and skip (backButton size set in superclass)
            showButton.setPrefSize(150, 40);
            showButton.setStyle(buttonStyle);
            showButton.setOnAction(e -> {
                ClickSound.play();
                centerPane.getChildren().clear();
                endGame();
            });
            buttonBox = new HBox(20);
            buttonBox.getChildren().addAll(showButton, backButton);
            buttonBox.setPadding(new Insets(10, 0, 10, 0));
            centerPane.getChildren().add(buttonBox);
            StackPane.setAlignment(buttonBox, Pos.BOTTOM_CENTER);  // Position at bottom
            StackPane.setMargin(buttonBox, new Insets(50, 0, 0, 0));
        }
    }

    private void endGame(){
        List<Map.Entry<Integer, Integer>> ranking = new ArrayList<>(scores.entrySet());

// Sắp xếp theo giá trị (score) giảm dần
        ranking.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

// Hiển thị ranking
        String resultText = "Ranking:\n";
        for (int rank = 0; rank < ranking.size(); rank++) {
            int playerIndex = ranking.get(rank).getKey();
            int score = ranking.get(rank).getValue();
            resultText += "Rank " + (rank + 1) + ": Player " + playerIndex + " with score: " + score + "\n";
        }
        Label winnerLabel = new Label(resultText);
        winnerLabel.setFont(Font.font("Arial", 20));
        winnerLabel.setTextFill(Color.WHITE);

        Button newGame = new Button("New Game");
        Button backButton = new Button("Back");


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
