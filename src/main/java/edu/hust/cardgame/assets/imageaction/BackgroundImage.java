package edu.hust.cardgame.assets.imageaction;

import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

import java.net.URL;
import java.util.Objects;

public final class BackgroundImage {
    private static final String DEFAULT_BACKGROUND_PATH = "/card/background.png";

    private BackgroundImage() {
    }

    public static Background set() {
        return set(DEFAULT_BACKGROUND_PATH);
    }

    public static Background set(String resourcePath) {
        URL resource = Objects.requireNonNull(
                BackgroundImage.class.getResource(resourcePath),
                "Missing background image: " + resourcePath
        );
        Image image = new Image(resource.toExternalForm(), 0, 0, true, true);
        javafx.scene.layout.BackgroundImage backgroundImage =
                new javafx.scene.layout.BackgroundImage(
                        image,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.DEFAULT,
                        new BackgroundSize(1, 1, true, true, false, false)
                );
        return new Background(backgroundImage);
    }
}
