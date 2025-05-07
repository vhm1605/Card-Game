package module;

import java.util.ArrayList;
import java.util.List;
// STUPID
/*
 * Try combination of size K to 1
 * (or up to maxSize if specified)
 * until a valid play
 */
public class BacktrackingStrategy implements AIStrategy {
    private final int maxSize;

    public BacktrackingStrategy(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public CardCollection decideMove(PlayableGame game, Player ai) {
        CardCollection hand = game.getHandOf(ai).clone();
        int n = hand.getSize();

        // build list of sizes to attempt
        List<Integer> sizes = new ArrayList<>();
        int limit = (maxSize > 0) ? Math.min(maxSize, n) : n;
        for (int s = limit; s >= 1; s--) {
            sizes.add(s);
        }
//        if (maxSize > 0) {
//            sizes.add(Math.min(maxSize, n));
//        } else {
//            for (int s = n; s >= 1; s--) sizes.add(s);
//        }

        for (int size : sizes) {
            if (search(hand, game, size, 0, new ArrayList<>())) {
                return game.getSelectedCards().clone();
            }
        }
        // pass when found nothing
        return new CardCollection();
    }

    private boolean search(CardCollection hand,
                           PlayableGame game,
                           int size,
                           int start,
                           List<Integer> idxs)
    {
        if (idxs.size() == size) {
            game.getSelectedCards().empty();
            for (int i : idxs) game.getSelectedCards().addCard(hand.getCardAt(i));
            if (game.isValidPlay()) return true;
            return false;
        }
        for (int i = start; i <= hand.getSize() - (size - idxs.size()); i++) {
            idxs.add(i);
            if (search(hand, game, size, i + 1, idxs)) return true;
            idxs.remove(idxs.size() - 1);
        }
        return false;
    }
}
