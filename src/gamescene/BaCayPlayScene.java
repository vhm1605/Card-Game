package gamescene;

import gamelogic.BaCay;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import module.Card;
import module.Player;

import imageaction.CardImage;
import javafx.scene.layout.Pane;

import java.util.List;

/*
 Khi nao bao Nhat vs HMinh code,
 hoac sua cai GamePlayScene cho no extend CardGame
 chu khong extend cai TienLen nua
 chu t chiu :)
 */
public class BaCayPlayScene {
    private final BaCay game;
    private Stage stage;
    private boolean isBasic;

    public BaCayPlayScene(BaCay game) {
        this.game = game;
    }

    public Parent createGamePlay(Stage primaryStage, boolean basicMode) {
        this.stage   = primaryStage;
        this.isBasic = basicMode;
        game.startNewGame();   // deal 3 cards each

        HBox root = new HBox(20);
        root.setAlignment(Pos.CENTER);

        // One VBox per player: card‐backs until reveal
        List<Player> players = game.getPlayers();
        for (int pi = 0; pi < players.size(); pi++) {
            VBox playerBox = new VBox(5);
            playerBox.setAlignment(Pos.CENTER);

            // placeholder images
            for (int ci = 0; ci < 3; ci++) {
                ImageView iv = isBasic
                        ? CardImage.createBackOfCard()            // you’ll add a helper here
                        : CardImage.createBackOfCard();           // or whatever “basic vs fancy” means
                playerBox.getChildren().add(iv);
            }

            root.getChildren().add(playerBox);
        }

        Button reveal = new Button("Reveal");
        reveal.setOnAction(e -> doReveal(root));
        VBox container = new VBox(30, root, reveal);
        container.setAlignment(Pos.CENTER);
        return container;
    }

    private void doReveal(Pane root) {
        game.playGame();   // computes all 3‐card scores internally

        root.getChildren().clear();
        List<Player> players = game.getPlayers();
        List<Integer> scores = game.getPlayerScores();    // you’ll add this helper

        for (int i = 0; i < players.size(); i++) {
            HBox h = new HBox(5);
            h.setAlignment(Pos.CENTER);

            // show their actual cards
            for (Card c : players.get(i).getHand().getAllCards()) {
                ImageView iv = CardImage.createFaceOfCard(c);
                h.getChildren().add(iv);
            }
            // show the number
            Label lbl = new Label("→ " + scores.get(i));
            h.getChildren().add(lbl);

            root.getChildren().add(h);
        }

        // show ranking
        Label ranking = new Label("Ranking: " + game.getPlayerRanking());
        root.getChildren().add(ranking);

        // optional: “Play Again” button
    }
}
