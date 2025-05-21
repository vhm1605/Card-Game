// src/gamescene/ScoringGameScene.java
package gamescene;

import gamelogic.CardGame;
import module.CardType;
import module.Player;
import module.ScoringGame;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.stream.Collectors;

/**
 * A little UI wrapper around any game that
 *   1) is a CardGame<C> (so we can deal/reset/getPlayers)
 *   2) is a ScoringGame<C> (so we can getScoreFor/getRanking)
 */
public class ScoringGameScene<
        C extends CardType,
        G extends CardGame<C> & ScoringGame<C>
        > {
    private final G game;

    public ScoringGameScene(G game) {
        this.game = game;
    }

    public Parent createScene(Stage primaryStage) {
        // (re)deal & score
        game.startNewGame();
        // if you prefer to split deal/playGame, you could call game.deal() then game.playGame()
        game.playGame();

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);

        // show each player's hand + their computed score
        for (int i = 0; i < game.getPlayers().size(); i++) {
            Player<C> p = game.getPlayers().get(i);
            String hand = p.getAllCards().stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(" "));
            int score = game.getScoreFor(p);

            Label lbl = new Label(
                    "Player " + (i+1) + ": " + hand + " → score = " + score
            );
            root.getChildren().add(lbl);
        }

        Button newRound = new Button("New Round");
        newRound.setOnAction(e -> {
            // you can either call resetGame() or simply re‐start:
            game.resetGame();
            Parent next = createScene(primaryStage);
            primaryStage.getScene().setRoot(next);
        });
        root.getChildren().add(newRound);

        return root;
    }
}
