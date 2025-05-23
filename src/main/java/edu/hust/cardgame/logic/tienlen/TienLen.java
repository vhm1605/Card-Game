package main.java.edu.hust.cardgame.logic.tienlen;

import java.util.ArrayList;
import java.util.List;

import main.java.edu.hust.cardgame.ai.AIPlayer;
import main.java.edu.hust.cardgame.ai.AIStrategy;
import main.java.edu.hust.cardgame.ai.BacktrackingStrategy;
import main.java.edu.hust.cardgame.ai.RandomValidMoveStrategy;
import main.java.edu.hust.cardgame.core.CardGame;
import main.java.edu.hust.cardgame.core.DeckFactory;
import main.java.edu.hust.cardgame.core.SheddingGame;
import main.java.edu.hust.cardgame.model.*;
import main.java.edu.hust.cardgame.strategy.CardSorter;
import main.java.edu.hust.cardgame.strategy.TienLenCardComparisonStrategy;

public abstract class TienLen extends CardGame<StandardCard> implements SheddingGame<StandardCard> {
    protected CardCollection<StandardCard> lastPlayedCards = new CardCollection<>();
    protected CardCollection<StandardCard> selectedCards = new CardCollection<>();
    protected StandardCard startingCard;
    protected List<Integer> playerRanking = new ArrayList<>();
    protected int flag;
    protected TienLenPlayValidator playValidator;

    CardSorter<StandardCard> sorter = new CardSorter<>(new TienLenCardComparisonStrategy());
    TienLenCardComparisonStrategy comparer = new TienLenCardComparisonStrategy();

    public TienLen() {
    }

    public TienLen(int numberOfPlayers, int numberOfAIPlayers, DeckFactory<StandardCard> factory) {
        super(numberOfPlayers, numberOfAIPlayers, factory);
        flag = 1;
        startNewGame();
    }

    @Override
    public void deal() {
        players.clear();

        AIStrategy<StandardCard, SheddingGame<StandardCard>> aiStrategy = createAIStrategy(2);

        int numberOfHumanPlayers = numberOfPlayers - numberOfAIPlayers;
        for (int i = 0; i < numberOfHumanPlayers; i++) {
            players.add(new Player<>());
        }

        for (int i = 0; i < numberOfAIPlayers; i++) {
            players.add(new AIPlayer<>(aiStrategy));
        }

        for (Player<StandardCard> player : players) {
            for (int j = 0; j < 13; j++) {
                player.receiveCard(deck.removeCardAt(0));
            }
            sorter.sort(player.getHand());
        }
    }

    private AIStrategy<StandardCard, SheddingGame<StandardCard>> createAIStrategy(int aiMode) {
        return switch (aiMode) {
            case 1 -> new RandomValidMoveStrategy<>(1000);
            case 2 -> new BacktrackingStrategy<>();
            // case 3 -> new SomethingStrategy<>(10000);
            default -> new RandomValidMoveStrategy<>(1000);
        };
    }

    public void startNewGame() {
        initializeDeck();
        deal();
        if (flag == 1) {
            for (int i = 1; i < numberOfPlayers; i++) {
                if (comparer.compare(players.get(i).getCardAt(0), players.get(currentPlayerIndex).getCardAt(0)) < 0) {
                    currentPlayerIndex = i;
                }
            }
            startingCard = players.get(currentPlayerIndex).getCardAt(0);
        } else {
            currentPlayerIndex = playerRanking.getFirst() - 1;
            playerRanking.clear();
        }
        resetRound();
    }

    public void resetRound() {
        for (Player<StandardCard> player : players) {
            if (player.getState() != PlayerState.OUT_OF_CARDS) {
                player.setState(PlayerState.IN_ROUND);
            }
        }
        lastPlayedCards.empty();
    }

