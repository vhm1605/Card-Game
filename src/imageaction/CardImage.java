package imageaction;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import module.StandardCard;

import java.awt.*;
// cứ cái nào mà StandardCard là đang chạy của bài 52 lá

public class CardImage {

    // Tạo hình ảnh mặt sau của lá bài
    public static ImageView create(int j, int size, boolean isBasic) {
        try {
            ImageView cardImg;
            if (isBasic) {
                WritableImage writableImage = new WritableImage(80, 100);
                Canvas canvas = new Canvas(80, 100);
                GraphicsContext gc = canvas.getGraphicsContext2D();
                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, 80, 100);
                gc.setStroke(Color.WHITE);
                gc.strokeRect(0, 0, 80, 100);
                canvas.snapshot(null, writableImage);
                cardImg = new ImageView(writableImage);
            } else {
                cardImg = new ImageView(CardImage.class.getResource("/resources/card/back_of_card.png").toExternalForm());
            }

            cardImg.setFitWidth(80);
            cardImg.setFitHeight(100);
            cardImg.setTranslateX(j * 10 - 5 * size);
            return cardImg;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static ImageView create(int j, int size, StandardCard card, boolean isBasic) { // new
        if(isBasic) return createBasicCard(j, size, card);
        else return createCard(j, size, card);
    }

    public static ImageView createCard(int j, int size, StandardCard card){
        try {
            ImageView cardImg;
            cardImg = new ImageView(new Image(
                    CardImage.class.getResource("/resources/card/" + card.toString() + ".png").toExternalForm()));
            cardImg.setFitWidth(80);
            cardImg.setFitHeight(100);
            cardImg.setTranslateX(j * 20 - 10 * size);
            return cardImg;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static ImageView createBasicCard(int j, int size, StandardCard card) {
        try {
            // Tạo một hình ảnh trắng với kích thước 80x100
            String text =  card.toString();
            WritableImage writableImage = new WritableImage(80, 100);
            Canvas canvas = new Canvas(80, 100);
            GraphicsContext gc = canvas.getGraphicsContext2D();

            // Vẽ nền trắng và viền đen
            gc.setFill(Color.WHITE);
            gc.fillRect(0, 0, 80, 100);
            gc.setStroke(Color.BLACK);
            gc.strokeRect(0, 0, 80, 100);

            // Vẽ text ở giữa hình ảnh
            String upperText = text.substring(0, text.length() - 1);
            String lowerText = text.substring(text.length() - 1);
            gc.setFill(Color.BLACK);
            gc.setFont(Font.font("Arial", 12));
            gc.fillText(upperText, 2, 45);
            gc.setFont(Font.font("Arial", 20));
            gc.fillText(lowerText, 2, 70);

            // Lưu hình ảnh từ canvas vào writableImage
            canvas.snapshot(null, writableImage);

            // Chuyển hình ảnh thành ImageView
            ImageView cardImg = new ImageView(writableImage);
            cardImg.setTranslateX(j * 20 - 10 * size);
            return cardImg;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}