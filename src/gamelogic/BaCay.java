package gamelogic;

import module.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import java.util.*;

public class BaCay extends CardGame<StandardCard> implements ScoringGame<StandardCard> {
    private final ScoreStrategy<StandardCard> scoreStrategy;
    private final List<Integer> playerScores = new ArrayList<>();
    private boolean gameOver = false;

    public BaCay(int numberOfPlayers, int numberOfAIPlayers, ScoreStrategy<StandardCard> scoreStrategy, DeckFactory<StandardCard> factory) {
        super(numberOfPlayers, numberOfAIPlayers, factory);
        this.scoreStrategy = scoreStrategy;
        startNewGame();
    }

    @Override
    public void deal() {
        players.clear();
        // create human players
        int humans = numberOfPlayers - numberOfAIPlayers;
        for (int i = 0; i < humans; i++) {
            players.add(new Player<>());
        }
        // create AI players
        for (int i = 0; i < numberOfAIPlayers; i++) {
            players.add(new Player<>());
        }
        // deal 3 cards each
        for (Player<StandardCard> p : players) {
            for (int i = 0; i < 3; i++) {
                p.receiveCard(deck.removeCardAt(0));
            }
        }
    }

    @Override
    public void startNewGame() {
        deck.empty();
        initializeDeck();   // new 52 card deck
        playerScores.clear();
        gameOver = false;
        deal();
    }

    @Override
    public void playGame() {
        if (gameOver) return;
        // compute player's score
        for (Player<StandardCard> p : players) {
            playerScores.add(scoreStrategy.computeScore(p.getHand()));
        }
        gameOver = true;
    }

    @Override
    public boolean isGameOver() {
        return gameOver;
    }

    @Override
    public void resetGame() {
        for (Player<StandardCard> p : players) {
            p.clearHand();
        }
        startNewGame();
    }

    public List<Integer> getPlayerRanking() {
        List<Integer> ranking = new ArrayList<>();
        for (int i = 1; i <= playerScores.size(); i++) {
            ranking.add(i);
        }
        ranking.sort((p1, p2) -> playerScores.get(p2 - 1) - playerScores.get(p1 - 1));
        return ranking;
    }

    public int getPlayerScore(int playerIndex) {
        return playerScores.get(playerIndex);
    }

    public List<Integer> getPlayerScores() {
        return Collections.unmodifiableList(playerScores);
    }

    @Override
    public int getScoreFor(Player<StandardCard> player) {
        return scoreStrategy.computeScore(player.getHand());
    }

    @Override
    public List<Integer> getRanking() {
        return getPlayerRanking();
    }
}
