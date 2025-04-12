package imageaction;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import module.Card;

public class CardImage {

    // Tạo hình ảnh mặt sau của lá bài
    public static ImageView create(int j, int size) {
        try {
            ImageView cardImg;
            cardImg = new ImageView(CardImage.class.getResource("/resources/card/back_of_card.png").toExternalForm());
            cardImg.setFitWidth(80);
            cardImg.setFitHeight(100);
            cardImg.setTranslateX(j * 10 - 5 * size);
            return cardImg;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Tạo hình ảnh mặt trước của lá bài dựa vào đối tượng Card
    public static ImageView create(int j, int size, Card card) {
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
}
