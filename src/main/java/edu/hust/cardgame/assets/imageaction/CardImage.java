package edu.hust.cardgame.assets.imageaction;

import edu.hust.cardgame.core.StandardCard;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.Objects;

public final class CardImage {
    private static final double CARD_WIDTH = 80;
    private static final double CARD_HEIGHT = 100;

    private CardImage() {
    }

    public static ImageView create(int index, int handSize, boolean isBasic) {
        ImageView cardImage = isBasic
                ? createBasicCardBack()
                : new ImageView(loadImage("/card/back_of_card.png"));
        sizeAndPosition(cardImage, index, handSize, 10, 5);
        return cardImage;
    }

    public static ImageView create(
            int index,
            int handSize,
            StandardCard card,
            boolean isBasic
    ) {
        Objects.requireNonNull(card, "card");
        return isBasic
                ? createBasicCard(index, handSize, card)
                : createCard(index, handSize, card);
    }

    private static ImageView createCard(int index, int handSize, StandardCard card) {
        ImageView cardImage = new ImageView(loadImage("/card/" + card + ".png"));
        sizeAndPosition(cardImage, index, handSize, 15, 7);
        return cardImage;
    }

    private static ImageView createBasicCard(int index, int handSize, StandardCard card) {
        Canvas canvas = createCardCanvas(Color.WHITE, Color.BLACK);
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        String label = card.toString();
        String face = label.substring(0, label.length() - 1);
        String suit = label.substring(label.length() - 1);

        boolean redSuit = "♥".equals(suit) || "♦".equals(suit);
        graphics.setFill(redSuit ? Color.FIREBRICK : Color.BLACK);
        graphics.setFont(Font.font("Arial", 12));
        graphics.fillText(face, 5, 20);
        graphics.setFont(Font.font("Arial", 20));
        graphics.fillText(suit, 5, 42);

        ImageView cardImage = snapshot(canvas);
        sizeAndPosition(cardImage, index, handSize, 15, 7);
        return cardImage;
    }

    private static ImageView createBasicCardBack() {
        return snapshot(createCardCanvas(Color.BLACK, Color.WHITE));
    }

    private static Canvas createCardCanvas(Color fill, Color stroke) {
        Canvas canvas = new Canvas(CARD_WIDTH, CARD_HEIGHT);
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        graphics.setFill(fill);
        graphics.fillRect(0, 0, CARD_WIDTH, CARD_HEIGHT);
        graphics.setStroke(stroke);
        graphics.strokeRect(0, 0, CARD_WIDTH, CARD_HEIGHT);
        return canvas;
    }

    private static ImageView snapshot(Canvas canvas) {
        WritableImage image = new WritableImage((int) CARD_WIDTH, (int) CARD_HEIGHT);
        canvas.snapshot(null, image);
        return new ImageView(image);
    }

    private static Image loadImage(String path) {
        URL resource = Objects.requireNonNull(
                CardImage.class.getResource(path),
                "Missing card image: " + path
        );
        return new Image(resource.toExternalForm());
    }

    private static void sizeAndPosition(
            ImageView image,
            int index,
            int handSize,
            int spacing,
            int centeringOffset
    ) {
        image.setFitWidth(CARD_WIDTH);
        image.setFitHeight(CARD_HEIGHT);
        image.setTranslateX(index * spacing - centeringOffset * handSize);
    }
}
