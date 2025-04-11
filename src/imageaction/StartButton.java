package imageaction;

import gamescene.GameSeclectScene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import soundeffect.ClickSound;

public class StartButton {
	public static StackPane create(Stage primaryStage) {
		try {
			// Load the start button image
			Image image = new Image(StartButton.class.getResource("/resources/card/startButton.png").toExternalForm());
			ImageView startButton = new ImageView(image);

			startButton.setPreserveRatio(true);
			startButton.setSmooth(true);

			// Bind the width to 25% of window width
			startButton.fitWidthProperty().bind(primaryStage.widthProperty().multiply(0.25));

			// Set click event
			startButton.setOnMouseClicked(e -> {
				ClickSound.play();
				primaryStage.setScene(GameSeclectScene.create(primaryStage));
			});

			// Wrap in StackPane to center it
			StackPane buttonContainer = new StackPane(startButton);
			return buttonContainer;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
