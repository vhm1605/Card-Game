package edu.hust.cardgame.controller;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import edu.hust.cardgame.core.StandardCard;

import java.util.List;
import java.util.function.Function;

public interface GameController {

    /** Returns the number of players in the current game. */
    int getPlayerCount();

    /** Returns the zero-based index of the current player. */
    int getCurrentPlayerIndex();

    /** Returns the display name for a player. */
    String getPlayerName(int index);

    /** Creates views for the cards most recently played. */
    List<ImageView> getLastPlayedCardImages(boolean isBasic);

    /** Creates face-up card views for a player. */
    List<ImageView> getVisibleCards(int playerIndex, boolean isBasic, Function<StandardCard, Integer> onClickOffsetHandler);

    /** Creates face-down card views for a player. */
    List<ImageView> getHiddenCardImages(int playerIndex, boolean isBasic);

    /** Reports whether the current player is computer-controlled. */
    boolean isCurrentPlayerAI();

    /** Executes the current AI player's move. */
    void makeAIMove();

    /** Validates the currently selected cards. */
    boolean isValidPlay();

    /** Plays the currently selected cards. */
    void play();

    /** Passes the current turn. */
    void passTurn();

    /** Reports whether the game has ended. */
    boolean isGameOver();

    /** Returns a display-ready ranking summary. */
    String getRankingText();

    /** Resets the model for a new game. */
    void resetGame();

    /** Returns the gameplay background. */
    Background getBackgroundImage();

    /** Plays the UI click sound. */
    void playClickSound();

    /** Reports whether a card is selected. */
    boolean isCardSelected(StandardCard card);

    /** Selects a card. */
    void selectCard(StandardCard card);

    /** Deselects a card. */
    void deselectCard(StandardCard card);

    /** Clears the card selection. */
    void clearSelectedCards();

    /** Advances past players who are no longer active in the round. */
    void ensureInRound();
}
