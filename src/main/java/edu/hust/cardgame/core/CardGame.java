package main.java.edu.hust.cardgame.core;

import java.util.ArrayList;
import java.util.List;

public abstract class CardGame<C extends CardType> implements GeneralGame<C> {
    protected List<Player<C>> players = new ArrayList<>();
    protected CardCollection<C> deck = new CardCollection<>();
    protected int numberOfPlayers;
    protected int numberOfAIPlayers;
    protected int currentPlayerIndex;
    protected DeckFactory<C> deckFactory;

    protected CardGame() {
    }

    public CardGame(int numberOfPlayers, int numberOfAIPlayers, DeckFactory<C> deckFactory) {
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfAIPlayers = numberOfAIPlayers;
        this.currentPlayerIndex = 0;
        this.deckFactory = deckFactory;
    }

    protected void initializeDeck() {
        deck.empty();
        for (C card : deckFactory.createDeck()) {
            deck.addCard(card);
        }
        deck.shuffle();
    }

    public List<Player<C>> getPlayers() {
        return players;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public Player<C> getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void showAllPlayerHands() {
        for (Player<C> player : players) {
            player.showHand();
        }
    }

    @Override
    public CardCollection<C> getSelectedCards() {
        return new CardCollection<>();
    }

    @Override
    public boolean isValidPlay() {
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
    public CardCollection<C> getHandOf(Player<C> ai) {
        return ai.getHand();
    }

//    @Override
//    public int getHandSizeOf(Player<C> ai) {
//        return ai.getHandSize();
//    }

//    public CardCollection<C> getDeck() {
//        return deck.clone(); // Return a clone to prevent external modification
//    }

    public abstract void deal();

    public abstract void startNewGame();

    public abstract boolean isGameOver();

    public abstract void resetGame();
}
