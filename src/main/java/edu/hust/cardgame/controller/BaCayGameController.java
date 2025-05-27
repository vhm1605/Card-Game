package main.java.edu.hust.cardgame.controller;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import main.java.edu.hust.cardgame.assets.imageaction.BackgroundImage;
import main.java.edu.hust.cardgame.assets.imageaction.CardImage;
import main.java.edu.hust.cardgame.assets.soundaction.ClickSound;
import main.java.edu.hust.cardgame.logic.bacay.BaCay;
import main.java.edu.hust.cardgame.core.Player;
import main.java.edu.hust.cardgame.core.StandardCard;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class BaCayGameController implements GameController {

    private final BaCay game;

    public BaCayGameController(BaCay game) {
        this.game = game;
    }

    public int getScore(int playerIndex) {
        return game.getScoreFor(game.getPlayers().get(playerIndex));
    }

    public ImageView getCardImage(int playerIndex, int cardIndex, boolean isBasic) {
        StandardCard card = game.getPlayers().get(playerIndex).getAllCards().get(cardIndex);
        return CardImage.create(cardIndex, 3, card, isBasic);
    }

    public ImageView getHiddenCardImage(int cardIndex, boolean isBasic) {
        return CardImage.create(cardIndex, 3, isBasic);
    }

    @Override
    public void playClickSound() {
        ClickSound.play();
    }

    @Override
    public int getPlayerCount() {
        return game.getPlayers().size();
    }

    @Override
    public int getCurrentPlayerIndex() {
        return -1; // Không cần thiết với Ba Cây vì mọi người chơi cùng lúc
    }

    @Override
    public String getPlayerName(int index) {
        return "Player " + (index + 1);
    }

    @Override
    public boolean isGameOver() {
        return false; // Ba Cây không có logic 'kết thúc' truyền thống
    }

    @Override
    public boolean isCurrentPlayerAI() {
        return false; // không có lượt AI cụ thể trong game này
    }

    @Override
    public void makeAIMove() {
        // Không áp dụng cho Ba Cây
    }

    @Override
    public boolean isValidPlay() {
        return false; // Không áp dụng cho Ba Cây
    }

    @Override
    public void play() {
        // Không áp dụng cho Ba Cây
    }

    @Override
    public void passTurn() {
        // Không áp dụng cho Ba Cây
    }

    @Override
    public String getRankingText() {
        return ""; // Xử lý xếp hạng trong Scene
    }

    @Override
    public void resetGame() {
        game.resetGame();
    }

    @Override
    public Background getBackgroundImage() {
        return BackgroundImage.set("/main/resources/card/backgroundgameplay.png");
    }

    @Override
    public java.util.List<ImageView> getLastPlayedCardImages(boolean isBasic) {
        return java.util.Collections.emptyList();
    }

    @Override
    public List<ImageView> getVisibleCards(int playerIndex, boolean isBasic, Function<StandardCard, Integer> onClickOffsetHandler) {
        List<ImageView> views = new ArrayList<>();
        Player<StandardCard> player = game.getPlayers().get(playerIndex);
        List<StandardCard> cards = player.getAllCards();
        int handSize = cards.size();

        for (int j = 0; j < handSize; j++) {
            StandardCard card = cards.get(j);
            ImageView img = CardImage.create(j, handSize, card, isBasic);

            img.setOnMouseClicked(e -> {
                ClickSound.play();
                if (game.getSelectedCards().getAllCards().contains(card)) {
                    deselectCard(card);
                    img.setTranslateY(0);
                } else {
                    selectCard(card);
                    img.setTranslateY(-10);
                }
            });

            views.add(img);
        }

        return views;
    }

    @Override
    public List<ImageView> getHiddenCardImages(int playerIndex, boolean isBasic) {
        List<ImageView> views = new ArrayList<>();
        Player<StandardCard> player = game.getPlayers().get(playerIndex);
        int handSize = player.getHandSize();

        for (int i = 0; i < handSize; i++) {
            views.add(CardImage.create(i, handSize, isBasic));
        }

        return views;
    }

    @Override
    public boolean isCardSelected(StandardCard card) {
        return false;
    }

    @Override
    public void selectCard(StandardCard card) {}

    @Override
    public void deselectCard(StandardCard card) {}

    @Override
    public void clearSelectedCards() {}

    @Override
    public void ensureInRound() {}
}
