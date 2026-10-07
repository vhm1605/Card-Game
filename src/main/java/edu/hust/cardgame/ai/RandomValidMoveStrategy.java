package edu.hust.cardgame.ai;

import edu.hust.cardgame.core.SheddingGame;
import edu.hust.cardgame.core.CardCollection;
import edu.hust.cardgame.core.CardType;
import edu.hust.cardgame.core.Player;

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

            CardCollection<C> sel = game.getSelectedCards();
            sel.empty();
            candidate.getAllCards().forEach(sel::addCard);

            if (game.isValidPlay()) {
                return candidate;
            }

            sel.empty();
        }

        return new CardCollection<>();
    }
}
