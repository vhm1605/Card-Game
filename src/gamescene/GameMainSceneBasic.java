package gamescene;

import gamelogic.TienLen;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import module.AIPlayer;
import module.Card;

public class GameMainSceneBasic<T extends TienLen> {
	private T gameType;
	private Stage primaryStage;
	FlowPane cardPane = new FlowPane(5, 1);
	Label playerLabel = new Label();

	public GameMainSceneBasic(T gameType, Stage primaryStage) {
		this.gameType = gameType;
		this.primaryStage = primaryStage;
	}

	Label statusLabel1 = new Label("Lượt đầu tiên");
	Label statusLabel2 = new Label("Trên bàn đang không có gì!");
	Runnable SceneUpdate = () -> {
		primaryStage.setScene(createScene());
	};

	public Scene createScene() {
		VBox layout0 = new VBox(10);// Layout cha lớn nhất
		// 4 đối tượng con của container layout0

		HBox buttonLayout = new HBox(50);
		// Layout con1 cardPane chủ yếu để chứa những lá bài của người chơi
		if (!(gameType.getCurrentPlayer() instanceof AIPlayer)) {
			cardPane.getChildren().clear();
			// cardPane.getChildren().clear();
			// Đặt text cho playerLabel
			playerLabel.setText("Người chơi số " + (gameType.getCurrentPlayerIndex() + 1) + ": "
					+ gameType.getCurrentPlayer().getName() + " - Số bài còn lại: " + gameType.getCurrentPlayer().getHandSize());
			playerLabel.setFont(Font.font("Arial", 30));
			if (gameType.getCurrentPlayer() != null && gameType.getCurrentPlayer().getAllCards() != null) {
				for (Card card : gameType.getCurrentPlayer().getAllCards()) {
					Button cardButton = new Button(card.toString());
					cardButton.setStyle("-fx-padding: 13; -fx-background-color: pink;");
					cardButton.setOnAction(e -> {
						if (gameType.getSelectedCards().getAllCards().contains(card)) {
							gameType.deselectCard(card);
							cardButton.setStyle("-fx-padding: 13; -fx-background-color: pink;");
						} else {
							gameType.selectCard(card);
							cardButton.setStyle("-fx-padding: 10; -fx-background-color: lightgreen;");
						}
					});
					cardPane.getChildren().add(cardButton);
				}
			} else {
				System.err.println("Không thể cập nhật cardPane: Player hoặc Cards bị null.");
			}
		} else {
			Label AILabel = new Label();
			AILabel.setText("Lượt của AI");
			AIPlayer tempBot = (AIPlayer) gameType.getCurrentPlayer();
			if (tempBot.makeMove(gameType)) {
				int sizeOfBot = tempBot.getHandSize();
				if (gameType.isGameOver()) {
					// Khi kết thúc trò chơi, chuyển sang Scene mới chứa nút Restart
					VBox endGameLayout = new VBox(20);
					endGameLayout.setStyle("-fx-padding: 25;");
					endGameLayout.setAlignment(Pos.CENTER);

					String temp = "Thứ tự xếp hạng chiến thắng: ";
					temp += gameType.playerRankingToString();
					Label winnerLabel = new Label(temp);
					winnerLabel.setFont(Font.font("Arial", 20));

					Label endGameLabel = new Label("Trò chơi kết thúc!");
					endGameLabel.setFont(Font.font("Arial", 30));

					Button restartButton = new Button("Restart");
					restartButton.setOnAction(restartEvent -> {
						gameType.resetGame(); // Khởi động lại trò chơi
						statusLabel1.setText("Trò chơi đã được khởi động lại!");
						statusLabel2.setText("Trên bàn chưa có gì!!!");
						// Chuyển lại về màn hình chính sau khi khởi động lại trò chơi
						primaryStage.setScene(createScene());
					});

					endGameLayout.getChildren().addAll(winnerLabel, endGameLabel, restartButton);
					Scene endGameScene = new Scene(endGameLayout, 1200, 700);

					// Dời việc chuyển scene sang luồng UI chính
					Platform.runLater(() -> {
						primaryStage.setScene(endGameScene); // Chuyển đổi scene ngay lập tức và không gọi thêm gì nữa
					});
				}
				statusLabel1.setText("AI đã đánh bài!" + "    còn lại:" + sizeOfBot + "lá");
				StringBuilder selectedCardsText = new StringBuilder();
				for (Card card : gameType.getLastPlayedCards().getAllCards()) {
					selectedCardsText.append(card.toString()).append(", ");
				}
				if (selectedCardsText.length() > 0) {
					selectedCardsText.setLength(selectedCardsText.length() - 2); // Xóa dấu ", " cuối cùng
				}
				statusLabel2.setText("Bài trên bàn:   " + selectedCardsText.toString());
				statusLabel2.setFont(Font.font("Arial", 20));
				gameType.getSelectedCards().empty();
			} else {
				statusLabel1.setText("AI đã bỏ lượt!");
			}
			if (!gameType.isGameOver()) {
				SceneUpdate.run();
			}
		}

		// layout con2 HBox chủ yếu chứa các nút thao tác của người chơi
		// Nút Hit
		Button playButton = new Button("Hit");
		playButton.setOnAction(e -> {
			if (gameType.isValidPlay()) {
				gameType.playGame(); // Đánh bài
				statusLabel1.setText("Đánh bài ");

				if (gameType.isGameOver()) {
					// Khi kết thúc trò chơi, chuyển sang Scene mới chứa nút Restart
					VBox endGameLayout = new VBox(20);
					endGameLayout.setStyle("-fx-padding: 25;");
					endGameLayout.setAlignment(Pos.CENTER);

					String temp = "Thứ tự xếp hạng chiến thắng: ";
					temp += gameType.playerRankingToString();
					Label winnerLabel = new Label(temp);
					winnerLabel.setFont(Font.font("Arial", 20));

					Label endGameLabel = new Label("Trò chơi kết thúc!");
					endGameLabel.setFont(Font.font("Arial", 30));

					Button restartButton = new Button("Restart");
					restartButton.setOnAction(restartEvent -> {
						gameType.resetGame(); // Khởi động lại trò chơi
						statusLabel1.setText("Trò chơi đã được khởi động lại!");
						statusLabel2.setText("Trên bàn chưa có gì!!!");
						// Chuyển lại về màn hình chính sau khi khởi động lại trò chơi
						primaryStage.setScene(createScene());
					});

					endGameLayout.getChildren().addAll(winnerLabel, endGameLabel, restartButton);
					Scene endGameScene = new Scene(endGameLayout, 1200, 700);

					// Đảm bảo chỉ chuyển scene duy nhất
					primaryStage.setScene(endGameScene); // Chuyển đổi scene ngay lập tức và không gọi thêm gì nữa
				}

				// Hiển thị text của các lá bài đã chọn
				StringBuilder selectedCardsText = new StringBuilder();
				for (Card card : gameType.getSelectedCards().getAllCards()) {
					selectedCardsText.append(card.toString()).append(", ");
				}
				if (selectedCardsText.length() > 0) {
					selectedCardsText.setLength(selectedCardsText.length() - 2); // Xóa dấu ", " cuối cùng
				}
				statusLabel2.setText("Bài trên bàn:   " + selectedCardsText.toString());
				statusLabel2.setFont(Font.font("Arial", 20));
				gameType.getSelectedCards().empty();
				if (!gameType.isGameOver()) {
					SceneUpdate.run();
				}
			} else {
				statusLabel1.setText("Bài không hợp lệ! Vui lòng chọn lại.");
			}
		});

		// Nút Skip
		Button skipButton = new Button("Skip");
		skipButton.setOnAction(e -> {
			if (gameType.passTurn()) {
				statusLabel1.setText("Bỏ lượt ");
			} else {
				statusLabel1.setText("Không thể skip!!!");
			}
			SceneUpdate.run();
		});

		// Nút Cancel
		Button cancelButton = new Button("Cancel");
		cancelButton.setOnAction(e -> {
			if (gameType.getSelectedCards().getAllCards().isEmpty()) {
				statusLabel1.setText("Hãy chọn bài của bạn!");
			} else {
				gameType.cancelSelection();
				statusLabel1.setText("Đã bỏ chọn");
			}
			SceneUpdate.run();
		});
		buttonLayout.getChildren().addAll(playButton, skipButton, cancelButton);
		layout0.getChildren().addAll(playerLabel, cardPane, buttonLayout, statusLabel1, statusLabel2);
		Scene sceneResult = new Scene(layout0, 1200, 700);
		return sceneResult;
	}
}
