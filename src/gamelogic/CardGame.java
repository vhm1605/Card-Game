package gamelogic;

import java.util.ArrayList;
import java.util.List;

import module.*;

public abstract class CardGame implements PlayableGame {
    protected List<Player> players = new ArrayList<>();
    protected CardCollection deck = new CardCollection();;
    protected int numberOfPlayers;
    protected int numberOfAIPlayers;
    protected int currentPlayerIndex;

    CardGame() {}

    public CardGame(int numberOfPlayers, int numberOfAIPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfAIPlayers = numberOfAIPlayers;
        this.currentPlayerIndex = 0;
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

    // new
    @Override
    public CardCollection getSelectedCards() {
        // default: games that don’t use “selectedCards” can override
        return new CardCollection();
    }

    @Override
    public boolean isValidPlay() {
        // default: if no rule validation, everything is valid
        return true;
    }

    @Override
    public void playGame() {
        resetGame();
    }

    @Override
    public void passTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % numberOfPlayers;
    }

    @Override
    public CardCollection getHandOf(Player ai) {
        return ai.getHand();
    }

    @Override
    public int getHandSizeOf(Player ai) {
        return ai.getHandSize();
    }

    public abstract void deal();

    public abstract void startNewGame();

//    public abstract void playGame();

    public abstract boolean isGameOver();

    public abstract void resetGame();
}
