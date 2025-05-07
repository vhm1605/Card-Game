package module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class IDKStrategy implements AIStrategy {
    private final Comparator<Card> ordering;

    public IDKStrategy(Comparator<Card> ordering) {
        this.ordering = ordering;
    }

    @Override
    public CardCollection decideMove(PlayableGame game, Player ai) {
        // get last-play size, if no one has played, play with 1
        int lastSize = game.getSelectedCards().getSize();
        int desired = lastSize == 0 ? 1 : lastSize;

        List<Card> hand = new ArrayList<>(game.getHandOf(ai).getAllCards());
        Collections.sort(hand, ordering);   // e.g. TienLen or poker order

        // generate combinations
        int n = hand.size();
        int[] idxs = new int[desired];
        for (int i = 0; i < desired; i++) {
            idxs[i] = i;
        }

        outer:
        while (true) {
            CardCollection pick = new CardCollection();
            for (int i : idxs) pick.addCard(hand.get(i));

            // test
            game.getSelectedCards().empty();
            pick.getAllCards().forEach(game.getSelectedCards()::addCard);
            if (game.isValidPlay()) {
                return pick;
            }

            int i;
            for (i = desired - 1; i >= 0; i--) {
                if (idxs[i] < n - desired + i) {
                    idxs[i]++;
                    for (int j = i + 1; j < desired; j++) {
                        idxs[j] = idxs[j - 1] + 1;
                    }
                    continue outer;
                }
            }
            // no more combos
            break;
        }
        // nothing found -> pass
        return new CardCollection();
    }
}
