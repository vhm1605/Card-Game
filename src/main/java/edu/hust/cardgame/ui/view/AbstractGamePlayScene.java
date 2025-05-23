package main.java.edu.hust.cardgame.ui.view;

import javafx.application.Platform;
import main.java.edu.hust.cardgame.assets.imageaction.BackgroundImage;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import main.java.edu.hust.cardgame.assets.soundaction.ClickSound;
import main.java.edu.hust.cardgame.model.CardType;
import main.java.edu.hust.cardgame.core.CardGame;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
//import javafx.scene.layout.Pane;
//import game.engine.CardGame; // Placeholder, replace with your actual game engine's base class/interface
//import game.engine.CardType; // Placeholder

public abstract class AbstractGamePlayScene<C extends CardType, G extends CardGame<C>> {
    protected Stage primaryStage;
    protected boolean isBasic;
    protected boolean pressBack = false;

    protected G game;

    // Dynamic map of up-to-8 player seats
    protected final Map<Integer, StackPane> playerPanes = new HashMap<>();

    // Injected via FXML
    protected StackPane centerPane;
    protected Pane uiLayer;

    protected StackPane bottomPane;
    protected StackPane rightPane;
    protected StackPane topPane;
    protected StackPane leftPane;

    public AbstractGamePlayScene(G game) {
        this.game = game;
    }

    public Parent createGamePlay(Stage primaryStage, boolean isBasic) {
        this.primaryStage = primaryStage;
        this.isBasic = isBasic;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/java/edu/hust/cardgame/ui/fxml/GamePlayScene.fxml"));
            Parent root = loader.load();
            GameSceneController ctrl = loader.getController();

            // grab the injected panes
            this.centerPane = ctrl.getCenterPane();
            this.uiLayer = ctrl.getUiLayer();

            uiLayer.setPickOnBounds(false);

            uiLayer.prefWidthProperty().bind(((Region) root).widthProperty());
            uiLayer.prefHeightProperty().bind(((Region) root).heightProperty());

            // set the background
            ctrl.getBorderPane().setBackground(isBasic ? new Background(new BackgroundFill(Color.DARKGREEN, null, null))
                    : BackgroundImage.set("/main/resources/card/backgroundgameplay.png"));

            // 1) create empty seats
            int count = Math.min(game.getPlayers().size(), 8);
            for (int i = 0; i < count; i++) {
                StackPane seat = new StackPane();
                seat.setPrefSize(160, 160);

                playerPanes.put(i, seat);
                uiLayer.getChildren().add(seat);
                seat.setPickOnBounds(false);

            }

            if (playerPanes.size() > 0)
                topPane = playerPanes.get(0); // seat 0 – TOP
            if (playerPanes.size() > 1)
                rightPane = playerPanes.get(1); // seat 1 – RIGHT
            if (playerPanes.size() > 2)
                bottomPane = playerPanes.get(2); // seat 2 – BOTTOM
            if (playerPanes.size() > 3)
                leftPane = playerPanes.get(3);

            // 2) whenever the layer resizes, reposition them
            uiLayer.widthProperty().addListener(o -> layoutSeats());
            uiLayer.heightProperty().addListener(o -> layoutSeats());

            // And run once on startup:
            Platform.runLater(() -> {
                layoutSeats();
                updateScene();
            });

            return root;
        } catch (IOException ex) {
            ex.printStackTrace();
            return new StackPane(new Label("Failed to load UI"));
        }
    }

    private void layoutSeats() {
        double w = uiLayer.getWidth();
        double h = uiLayer.getHeight();
        double cx = w / 2, cy = h / 2;
        int count = playerPanes.size();
        if (count == 0)
            return;

        double seatSize = (count <= 4 ? 160 : count <= 6 ? 140 : 120);

        double marginX = 60;
        double marginY = 30;

        double radiusX = w / 2 - seatSize / 2 - marginX;
        double radiusY = h / 2 - seatSize / 2 - marginY;

        for (int i = 0; i < count; i++) {
            StackPane seat = playerPanes.get(i);

            seat.setPrefSize(seatSize, seatSize);

            double angle = 2 * Math.PI * i / count - Math.PI / 2;
            double x = cx + radiusX * Math.cos(angle) - seatSize / 2;
            double y = cy + radiusY * Math.sin(angle) - seatSize / 2;
            seat.setLayoutX(x);
            seat.setLayoutY(y);
        }
    }

    protected Button createOutButton() {
        Button btn = new Button("Out");
        btn.setPrefSize(150, 40);
        btn.setStyle("-fx-background-color: linear-gradient(to right, #FF5722, #E64A19);" + "-fx-text-fill: white;"
                + "-fx-font-size: 16px;" + "-fx-font-weight: bold;" + "-fx-background-radius: 20;"
                + "-fx-border-radius: 20;" + "-fx-border-color: white;" + "-fx-border-width: 2;");
        btn.setOnAction(e -> {
            ClickSound.play();
            pressBack = true;
            Parent select = SelectGame.create(primaryStage);
            primaryStage.getScene().setRoot(select);
        });
        return btn;
    }

    protected void addPlayerNameLabel(StackPane seat, String name, int idx) {
        Label lbl = new Label(name);
        lbl.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        /* angle of this seat on the circle */
        double deg = (360.0 * idx / playerPanes.size() - 90 + 360) % 360;

        if (deg >= 45 && deg < 135) { // bottom arc
            StackPane.setAlignment(lbl, Pos.BOTTOM_CENTER);
            StackPane.setMargin(lbl, new Insets(10, 0, 0, 0));

        } else { // top + both side arcs
            StackPane.setAlignment(lbl, Pos.TOP_CENTER);
            StackPane.setMargin(lbl, new Insets(-20, 0, 0, 0));

        }

        seat.getChildren().add(lbl); // add as last child …
        lbl.toFront(); // … and keep it above the cards
    }

    protected void handleBackAction() {
        ClickSound.play();
        pressBack = true;
        Parent selectGameRoot = SelectGame.create(primaryStage);
        primaryStage.getScene().setRoot(selectGameRoot);
    }

    protected abstract void updateScene();
}