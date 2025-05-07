package application;

import soundaction.ClickSound;
import imageaction.BackgroundImage;
import gamescene.SelectGame;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Tạo layout gốc
            StackPane root = new StackPane();
            root.setPrefSize(1280, 720);

            // Đặt ảnh nền
            root.setBackground(BackgroundImage.set());

            // Tạo ảnh và nút bắt đầu
            Image image = new Image(
                    Main.class.getResource("/resources/card/start_button.png").toExternalForm()
            );

            ImageView startButton = new ImageView(image);
            startButton.setPreserveRatio(true);   // Giữ tỉ lệ ảnh gốc
            startButton.setSmooth(true);          // Hiển thị mượt hơn

            // Ràng buộc chiều rộng ảnh bằng 20% chiều rộng cửa sổ
            startButton.fitWidthProperty().bind(
                    primaryStage.widthProperty().multiply(0.25)
            );

            // Bọc trong StackPane để căn giữa
            StackPane buttonContainer = new StackPane(startButton);

            // Click event
            startButton.setOnMouseClicked(e -> {
                ClickSound.play();
                // Get the new root pane from SelectGame.create
                Parent selectGameRoot = SelectGame.create(primaryStage); // <--- Get the Parent root

                // Get the current scene and set its root
                Scene currentScene = primaryStage.getScene(); // <--- Get the existing scene
                currentScene.setRoot(selectGameRoot);        // <--- Set the new root
            });

            // Thêm nút bắt đầu vào layout
            root.getChildren().add(buttonContainer);

            // Tạo scene chính
            Scene mainScene = new Scene(root, 1280, 720);

            // Thiết lập Stage
            primaryStage.setTitle("Game Bài");
            primaryStage.setScene(mainScene);
            primaryStage.setFullScreen(true);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
