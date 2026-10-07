package edu.hust.cardgame.assets.soundaction;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class ClickSound {
    private static final Media CLICK = loadClickSound();
    private static final Set<MediaPlayer> ACTIVE_PLAYERS = new HashSet<>();

    private ClickSound() {
    }

    public static void play() {
        MediaPlayer player = new MediaPlayer(CLICK);
        ACTIVE_PLAYERS.add(player);
        player.setVolume(1);
        player.setOnEndOfMedia(() -> dispose(player));
        player.setOnError(() -> dispose(player));
        player.play();
    }

    private static Media loadClickSound() {
        URL resource = Objects.requireNonNull(
                ClickSound.class.getResource("/sound/clicksound.mp3"),
                "Missing click sound"
        );
        return new Media(resource.toExternalForm());
    }

    private static void dispose(MediaPlayer player) {
        ACTIVE_PLAYERS.remove(player);
        player.dispose();
    }
}