    public void playGame() {
        if (isGameOver()) {
            return;
        }
        if (getCurrentPlayer().getState() == PlayerState.IN_ROUND) {
            sorter.sort(selectedCards);
            if (isValidPlay()) {
                processValidPlay();
            }
            moveToNextPlayer();
            updatePlayerRankingIfNeeded();
        }
    }

    private void processValidPlay() {
        lastPlayedCards = selectedCards.clone();
        getCurrentPlayer().useCards(lastPlayedCards);
        if (getCurrentPlayer().getAllCards().isEmpty()) {
            playerRanking.add(currentPlayerIndex + 1);
            getCurrentPlayer().setState(PlayerState.OUT_OF_CARDS);
            flag--;
            restorePlayersWhoPassed();
        }
    }

    private void restorePlayersWhoPassed() {
        for (Player<StandardCard> player : players) {
            if (player.getState() == PlayerState.PASSED) {
                player.setState(PlayerState.IN_ROUND);
            }
        }
    }

    private void updatePlayerRankingIfNeeded() {
        if (playerRanking.size() == numberOfPlayers - 1) {
            playerRanking.add(currentPlayerIndex + 1);
            getCurrentPlayer().setState(PlayerState.OUT_OF_CARDS);
        }
    }

    public void passTurn() {
        int numberOfInRoundPlayers = countPlayersInRound();
        if (lastPlayedCards.getAllCards().isEmpty()) {
            selectedCards.empty();
            return;
        }
        if (flag < 0 && numberOfInRoundPlayers == 2) {
            players.get(currentPlayerIndex).setState(PlayerState.PASSED);
            moveToNextPlayer();
            selectedCards.empty();
            flag = 0;
            return;
        }
        if (numberOfInRoundPlayers <= 2) {
            if (numberOfInRoundPlayers == 1) {
                resetRound();
                moveToNextPlayer();
            } else {
                moveToNextPlayer();
                resetRound();
            }
            selectedCards.empty();
            return;
        }
        if (flag != 1) {
            players.get(currentPlayerIndex).setState(PlayerState.PASSED);
            moveToNextPlayer();
            selectedCards.empty();
        }
    }

    private int countPlayersInRound() {
        int numberOfInRoundPlayers = 0;
        for (Player<StandardCard> player : players) {
            if (player.getState() == PlayerState.IN_ROUND) {
                numberOfInRoundPlayers++;
            }
        }
        return numberOfInRoundPlayers;
    }

    public void moveToNextPlayer() {
        int start = currentPlayerIndex;
        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % numberOfPlayers;
            if (currentPlayerIndex == start) {
                break;
            }
        } while (players.get(currentPlayerIndex).getState() != PlayerState.IN_ROUND);
    }

    public abstract boolean isValidPlay();

    @Override
    public CardComboType determineComboType(CardCollection<StandardCard> cardCollection) {
        return playValidator.determineComboType(cardCollection);
    }

    public boolean isGameOver() {
        return (playerRanking.size() == numberOfPlayers);
    }

    public void resetGame() {
        selectedCards.empty();
        lastPlayedCards.empty();
        for (int i = 0; i < numberOfPlayers; i++) {
            players.get(i).getAllCards().clear();
            players.get(i).setState(PlayerState.IN_ROUND);
        }
        startNewGame();
    }
    int i = 0;
    public String playerRankingToString() {
        StringBuilder builder = new StringBuilder();
        for (Integer rank : playerRanking) {
            i++;
            builder.append("Rank " + i + ": Player ").append(rank).append("\n");
        }
        return builder.toString();
    }

    public CardCollection<StandardCard> getSelectedCards() {
        return selectedCards;
    }

    public void deselectCard(StandardCard card) {
        selectedCards.getAllCards().remove(card);
    }

    public void selectCard(StandardCard card) {
        if (!selectedCards.contains(card) && players.get(currentPlayerIndex).contains(card)) {
            selectedCards.addCard(card);
        }
    }

    @Override
    public CardCollection<StandardCard> getLastPlayedCards() {
        return lastPlayedCards;
    }
}
