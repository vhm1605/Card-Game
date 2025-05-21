package imageaction;

import javafx.scene.image.Image;
import javafx.scene.layout.*;
import java.util.Objects;

public class BackgroundImage {

    private static final String DEFAULT_BACKGROUND_PATH = "/resources/card/background.png";

    public static Background set() {
        return set(DEFAULT_BACKGROUND_PATH);
    }

    public static Background set(String resourcePath) {
        try {
            Image background = new Image(
                    Objects.requireNonNull(
                            BackgroundImage.class.getResource(resourcePath),
                            "Không tìm thấy file background: " + resourcePath
                    ).toExternalForm(),
                    0, 0, // width & height: 0 nghĩa là không giới hạn
                    true, // preserve ratio
                    true  // smooth
            );

            javafx.scene.layout.BackgroundImage bgImage = new javafx.scene.layout.BackgroundImage(
                    background,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT,
                    new BackgroundSize(
                            1, 1, // width and height in percentage
                            true, true, // widthAsPercentage & heightAsPercentage
                            false, false // contain, cover
                    )
            );

            return new Background(bgImage);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}