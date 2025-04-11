package soundeffect;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class BackgroundMusic {
	public static void play() {
		try {
			MediaPlayer mediaPlayer = new MediaPlayer(
					new Media(BackgroundMusic.class.getResource("/resources/music/login.mp3").toExternalForm()));
			mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
			mediaPlayer.setVolume(0.3);
			mediaPlayer.play();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
