package gamelogic;

import module.AIPlayer;
import module.CardCollection;
import module.Player;
import module.ScoreStrategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import java.util.*;

public class BaCay extends CardGame {
    private final ScoreStrategy scoreStrategy;
    private final List<Integer> playerScores = new ArrayList<>();
    private boolean gameOver = false;

    public BaCay(int numberOfPlayers, int numberOfAIPlayers, ScoreStrategy scoreStrategy) {
        super(numberOfPlayers, numberOfAIPlayers);
        this.scoreStrategy = scoreStrategy;
        startNewGame();
    }

    @Override
    public void deal() {
        players.clear();
        // create human players
        int humans = numberOfPlayers - numberOfAIPlayers;
        for (int i = 0; i < humans; i++) {
            players.add(new Player());
        }
        // create AI players
        for (int i = 0; i < numberOfAIPlayers; i++) {
            players.add(new AIPlayer());
        }
        // deal 3 cards each
        for (Player p : players) {
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
        for (Player p : players) {
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
        for (Player p : players) {
            p.clearHand();
        }
        startNewGame();
    }

    public List<Integer> getPlayerRanking() {
        List<Integer> ranking = new ArrayList<>();
        for (int i = 1; i <= playerScores.size(); i++) {
            ranking.add(i);
        }

        Collections.sort(ranking, new Comparator<Integer>() {
            @Override
            public int compare(Integer p1, Integer p2) {
                int score1 = playerScores.get(p1 - 1);
                int score2 = playerScores.get(p2 - 1);
                return score2 - score1;
            }
        });

        return ranking;
    }

    public int getPlayerScore(int playerIndex) {
        return playerScores.get(playerIndex);
    }

    public List<Integer> getPlayerScores() {
        return Collections.unmodifiableList(playerScores);
    }

}
