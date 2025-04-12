package gamelogic;

import java.util.ArrayList;
import java.util.List;

import module.*;

public abstract class CardGame {
    protected List<Player> players;
    protected CardCollection deck;
    protected int numberOfPlayers;
    protected int numberOfAIPlayers;
    protected int currentPlayerIndex;
    protected int roundNumber;

    CardGame() {}

    public CardGame(int numberOfPlayers, int numberOfAIPlayers) {
        players = new ArrayList<>();
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfAIPlayers = numberOfAIPlayers;
        this.deck = new CardCollection();
        this.roundNumber = 0;
        this.currentPlayerIndex = 0;
        this.initializeDeck();
    }

    public void initializeDeck() {
        deck.empty();
        for (Face face : Face.values()) {
            for (Suit suit : Suit.values()) {
                deck.addCard(new Card(face, suit));
            }
        }
        deck.shuffle();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void showAllPlayerHands() {
        for (Player player : players) {
            player.showHand();
        }
    }

    public abstract void deal();

    public abstract void startNewGame();

    public abstract void resetGame();

    public abstract boolean isGameOver();
}
