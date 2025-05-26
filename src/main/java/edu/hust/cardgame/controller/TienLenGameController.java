package main.java.edu.hust.cardgame.controller;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import main.java.edu.hust.cardgame.assets.imageaction.BackgroundImage;
import main.java.edu.hust.cardgame.assets.imageaction.CardImage;
import main.java.edu.hust.cardgame.assets.soundaction.ClickSound;
import main.java.edu.hust.cardgame.logic.tienlen.TienLen;
import main.java.edu.hust.cardgame.model.CardCollection;
import main.java.edu.hust.cardgame.model.Player;
import main.java.edu.hust.cardgame.model.PlayerState;
import main.java.edu.hust.cardgame.model.StandardCard;
import main.java.edu.hust.cardgame.ai.AIPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class TienLenGameController implements GameController {

    private final TienLen game;

    public TienLenGameController(TienLen game) {
        this.game = game;
    }

    @Override
    public int getPlayerCount() {
        return game.getPlayers().size();
    }

    @Override
    public int getCurrentPlayerIndex() {
        return game.getCurrentPlayerIndex();
    }

    @Override
    public String getPlayerName(int index) {
        return "Player " + (index + 1);
    }

    @Override
    public List<ImageView> getLastPlayedCardImages(boolean isBasic) {
        List<ImageView> views = new ArrayList<>();
        CardCollection<StandardCard> lastPlay = game.getLastPlayedCards();
        for (int i = 0; i < lastPlay.getSize(); i++) {
            views.add(CardImage.create(i, lastPlay.getSize(), lastPlay.getCardAt(i), isBasic));
        }
        return views;
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
    public boolean isCurrentPlayerAI() {
        return game.getCurrentPlayer() instanceof AIPlayer<?, ?>;
    }

    @Override
    public void makeAIMove() {
        AIPlayer<StandardCard, TienLen> ai = (AIPlayer<StandardCard, TienLen>) game.getCurrentPlayer();
        ai.makeMove(game);
    }

    @Override
    public boolean isValidPlay() {
        return game.isValidPlay();
    }

    @Override
    public void play() {
        game.playGame();
    }

    @Override
    public void passTurn() {
        game.passTurn();
    }

    @Override
    public boolean isGameOver() {
        return game.isGameOver();
    }

    @Override
    public String getRankingText() {
        return game.playerRankingToString();
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
    public void playClickSound() {
        ClickSound.play();
    }

    @Override
    public boolean isCardSelected(StandardCard card) {
        return game.getSelectedCards().getAllCards().contains(card);
    }

    @Override
    public void selectCard(StandardCard card) {
        game.selectCard(card);
    }

    @Override
    public void deselectCard(StandardCard card) {
        game.deselectCard(card);
    }

    @Override
    public void clearSelectedCards() {
        game.getSelectedCards().empty();
    }

    @Override
    public void ensureInRound() {
        while (game.getCurrentPlayer().getState() != PlayerState.IN_ROUND) {
            game.moveToNextPlayer();
        }
    }
}
