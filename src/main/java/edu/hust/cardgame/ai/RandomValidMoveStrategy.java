package main.java.edu.hust.cardgame.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import main.java.edu.hust.cardgame.core.SheddingGame;
import main.java.edu.hust.cardgame.model.CardCollection;
import main.java.edu.hust.cardgame.model.CardType;
import main.java.edu.hust.cardgame.model.Player;


public class RandomValidMoveStrategy<C extends CardType, G extends SheddingGame<C>> implements AIStrategy<C, G> {
    private final int maxAttempts;

    public RandomValidMoveStrategy(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    @Override
    public CardCollection<C> decideMove(G game, Player<C> ai) {
        List<C> hand = new ArrayList<>(game.getHandOf(ai).getAllCards());
        int handSize = hand.size();
        int lastSize = game.getLastPlayedCards().getSize();
        int desired = (lastSize > 0) ? Math.min(lastSize, handSize) : 1;

        // Generate all possible combinations of 'desired' size
        List<List<C>> combos = new ArrayList<>();
        generateCombos(hand, desired, 0, new ArrayList<>(), combos);

        // Shuffle for randomness
        Collections.shuffle(combos);

        for (List<C> combo : combos) {
            CardCollection<C> candidate = new CardCollection<>();
            combo.forEach(candidate::addCard);

            // Try it
            game.getSelectedCards().empty();
            candidate.getAllCards().forEach(game.getSelectedCards()::addCard);
            if (game.isValidPlay()) {
                return candidate;
            }
        }

        // If no valid play found, pass
        return new CardCollection<>();
    }

    // Helper method to generate combinations
    private void generateCombos(List<C> hand, int desired, int start, List<C> path, List<List<C>> out) {
        if (path.size() == desired) {
            out.add(new ArrayList<>(path));
            return;
        }
        for (int i = start; i < hand.size(); i++) {
            path.add(hand.get(i));
            generateCombos(hand, desired, i + 1, path, out);
            path.remove(path.size() - 1);
        }
    }
}
