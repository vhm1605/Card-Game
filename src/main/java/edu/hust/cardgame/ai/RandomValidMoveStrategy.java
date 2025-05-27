package main.java.edu.hust.cardgame.ai;

import main.java.edu.hust.cardgame.core.SheddingGame;
import main.java.edu.hust.cardgame.model.CardCollection;
import main.java.edu.hust.cardgame.model.CardType;
import main.java.edu.hust.cardgame.model.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomValidMoveStrategy<C extends CardType, G extends SheddingGame<C>> implements AIStrategy<C, G> {
    private final int maxAttempts;
    private final Random rng = new Random();

    public RandomValidMoveStrategy(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    @Override
    public CardCollection<C> decideMove(G game, Player<C> ai) {
        List<C> hand = new ArrayList<>(game.getHandOf(ai).getAllCards());
        int handSize = hand.size();

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            int subsetSize = rng.nextInt(handSize) + 1;
            Collections.shuffle(hand, rng);

            CardCollection<C> candidate = new CardCollection<>();
            for (int i = 0; i < subsetSize; i++) {
                candidate.addCard(hand.get(i));
            }

            // set and test
            CardCollection<C> sel = game.getSelectedCards();
            sel.empty();
            candidate.getAllCards().forEach(sel::addCard);

            if (game.isValidPlay()) {
                return candidate;
            }

            sel.empty();
        }

        // no valid move → pass
        return new CardCollection<>();
    }
}