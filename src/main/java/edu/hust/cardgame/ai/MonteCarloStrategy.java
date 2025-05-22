package main.java.edu.hust.cardgame.ai;

import main.java.edu.hust.cardgame.core.SheddingGame;
import main.java.edu.hust.cardgame.model.CardCollection;
import main.java.edu.hust.cardgame.model.Player;
import main.java.edu.hust.cardgame.model.StandardCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MonteCarloStrategy<C extends StandardCard, G extends SheddingGame<C>>
        implements AIStrategy<C, G> {

    private final int simulationsPerMove;
    private final Random rng = new Random();

    public MonteCarloStrategy(int simulationsPerMove) {
        this.simulationsPerMove = simulationsPerMove;
    }

    @Override
    public CardCollection<C> decideMove(G game, Player<C> ai) {
        List<CardCollection<C>> legal = generateLegalMoves(game, ai);
        if (legal.isEmpty()) {
            return new CardCollection<>();
        }

        CardCollection<C> best = legal.get(0).clone();
        double bestRate = -1;

        for (CardCollection<C> move : legal) {
            int wins = 0;
            for (int i = 0; i < simulationsPerMove; i++) {
                if (simulate(game, ai, move)) {
                    wins++;
                }
            }
            double rate = wins / (double) simulationsPerMove;
            if (rate > bestRate) {
                bestRate = rate;
                best = move.clone();
            }
        }
        return best;
    }

    private List<CardCollection<C>> generateLegalMoves(G game, Player<C> ai) {
        List<CardCollection<C>> out = new ArrayList<>();
        CardCollection<C> hand = game.getHandOf(ai).clone();
        int lastSize = game.getLastPlayedCards().getSize();
        int minSize = lastSize > 0 ? lastSize : 1;

        for (int sz = minSize; sz <= hand.getSize(); sz++) {
            backtrack(game, hand, sz, 0, new ArrayList<>(), out);
            if (!out.isEmpty()) break;
        }
        return out;
    }

    private void backtrack(G game,
                           CardCollection<C> hand,
                           int targetSize,
                           int start,
                           List<Integer> path,
                           List<CardCollection<C>> out) {
        if (path.size() == targetSize) {
            CardCollection<C> sel = game.getSelectedCards();
            sel.empty();
            for (int idx : path) sel.addCard(hand.getCardAt(idx));
            if (game.isValidPlay()) {
                out.add(sel.clone());
            }
            return;
        }
        int need = targetSize - path.size();
        for (int i = start; i <= hand.getSize() - need; i++) {
            path.add(i);
            backtrack(game, hand, targetSize, i + 1, path, out);
            path.remove(path.size() - 1);
        }
    }

    private boolean simulate(G game, Player<C> ai, CardCollection<C> move) {
        return rng.nextBoolean();
    }
}
