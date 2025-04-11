package gamescene;

import imageaction.BackgroundImageView;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameSeclectScene {
	public static Scene create(Stage primaryStage) {
		VBox selectPane = new VBox(20);
		selectPane.setAlignment(Pos.CENTER);

		Label label = new Label("Chọn Game và chế độ chơi");
		label.setStyle("-fx-font-size: 18px; -fx-text-fill: white;");

		Button button1 = new Button("Tiến lên miền nam giao diện đẹp");
		Button button2 = new Button("Tiến lên miền nam giao diện Basic");
		Button button3 = new Button("Tiến lên miền bắc giao diện đẹp");
		Button button4 = new Button("Tiến lên miền bắc giao diện Basic");

		// Áp dụng cùng kiểu dáng cho các nút
		Button[] buttons = { button1, button2, button3, button4 };
		for (Button button : buttons) {
			button.setPrefSize(300, 50); // Kích thước đồng nhất
			button.setStyle("""
					    -fx-background-color: linear-gradient(to right, #4CAF50, #81C784);
					    -fx-text-fill: white;
					    -fx-font-size: 16px;
					    -fx-font-weight: bold;
					    -fx-background-radius: 25;
					    -fx-border-radius: 25;
					    -fx-border-color: white;
					    -fx-border-width: 2;
					""");
		}

		// Thêm sự kiện vào các nút
		button1.setOnAction(e -> primaryStage.setScene(InputScene.create(primaryStage, 0)));
		button2.setOnAction(e -> primaryStage.setScene(InputScene.create(primaryStage, 1)));
		button3.setOnAction(e -> primaryStage.setScene(InputScene.create(primaryStage, 2)));
		button4.setOnAction(e -> primaryStage.setScene(InputScene.create(primaryStage, 3)));

		selectPane.getChildren().addAll(label, button1, button2, button3, button4);

		StackPane inputRoot = new StackPane();
		inputRoot.setPrefSize(1200, 700);
		inputRoot.setBackground(BackgroundImageView.set());
		inputRoot.getChildren().add(selectPane);

		return new Scene(inputRoot, 1200, 700);
	}
}
