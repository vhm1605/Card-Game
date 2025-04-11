package gamescene;

import gamelogic.TienLen;
import gamelogic.TienLenMienBac;
import gamelogic.TienLenMienNam;
import imageaction.BackgroundImageView;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import soundeffect.ClickSound;

public class InputScene {
	public static Scene create(Stage primaryStage, int k) {
		VBox inputPane = new VBox(20);
		inputPane.setAlignment(Pos.CENTER);

		// Tạo các nhãn và text field
		Label label = new Label("Chọn số lượng người chơi:");
		label.setStyle("-fx-font-size: 18px; -fx-text-fill: white;");

		// Thiết lập style cho TextField
		TextField playerTextField = new TextField();
		playerTextField.setPromptText("Số lượng người chơi");
		playerTextField.setMaxWidth(200);
		playerTextField.setStyle("""
				    -fx-font-size: 14px;
				    -fx-background-radius: 10;
				    -fx-border-radius: 10;
				    -fx-border-color: white;
				    -fx-border-width: 2;
				    -fx-padding: 5 10 5 10;
				""");

		TextField aiTextField = new TextField();
		aiTextField.setPromptText("Số lượng AI");
		aiTextField.setMaxWidth(200);
		aiTextField.setStyle("""
				    -fx-font-size: 14px;
				    -fx-background-radius: 10;
				    -fx-border-radius: 10;
				    -fx-border-color: white;
				    -fx-border-width: 2;
				    -fx-padding: 5 10 5 10;
				""");

		// Tạo button chuyển cảnh
		Button next = new Button("Tiếp theo");
		next.setPrefSize(150, 40);
		next.setStyle("""
				    -fx-background-color: linear-gradient(to right, #4CAF50, #81C784);
				    -fx-text-fill: white;
				    -fx-font-size: 16px;
				    -fx-font-weight: bold;
				    -fx-background-radius: 20;
				    -fx-border-radius: 20;
				    -fx-border-color: white;
				    -fx-border-width: 2;
				""");

		next.setOnAction(e -> {
			ClickSound.play();
			int playerCount = parseInput(playerTextField.getText());
			int aiCount = parseInput(aiTextField.getText());
			int numOfPlayer = playerCount + aiCount;
			if (numOfPlayer <= 1 || numOfPlayer > 4) {
				return;
			} else {
				// Chuyển sang màn hình chơi game
				if (k == 0) {
					TienLen tienLen = new TienLenMienNam(numOfPlayer, aiCount);
					GameMainScene<TienLenMienNam> gameScene = new GameMainScene<>((TienLenMienNam) tienLen);
					Scene gameSceneObject = gameScene.createScene(); // Sử dụng GameScene để tạo gameScene
					primaryStage.setScene(gameSceneObject);
				} else if (k == 1) {
					TienLen tienLen = new TienLenMienNam(numOfPlayer, aiCount);
					GameMainSceneBasic<TienLenMienNam> gameScene = new GameMainSceneBasic<>(
							(TienLenMienNam) tienLen, primaryStage);
					Scene gameSceneObject = gameScene.createScene(); // Sử dụng GameScene để tạo gameScene
					primaryStage.setScene(gameSceneObject);
				} else if (k == 2) {
					TienLen tienLen = new TienLenMienBac(numOfPlayer, aiCount);
					GameMainScene<TienLenMienBac> gameScene = new GameMainScene<>((TienLenMienBac) tienLen);
					Scene gameSceneObject = gameScene.createScene(); // Sử dụng GameScene để tạo gameScene
					primaryStage.setScene(gameSceneObject);
				} else {
					TienLen tienLen = new TienLenMienBac(numOfPlayer, aiCount);
					GameMainSceneBasic<TienLenMienBac> gameScene = new GameMainSceneBasic<>(
							(TienLenMienBac) tienLen, primaryStage);
					Scene gameSceneObject = gameScene.createScene(); // Sử dụng GameScene để tạo gameScene
					primaryStage.setScene(gameSceneObject);
				}
			}
		});

		inputPane.getChildren().addAll(label, playerTextField, aiTextField, next);

		// Tạo nền riêng cho scene nhập số người chơi
		StackPane inputRoot = new StackPane();
		inputRoot.setPrefSize(1200, 700);
		inputRoot.setBackground(BackgroundImageView.set());

		inputRoot.getChildren().add(inputPane);

		return new Scene(inputRoot, 1200, 700);
	}

	// xử lý input
	public static int parseInput(String input) {
		if (input == null || input.trim().isEmpty()) {
			return 0;
		}
		return Integer.parseInt(input);
	}
}
