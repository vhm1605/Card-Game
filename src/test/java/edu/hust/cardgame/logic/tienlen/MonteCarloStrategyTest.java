package edu.hust.cardgame.logic.tienlen;

import edu.hust.cardgame.core.CardCollection;
import edu.hust.cardgame.core.DeckFactory;
import edu.hust.cardgame.core.EnumPairDeckFactory;
import edu.hust.cardgame.core.Face;
import edu.hust.cardgame.core.StandardCard;
import edu.hust.cardgame.core.Suit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MonteCarloStrategyTest {
    private final DeckFactory<StandardCard> deckFactory =
            new EnumPairDeckFactory<>(Face.class, Suit.class, StandardCard::new);

    @Test
    @Timeout(5)
    void searchReturnsAnOpeningMoveWithoutMutatingTheLiveGame() {
        TienLenMienNam game = new TienLenMienNam(4, 4, deckFactory);
        MonteCarloStrategy strategy = new MonteCarloStrategy(10, 40, new Random(7));
        int currentPlayer = game.getCurrentPlayerIndex();
        int initialFlag = game.getFlag();
        List<Integer> handSizes = game.getPlayers().stream()
                .map(player -> player.getHandSize())
                .toList();

        CardCollection<StandardCard> move = strategy.decideMove(game, game.getCurrentPlayer());

        assertFalse(move.isEmpty());
        assertTrue(move.contains(game.getStartingCard()));
        assertEquals(currentPlayer, game.getCurrentPlayerIndex());
        assertEquals(initialFlag, game.getFlag());
        assertTrue(game.getSelectedCards().isEmpty());
        assertEquals(
                handSizes,
                game.getPlayers().stream().map(player -> player.getHandSize()).toList()
        );
    }
}
