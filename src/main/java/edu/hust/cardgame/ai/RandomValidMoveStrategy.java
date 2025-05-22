package main.java.edu.hust.cardgame.ai;

import main.java.edu.hust.cardgame.model.CardCollection;
import main.java.edu.hust.cardgame.model.CardType;
import main.java.edu.hust.cardgame.model.Player;
import main.java.edu.hust.cardgame.core.SheddingGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class RandomValidMoveStrategy<C extends CardType, G extends SheddingGame<C>> implements AIStrategy<C, G> {
    private final int maxAttempts;

    public RandomValidMoveStrategy(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    @Override
    public CardCollection<C> decideMove(G game, Player<C> ai) {
        List<C> hand = new ArrayList<>(game.getHandOf(ai).getAllCards());
        int handSize = hand.size();
        int lastSize = game.getSelectedCards().getSize();
        // if nobody has played yet, we still try at least 1 card
        int desired  = (lastSize > 0) ? Math.min(lastSize, handSize) : 1;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            Collections.shuffle(hand);

            // build a candidate of exactly 'desired' cards
            CardCollection<C> candidate = new CardCollection<>();
            for (int i = 0; i < desired; i++) {
                candidate.addCard(hand.get(i));
            }

            // log what we're testing vs. what the table has
            String pickStr = candidate.getAllCards().stream()
                    .map(C::toString)
                    .collect(Collectors.joining(","));
            String lastStr = game.getSelectedCards().getAllCards().stream()
                    .map(C::toString)
                    .collect(Collectors.joining(","));
            System.out.printf("[Random] AI %s attempt %d → [%s] vs last [%s]%n",
                    ai.getName(), attempt, pickStr, lastStr);

            // try it
            game.getSelectedCards().empty();
            candidate.getAllCards().forEach(game.getSelectedCards()::addCard);
            if (game.isValidPlay()) {
                System.out.printf("[Random] AI %s plays [%s]%n%n", ai.getName(), pickStr);
                return candidate;
            }
        }

        // if we never found a valid play, pass
        System.out.printf("[Random] AI %s passes%n%n", ai.getName());
        return new CardCollection<>();
    }
}
