package module;

import gamelogic.CardComboType;
import gamelogic.TienLen;

import java.util.ArrayList;
import java.util.List;

public class BacktrackingStrategy<C extends StandardCard,
        G extends SheddingGame<C>>
        implements AIStrategy<C, G> {

    @Override
    public CardCollection<C> decideMove(G game, Player<C> ai) {
        CardCollection<C> hand = game.getHandOf(ai).clone();
        int lastSize = game.getLastPlayedCards().getSize();
        int target = lastSize > 0 ? lastSize : 1;

        System.out.printf("[Backtrack] AI %s trying combos of size %d%n",
                ai.getName(), target);

        boolean found = search(game, hand, target, 0, new ArrayList<>());
        return found
                ? game.getSelectedCards().clone()
                : new CardCollection<>();
    }

    @SuppressWarnings("unchecked")
    private boolean search(SheddingGame<C> game,
                           CardCollection<C> hand,
                           int size,
                           int start,
                           List<Integer> idxs) {
        if (idxs.size() == size) {
            CardCollection<C> sel = game.getSelectedCards();
            sel.empty();
            for (int i : idxs) {
                sel.addCard(hand.getCardAt(i));
            }
            if (game.isValidPlay()) {
                return true;
            }

            // TienLen‐only bomb: beating a TWO with 4‐of‐a‐kind or 3‐pairs
            if (game instanceof TienLen && size == 1) {
                // unchecked cast from CardCollection<C> to CardCollection<StandardCard>
                CardCollection<StandardCard> stdSel =
                        (CardCollection<StandardCard>) (CardCollection<?>) sel;

                StandardCard card0 = stdSel.getCardAt(0);
                if (card0.getFirst() == Face.TWO) {
                    CardComboType t = ((TienLen) game).determineComboType(stdSel);
                    if (t == CardComboType.FOUR_OF_A_KIND ||
                            t == CardComboType.CONSECUTIVE_PAIRS) {
                        return true;
                    }
                }
            }
            return false;
        }

        int remain = size - idxs.size();
        for (int i = start; i <= hand.getSize() - remain; i++) {
            idxs.add(i);
            if (search(game, hand, size, i + 1, idxs)) {
                return true;
            }
            idxs.remove(idxs.size() - 1);
        }
        return false;
    }
}
