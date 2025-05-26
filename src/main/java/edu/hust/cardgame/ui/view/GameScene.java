package main.java.edu.hust.cardgame.ui.view;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import main.java.edu.hust.cardgame.controller.GameController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class GameScene {
    protected Stage primaryStage;
    protected boolean isBasic;
    protected boolean pressBack = false;

    protected GameController controller;

    protected String buttonStyle;
    protected final Map<Integer, StackPane> playerPanes = new HashMap<>();

    protected StackPane centerPane;
    protected Pane uiLayer;

    protected StackPane topPane, bottomPane, leftPane, rightPane;

    public GameScene() {}

    public Parent createGamePlay(Stage primaryStage, boolean isBasic, GameController controller) {
        this.primaryStage = primaryStage;
        this.isBasic = isBasic;
        this.controller = controller;

        this.buttonStyle = isBasic ? BASIC_BUTTON_STYLE : FANCY_BUTTON_STYLE;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/java/edu/hust/cardgame/ui/fxml/GamePlayScene.fxml"));
            Parent root = loader.load();
            GameSceneController ctrl = loader.getController();

            this.centerPane = ctrl.getCenterPane();
            this.uiLayer = ctrl.getUiLayer();
            uiLayer.setPickOnBounds(false);
            uiLayer.prefWidthProperty().bind(((Region) root).widthProperty());
            uiLayer.prefHeightProperty().bind(((Region) root).heightProperty());

            ctrl.getBorderPane().setBackground(
                    isBasic
                            ? new Background(new BackgroundFill(Color.DARKGREEN, null, null))
                            : controller.getBackgroundImage()
            );

            int count = controller.getPlayerCount();
            for (int i = 0; i < count; i++) {
                StackPane seat = new StackPane();
                seat.setPrefSize(160, 160);
                playerPanes.put(i, seat);
                uiLayer.getChildren().add(seat);
                seat.setPickOnBounds(false);
            }

            uiLayer.widthProperty().addListener(o -> layoutSeats());
            uiLayer.heightProperty().addListener(o -> layoutSeats());
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
        double w = uiLayer.getWidth(), h = uiLayer.getHeight();
        double cx = w / 2, cy = h / 2;
        int count = playerPanes.size();
        if (count == 0) return;

        double seatSize = (count <= 4 ? 160 : count <= 6 ? 140 : 120);
        double marginX = 60, marginY = 30;
        double radiusX = w / 2 - seatSize / 2 - marginX;
        double radiusY = h / 2 - seatSize / 2 - marginY;

        for (int i = 0; i < count; i++) {
            StackPane seat = playerPanes.get(i);
            seat.setPrefSize(seatSize, seatSize);
            double angle = -(2 * Math.PI * i / count - Math.PI / 2);
            double x = cx + radiusX * Math.cos(angle) - seatSize / 2;
            double y = cy + radiusY * Math.sin(angle) - seatSize / 2;
            seat.setLayoutX(x);
            seat.setLayoutY(y);
        }
    }

    protected Button createOutButton() {
        Button btn = new Button("Out");
        btn.setPrefSize(150, 40);
        btn.setStyle(buttonStyle);
        btn.setOnAction(e -> {
            controller.playClickSound();
            pressBack = true;
            Parent select = SelectGame.create(primaryStage);
            primaryStage.getScene().setRoot(select);
        });
        return btn;
    }

    protected void addPlayerNameLabel(StackPane seat, String name, int idx) {
        Label lbl = new Label(name);
        lbl.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        double deg = (360.0 * idx / playerPanes.size() - 90 + 360) % 360;
        if (deg >= 45 && deg < 135) {
            StackPane.setAlignment(lbl, Pos.BOTTOM_CENTER);
            StackPane.setMargin(lbl, new Insets(10, 0, 0, 0));
        } else {
            StackPane.setAlignment(lbl, Pos.TOP_CENTER);
            StackPane.setMargin(lbl, new Insets(-20, 0, 0, 0));
        }
        seat.getChildren().add(lbl);
        lbl.toFront();
    }

    protected void handleBackAction() {
        controller.playClickSound();
        pressBack = true;
        Parent select = SelectGame.create(primaryStage);
        primaryStage.getScene().setRoot(select);
    }

    protected abstract void updateScene();

    private static final String BASIC_BUTTON_STYLE = """
        -fx-background-color: linear-gradient(to right, #B0B0B0, #7A7A7A);  
        -fx-text-fill: white;
        -fx-font-size: 16px;
        -fx-font-weight: bold;
        -fx-background-radius: 20;
        -fx-border-radius: 20;
        -fx-border-color: white;
        -fx-border-width: 2;
    """;

    private static final String FANCY_BUTTON_STYLE = """
        -fx-background-color: linear-gradient(to right, #FF5722, #E64A19);
        -fx-text-fill: white;
        -fx-font-size: 16px;
        -fx-font-weight: bold;
        -fx-background-radius: 20;
        -fx-border-radius: 20;
        -fx-border-color: white;
        -fx-border-width: 2;
    """;
}
