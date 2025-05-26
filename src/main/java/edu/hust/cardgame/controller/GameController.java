package main.java.edu.hust.cardgame.controller;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import main.java.edu.hust.cardgame.model.StandardCard;

import java.util.List;
import java.util.function.Function;

public interface GameController {

    /** Số lượng người chơi trong ván game */
    int getPlayerCount();

    /** Trả về chỉ số của người chơi hiện tại */
    int getCurrentPlayerIndex();

    /** Trả về tên hiển thị của người chơi tại index */
    String getPlayerName(int index);

    /** Trả về danh sách hình ảnh bài đã đánh gần nhất */
    List<ImageView> getLastPlayedCardImages(boolean isBasic);

    /** Trả về ảnh bài của người chơi i, nếu được phép hiển thị */
    List<ImageView> getVisibleCards(int playerIndex, boolean isBasic, Function<StandardCard, Integer> onClickOffsetHandler);

    /** Trả về ảnh bài úp (ẩn) của người chơi */
    List<ImageView> getHiddenCardImages(int playerIndex, boolean isBasic);

    /** Có phải lượt của AI không */
    boolean isCurrentPlayerAI();

    /** Gọi nước đi của AI */
    void makeAIMove();

    /** Bắt đầu hành động đánh bài của người chơi */
    boolean isValidPlay();

    /** Gọi hành động đánh bài */
    void play();

    /** Gọi hành động bỏ lượt */
    void passTurn();

    /** Có kết thúc game không */
    boolean isGameOver();

    /** Hiển thị xếp hạng người chơi sau trận */
    String getRankingText();

    /** Reset game để bắt đầu lại */
    void resetGame();

    /** Lấy ảnh nền (background gameplay) */
    Background getBackgroundImage();

    /** Âm thanh click */
    void playClickSound();

    /** Kiểm tra 1 lá bài có đang được chọn không */
    boolean isCardSelected(StandardCard card);

    /** Chọn 1 lá bài */
    void selectCard(StandardCard card);

    /** Bỏ chọn 1 lá bài */
    void deselectCard(StandardCard card);

    /** Bỏ chọn toàn bộ */
    void clearSelectedCards();

    /** Đảm bảo người chơi đang ở trạng thái IN_ROUND */
    void ensureInRound();
}
