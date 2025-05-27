package main.java.edu.hust.cardgame.ui.view;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import main.java.edu.hust.cardgame.assets.soundaction.ClickSound;
import main.java.edu.hust.cardgame.controller.BaCayGameController;

import java.util.*;

public class BaCayGameScene extends GameScene {
    private final BaCayGameController controller;
    private final Map<Integer, Integer> scores = new HashMap<>();
    private int turn = 0;

    public BaCayGameScene(BaCayGameController controller) {
        super();
        this.controller = controller;
    }

    @Override
    protected void updateScene() {
        centerPane.getChildren().clear();

        uiLayer.getChildren().removeIf(n -> {
            Object tag = n.getUserData();
            return "buttonBar".equals(tag) || "ranking".equals(tag);
        });

        int total = controller.getPlayerCount();

        for (int i = 0; i < total; i++) {
            StackPane seat = playerPanes.get(i);
            seat.getChildren().clear();
            addPlayerNameLabel(seat, controller.getPlayerName(i), i);

            if (i<turn) {
                controller.ensureInRound();
                List<ImageView> cardViews = controller.getVisibleCards(i, isBasic, card -> {
                    return 0;
                });
                seat.getChildren().addAll(cardViews);
            } else {
                List<ImageView> hidden = controller.getHiddenCardImages(i, isBasic);
                seat.getChildren().addAll(hidden);
            }

            int pts = i < turn ? controller.getScore(i) : 0;
            scores.put(i, pts);


        }

        HBox buttonBox = new HBox(20);
        buttonBox.setUserData("buttonBar");
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 20, 0));

        if (turn!=total) {
            Button reveal = new Button("Show");
            reveal.setPrefSize(150, 40);
            reveal.setStyle(buttonStyle);
            reveal.setOnAction(e -> {
                ClickSound.play();
                turn+=1;
                updateScene();
            });
            buttonBox.getChildren().setAll(reveal, createOutButton());
        } else {
            Button results = new Button("Show Results");
            results.setPrefSize(150, 40);
            results.setStyle(buttonStyle);
            results.setOnAction(e -> {
                ClickSound.play();
                renderResults();
            });
            buttonBox.getChildren().setAll(results, createOutButton());
        }

        uiLayer.getChildren().add(buttonBox);
        StackPane.setAlignment(buttonBox, Pos.BOTTOM_CENTER);
    }

    private void renderResults() {
        uiLayer.getChildren().removeIf(n -> "ranking".equals(n.getUserData()));
        uiLayer.getChildren().removeIf(n -> "buttonBar".equals(n.getUserData()));

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

        Button newGame = new Button("New Game");
        newGame.setPrefSize(150, 40);
        newGame.setStyle(buttonStyle);
        newGame.setOnAction(e -> {
            ClickSound.play();
            turn = 0;
            scores.clear();
            updateScene();
        });

        Button out = createOutButton();

        HBox ctrls = new HBox(20, newGame, out);
        ctrls.setAlignment(Pos.CENTER);
        ctrls.setPadding(new Insets(20, 0, 0, 0));

        VBox box = new VBox(10, summary, ctrls);
        box.setAlignment(Pos.CENTER);
        box.setUserData("ranking");
        uiLayer.getChildren().add(box);

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