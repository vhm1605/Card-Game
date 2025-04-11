package imageaction;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import module.Card;

public class CardImg {
	public static ImageView create(int j, int size, Card card) {
		try {
			ImageView cardImg;
			cardImg = new ImageView(new Image(
					CardImg.class.getResource("/resources/card/" + card.toString() + ".png").toExternalForm()));
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