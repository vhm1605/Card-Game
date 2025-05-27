package main.java.edu.hust.cardgame.ai;

import main.java.edu.hust.cardgame.core.CardCollection;
import main.java.edu.hust.cardgame.core.CardType;
import main.java.edu.hust.cardgame.core.Player;
import main.java.edu.hust.cardgame.core.SheddingGame;

import java.util.ArrayList;
import java.util.List;

public class GreedyStrategy<C extends CardType, G extends SheddingGame<C>> implements AIStrategy<C, G> {

    @Override
    public CardCollection<C> decideMove(G game, Player<C> ai) {
        List<C> hand = new ArrayList<>(game.getHandOf(ai).getAllCards());
        int handSize = hand.size();
        int lastSize = game.getLastPlayedCards().getSize();
        int desired = (lastSize > 0) ? Math.min(lastSize, handSize) : 1;

        CardCollection<C> bestMove = new CardCollection<>();
        boolean found = findSmallestValidMove(game, hand, desired, 0, new ArrayList<>(), bestMove);

        if (found) {
            return bestMove;
        }
        return new CardCollection<>();
    }

    // Try all combinations of 'size' cards from hand, lexicographically
    private boolean findSmallestValidMove(G game, List<C> hand, int size, int start,
                                          List<Integer> indices, CardCollection<C> result) {
        if (indices.size() == size) {
            CardCollection<C> candidate = new CardCollection<>();
            for (int idx : indices) {
                candidate.addCard(hand.get(idx));
            }
            // Try this candidate
            game.getSelectedCards().empty();
            candidate.getAllCards().forEach(game.getSelectedCards()::addCard);

            // debug
            System.out.println("Trying: " + candidate.getAllCards());

            if (game.isValidPlay()) {
                result.getAllCards().clear();
                result.getAllCards().addAll(candidate.getAllCards());
                return true;
            }
            return false;
        }
        int remain = size - indices.size();
        for (int i = start; i <= hand.size() - remain; i++) {
            indices.add(i);
            if (findSmallestValidMove(game, hand, size, i + 1, indices, result)) {
                return true;
            }
            indices.remove(indices.size() - 1);
        }
        return false;
    }
}