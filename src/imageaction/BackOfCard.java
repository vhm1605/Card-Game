package imageaction;

import javafx.scene.image.ImageView;

public class BackOfCard {
	public static ImageView create(int j, int size) {
		try {
			ImageView cardImg;
			cardImg = new ImageView(BackOfCard.class.getResource("/resources/card/back_of_card.png").toExternalForm());
			cardImg.setFitWidth(80);
			cardImg.setFitHeight(100);
			cardImg.setTranslateX(j * 10 - 5 * size);
			return cardImg;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
