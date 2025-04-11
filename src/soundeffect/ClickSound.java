package soundeffect;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class ClickSound {
	public static void play() {
		try {
			Media media = new Media(ClickSound.class.getResource("/resources/music/click.mp3").toExternalForm());
			MediaPlayer mediaPlayer = new MediaPlayer(media);
			mediaPlayer.setVolume(1);
			mediaPlayer.play();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
