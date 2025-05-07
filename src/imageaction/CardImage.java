package imageaction;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import module.Card;

public class CardImage {
// This is the original code
//    // Tạo hình ảnh mặt sau của lá bài
//    public static ImageView create(int j, int size) {
//        try {
//            ImageView cardImg;
//            cardImg = new ImageView(CardImage.class.getResource("/resources/card/back_of_card.png").toExternalForm());
//            cardImg.setFitWidth(80);
//            cardImg.setFitHeight(100);
//            cardImg.setTranslateX(j * 10 - 5 * size);
//            return cardImg;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    // Tạo hình ảnh mặt trước của lá bài dựa vào đối tượng Card
//    public static ImageView create(int j, int size, Card card) {
//        try {
//            ImageView cardImg;
//            cardImg = new ImageView(new Image(
//                    CardImage.class.getResource("/resources/card/" + card.toString() + ".png").toExternalForm()));
//            cardImg.setFitWidth(80);
//            cardImg.setFitHeight(100);
//            cardImg.setTranslateX(j * 20 - 10 * size);
//            return cardImg;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
    private static final String BACK = "/resources/card/back_of_card.png";

    /** Face-down card—no Card object needed. */
    public static ImageView createBackOfCard() {
        ImageView iv = new ImageView(
                CardImage.class.getResource(BACK).toExternalForm()
        );
        iv.setFitWidth(80);
        iv.setFitHeight(100);
        return iv;
    }

    /** Face-up card using your existing naming scheme (e.g. “AH.png”, “10♣.png”, etc). */
    public static ImageView createFaceOfCard(Card card) {
        String path = "/resources/card/" + card.toString() + ".png";
        ImageView iv = new ImageView(
                new Image(CardImage.class.getResource(path).toExternalForm())
        );
        iv.setFitWidth(80);
        iv.setFitHeight(100);
        return iv;
    }

    public static ImageView create(int j, int size) {
        ImageView iv = createBackOfCard();
        iv.setTranslateX(j * 10 - 5 * size);
        return iv;
    }

    /**
     * Face-of-card at position j of size slots;
     * delegates to createFaceOfCard(card) then applies the old translate logic.
     */
    public static ImageView create(int j, int size, Card card) {
        ImageView iv = createFaceOfCard(card);
        iv.setTranslateX(j * 20 - 10 * size);
        return iv;
    }
}
