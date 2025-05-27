package main.java.edu.hust.cardgame.logic.bacay;

import main.java.edu.hust.cardgame.core.CardGame;
import main.java.edu.hust.cardgame.core.DeckFactory;
import main.java.edu.hust.cardgame.core.ScoringGame;
import main.java.edu.hust.cardgame.model.Face;
import main.java.edu.hust.cardgame.model.Player;
import main.java.edu.hust.cardgame.model.StandardCard;
import main.java.edu.hust.cardgame.strategy.ScoreStrategy;

import java.util.ArrayList;
import java.util.List;

import java.util.*;

public class BaCay extends CardGame<StandardCard> implements ScoringGame<StandardCard> {
    private final ScoreStrategy<StandardCard> scoreStrategy;
    private final List<Integer> playerScores = new ArrayList<>();
    private boolean gameOver = false;
    private static final int MaxCardEachPlayer = 3;

    public BaCay(int numberOfPlayers, int numberOfAIPlayers, ScoreStrategy<StandardCard> scoreStrategy, DeckFactory<StandardCard> factory) {
        super(numberOfPlayers, numberOfAIPlayers, factory);
        this.scoreStrategy = scoreStrategy;
        startNewGame();
    }

    @Override
    public void deal() {
        players.clear();
        int humans = numberOfPlayers - numberOfAIPlayers;
        for (int i = 0; i < humans; i++) {
            players.add(new Player<>());
        }
        for (int i = 0; i < numberOfAIPlayers; i++) {
            players.add(new Player<>());
        }
        for (Player<StandardCard> p : players) {
            int drawn = 1;
            while (drawn <= MaxCardEachPlayer) {
                StandardCard card = deck.removeCardAt(0);
                if (card.getFirst().ordinal() <= Face.TEN.ordinal()) {
                    p.receiveCard(card);
                    drawn++;
                }
            }
        }
    }

    @Override
    public void startNewGame() {
        deck.empty();
        initializeDeck();
        playerScores.clear();
        gameOver = false;
        deal();
    }

    @Override
    public void playGame() {
        if (gameOver) {
            return;
        }
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

    @Override
    public int getScoreFor(Player<StandardCard> player) {
        return scoreStrategy.computeScore(player.getHand());
    }
}
