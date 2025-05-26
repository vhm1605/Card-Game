// src/gamescene/ScoringGameScene.java
package main.java.edu.hust.cardgame.ui.view;

import javafx.application.Platform;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
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
public class BaCayGameScene<T extends BaCay> extends GameScene<StandardCard, T> {


    private final Map<Integer, Integer> scores = new HashMap<>();
    private int turn = 0;

    public BaCayGameScene(T game) {
        super(game);
    }

    @Override
    protected void updateScene() {
        centerPane.getChildren().clear();

        uiLayer.getChildren().removeIf(n -> {
            Object tag = n.getUserData();
            return "buttonBar".equals(tag) || "ranking".equals(tag);
        });
        List<Player<StandardCard>> players = game.getPlayers();
        int total = players.size();

        // 1) Seat layout: cards + "Player X - Y pts" below
        for (int i = 0; i < total; i++) {
            StackPane seat = playerPanes.get(i);
            seat.getChildren().clear();

            // a) cards row
            HBox cards = new HBox(total > 4 ? 2 : 10);
            cards.setAlignment(Pos.CENTER);
            for (int j = 0; j < 3; j++) {
                ImageView iv = (i < turn) ? CardImage.create(j, 3, players.get(i).getAllCards().get(j), isBasic)
                        : CardImage.create(j, 3, isBasic);
                double scale = total > 4 ? 0.65 : 1.0;
                iv.setFitWidth(80 * scale);
                iv.setFitHeight(100 * scale);
                cards.getChildren().add(iv);
            }

            // b) name+score label
            int pts = (i < turn) ? game.getScoreFor(players.get(i)) : 0;
            scores.put(i, pts);

            Label lbl = new Label(String.format("Player %d – %d pts", i + 1, pts));
            lbl.setTextFill(Color.GOLD);
            lbl.setFont(Font.font(total > 4 ? 14 : 18));
            DropShadow ds = new DropShadow();
            ds.setOffsetY(1);
            ds.setColor(Color.BLACK);
            lbl.setEffect(ds);

            // c) stack vertically
            VBox seatBox = new VBox(5, cards, lbl);
            seatBox.setAlignment(Pos.CENTER);

            seat.getChildren().add(seatBox);
        }

        // 2) Bottom controls
        HBox buttonBox = new HBox(20);
        buttonBox.setUserData("buttonBar");
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 20, 0));

        if (turn < total) {
            Button show = new Button("Show Card");
            styleAndRefresh(show, () -> turn++);
            buttonBox.getChildren().setAll(show, createOutButton());
        } else {
            // Show results only once
            Button results = new Button("Show Results");
            results.setPrefSize(150, 40);
            results.setStyle(buttonStyle);
            results.setOnAction(e -> {
                ClickSound.play();
                renderResults(); // no updateScene() here
            });
            buttonBox.getChildren().setAll(results, createOutButton());
        }

        uiLayer.getChildren().add(buttonBox);
        StackPane.setAlignment(buttonBox, Pos.BOTTOM_CENTER);
    }

    private void styleAndRefresh(Button b, Runnable action) {
        b.setPrefSize(150, 40);
        b.setStyle(buttonStyle);
        b.setOnAction(e -> {
            ClickSound.play();
            action.run();
            updateScene();
        });
    }

    private void renderResults() {

        // 2) Clear only the previous button row
        uiLayer.getChildren().removeIf(n -> "ranking".equals(n.getUserData()));
        uiLayer.getChildren().removeIf(n -> "buttonBar".equals(n.getUserData()));
        // centerPane.getChildren().clear(); // you can clear last-play if you like

        // 3) Build your ranking summary
        List<Map.Entry<Integer, Integer>> rank = new ArrayList<>(scores.entrySet());
        rank.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        StringBuilder sb = new StringBuilder("Final Ranking:\n");
        for (int i = 0; i < rank.size(); i++) {
            var e = rank.get(i);
            sb.append(String.format("%d. Player %d – %d pts%n", i + 1, e.getKey() + 1, e.getValue()));
        }

        Label summary = new Label(sb.toString());
        summary.setTextFill(Color.WHITE);
        summary.setFont(Font.font(20));
//		summary.setTextAlignment(TextAlignment.CENTER);

        // 4) Build controls
        Button newGame = new Button("New Game");
        styleAndRefresh(newGame, () -> {
            turn = 0;
            scores.clear();
        });

        Button out = createOutButton();
        // (this one navigates back immediately)

        HBox ctrls = new HBox(20, newGame, out);
        ctrls.setAlignment(Pos.CENTER);
        ctrls.setPadding(new Insets(20, 0, 0, 0));

        VBox box = new VBox(10, summary, ctrls);
        box.setAlignment(Pos.CENTER);
        box.setUserData("ranking");
        uiLayer.getChildren().add(box);

        if (!uiLayer.getChildren().contains(box)) {
            uiLayer.getChildren().add(box);

        }

        Runnable centerBox = () -> {
            double x = (uiLayer.getWidth() - box.getBoundsInParent().getWidth()) / 2;
            double y = (uiLayer.getHeight() - box.getBoundsInParent().getHeight()) / 2;
            box.setLayoutX(Math.max(x, 0));
            box.setLayoutY(Math.max(y, 0));
        };

        Platform.runLater(centerBox);

        box.layoutBoundsProperty().addListener((o, ov, nv) -> centerBox.run());

        uiLayer.widthProperty().addListener((o, oldV, newV) -> centerBox.run());
        uiLayer.heightProperty().addListener((o, oldV, newV) -> centerBox.run());
    }
}