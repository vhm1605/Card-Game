package application;

import imageaction.BackgroundImageView;
import imageaction.StartButton;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import soundeffect.BackgroundMusic;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		StackPane root = new StackPane();
		root.setPrefSize(1200, 700);
		root.setBackground(BackgroundImageView.set());
		BackgroundMusic.play();
		root.getChildren().add(StartButton.create(primaryStage));

		Scene mainScene = new Scene(root, 1200, 700);

		primaryStage.setTitle("CardGame Project");
		primaryStage.setScene(mainScene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
