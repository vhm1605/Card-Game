package gamelogic;

import java.util.ArrayList;
import java.util.List;

import module.*;

public abstract class CardGame<C extends CardType> implements GeneralGame<C> {
//    protected List<Player> players = new ArrayList<>();
    protected List<Player<C>> players = new ArrayList<>();
    protected CardCollection<C> deck = new CardCollection<>();
    protected int numberOfPlayers;
    protected int numberOfAIPlayers;
    protected int currentPlayerIndex;
    protected DeckFactory<C> deckFactory; // new

    CardGame() {}

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

    // new
    @Override
    public CardCollection<C> getSelectedCards() {
        // default: games that don’t use “selectedCards” can override
        return new CardCollection<>();
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
    public CardCollection<C> getHandOf(Player<C> ai) {
//        CardCollection<C> hand = (CardCollection<C>) ai.getHand();
//        return hand;
        return ai.getHand();
    }

    @Override
    public int getHandSizeOf(Player<C> ai) {
        return ai.getHandSize();
    }

    public abstract void deal();

    public abstract void startNewGame();

    public abstract boolean isGameOver();

    public abstract void resetGame();
}
