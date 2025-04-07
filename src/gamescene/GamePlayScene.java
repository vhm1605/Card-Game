package gamescene;

import controller.GamePlayController;
import imageaction.BackgroundImage;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GamePlayScene {
    public static Parent create(Stage primaryStage) {
        try {
            // Load FXML
            FXMLLoader loader = new FXMLLoader(GamePlayScene.class.getResource("GamePlayScene.fxml"));
            Parent root = loader.load(); // This is the Parent we want to return
            GamePlayController controller = loader.getController();

            // Set background - Ensure root is castable to Pane if using setBackground directly
            if (root instanceof Pane) {
                ((Pane) root).setBackground(BackgroundImage.set());
            } else {
                // Handle cases where the root isn't a Pane if necessary,
                // though BorderPane (loaded from FXML) is a Pane.
                System.err.println("Warning: Root loaded from FXML is not a Pane, cannot set background directly.");
            }

            // Get panes from controller
            StackPane centerPane = controller.getCenterPane();
            StackPane bottomPane = controller.getBottomPane();
            StackPane topPane = controller.getTopPane();
            StackPane leftPane = controller.getLeftPane();
            StackPane rightPane = controller.getRightPane();

            // Apply padding to panes (similar to GameMainScene)
            bottomPane.setPadding(new Insets(10, 0, 10, 0));
            topPane.setPadding(new Insets(10, 0, 10, 0));
            leftPane.setPadding(new Insets(80, 150, 80, 150));
            rightPane.setPadding(new Insets(80, 150, 80, 150));

            // Add placeholder label to center pane
            Label gameLabel = new Label("Game Area");
            gameLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");
            centerPane.getChildren().add(gameLabel);

            // Add dummy cards to bottom pane (player's hand)
            double cardWidth = 50;
            double cardHeight = 70;
            double spacing = 10;
            for (int i = 0; i < 13; i++) { // 13 cards as a typical hand size
                Label cardLabel = new Label("Card " + (i + 1));
                cardLabel.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-padding: 5;");
                cardLabel.setPrefSize(cardWidth, cardHeight);
                cardLabel.setTranslateX(i * (cardWidth + spacing) - (12 * (cardWidth + spacing)) / 2); // Center horizontally
                cardLabel.setOnMouseClicked(e -> {
                    System.out.println("Card clicked: " + cardLabel.getText());
                    cardLabel.setStyle("-fx-background-color: yellow; -fx-border-color: black; -fx-padding: 5;");
                });
                bottomPane.getChildren().add(cardLabel);
            }

            // Add dummy cards to top, left, right panes (opponents)
            for (int i = 0; i < 13; i++) {
                Label topCard = new Label("Card");
                topCard.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-padding: 5;");
                topCard.setPrefSize(cardWidth, cardHeight);
                topCard.setTranslateX(i * (cardWidth + spacing) - (12 * (cardWidth + spacing)) / 2);
                topPane.getChildren().add(topCard);

                Label leftCard = new Label("Card");
                leftCard.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-padding: 5;");
                leftCard.setPrefSize(cardWidth, cardHeight);
                leftCard.setTranslateY(i * 20); // Vertical stacking with overlap
                leftPane.getChildren().add(leftCard);

                Label rightCard = new Label("Card");
                rightCard.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-padding: 5;");
                rightCard.setPrefSize(cardWidth, cardHeight);
                rightCard.setTranslateY(i * 20);
                rightPane.getChildren().add(rightCard);
            }

            // Add action buttons to bottom pane
            Button testButton1 = new Button("Test 1");
            Button testButton2 = new Button("Test 2");
            String buttonStyle = """
                -fx-background-color: linear-gradient(to right, #4CAF50, #81C784);
                -fx-text-fill: white;
                -fx-font-size: 16px;
                -fx-font-weight: bold;
                -fx-background-radius: 20;
                -fx-border-radius: 20;
                -fx-border-color: white;
                -fx-border-width: 2;
            """;
            testButton1.setStyle(buttonStyle);
            testButton2.setStyle(buttonStyle);
            testButton1.setPrefSize(150, 40);
            testButton2.setPrefSize(150, 40);
            testButton1.setOnAction(e -> System.out.println("Test 1 clicked"));
            testButton2.setOnAction(e -> System.out.println("Test 2 clicked"));
            HBox buttonBox = new HBox(20, testButton1, testButton2);
            buttonBox.setPadding(new Insets(20, 0, 0, 0)); // Space above buttons
            bottomPane.getChildren().add(buttonBox);

            // Create and return the scene
            return root;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
