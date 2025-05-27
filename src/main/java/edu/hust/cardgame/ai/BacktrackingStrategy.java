package main.java.edu.hust.cardgame.ai;

import main.java.edu.hust.cardgame.core.SheddingGame;
import main.java.edu.hust.cardgame.core.CardCollection;
import main.java.edu.hust.cardgame.core.Player;
import main.java.edu.hust.cardgame.core.CardType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BacktrackingStrategy<C extends CardType, G extends SheddingGame<C>> implements AIStrategy<C, G> {
    @Override
    public CardCollection<C> decideMove(G game, Player<C> ai) {
        CardCollection<C> hand = game.getHandOf(ai).clone();
        int handSize = hand.getSize();

        for (int size = 1; size <= handSize; size++) {
            if (search(game, hand, size, 0, new ArrayList<>())) {
                CardCollection<C> play = game.getSelectedCards().clone();
                game.getSelectedCards().empty();
                return play;
            }
        }

        return new CardCollection<>();
    }

    private boolean search(SheddingGame<C> game,
                           CardCollection<C> hand,
                           int size,
                           int start,
                           List<Integer> idxs) {
        if (idxs.size() == size) {
            CardCollection<C> sel = game.getSelectedCards();
            sel.empty();
            idxs.forEach(i -> sel.addCard(hand.getCardAt(i)));

            boolean ok = game.isValidPlay();
            return ok;
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