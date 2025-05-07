package module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
// STILL STUPID AS HELL
/*
 * Try up to maxAttempts random until it finds one that is valid
 * choosing a random hand‐size when there is no last play cards
 */
public class RandomValidMoveStrategy implements AIStrategy {
    private final int maxAttempts;

    public RandomValidMoveStrategy(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    @Override
    public CardCollection decideMove(PlayableGame game, Player ai) {
        int handSize = game.getHandSizeOf(ai);
        int lastSize = game.getSelectedCards().getSize();

        // how many cards to try
        int desired = (lastSize == 0) ? 1 : Math.min(lastSize, handSize);

        List<Card> hand = new ArrayList<>(game.getHandOf(ai).getAllCards());

        // Try up to maxAttempts times
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            Collections.shuffle(hand);

            CardCollection pick = new CardCollection();
            pick.addCard(hand.get(0)); // pick ONE when lastSize = 0

            if (lastSize != 0) {
                for (int i = 1; i < desired; i++) {
                    pick.addCard(hand.get(i));
                }
            }

            // test if it’s valid
            game.getSelectedCards().empty();
            pick.getAllCards().forEach(game.getSelectedCards()::addCard);
            if (game.isValidPlay()) {
                return pick;
            }
        }
        // No valid play → pass
        return new CardCollection();
    }
}
