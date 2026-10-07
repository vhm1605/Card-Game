package edu.hust.cardgame.logic;

import edu.hust.cardgame.core.DeckFactory;
import edu.hust.cardgame.core.EnumPairDeckFactory;
import edu.hust.cardgame.core.Face;
import edu.hust.cardgame.core.StandardCard;
import edu.hust.cardgame.core.Suit;
import edu.hust.cardgame.logic.bacay.BaCay;
import edu.hust.cardgame.logic.tienlen.TienLen;
import edu.hust.cardgame.logic.tienlen.TienLenMienBac;
import edu.hust.cardgame.logic.tienlen.TienLenMienNam;
import edu.hust.cardgame.strategy.BaCayScoreStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameIntegrationTest {
    private final DeckFactory<StandardCard> deckFactory =
            new EnumPairDeckFactory<>(Face.class, Suit.class, StandardCard::new);

    @Test
    void baCayDealsThreeNumericCardsToEachOfEightPlayers() {
        BaCay game = new BaCay(8, 3, new BaCayScoreStrategy(), deckFactory);

        assertEquals(8, game.getPlayers().size());
        game.getPlayers().forEach(player -> {
            assertEquals(3, player.getHandSize());
            assertTrue(player.getAllCards().stream()
                    .allMatch(card -> card.getFirst().ordinal() <= Face.TEN.ordinal()));
        });
    }

    @Test
    void gameConstructorsRejectUnsupportedPlayerCounts() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new TienLenMienNam(5, 0, deckFactory)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new BaCay(9, 0, new BaCayScoreStrategy(), deckFactory)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new TienLenMienBac(1, 0, deckFactory)
        );
    }

    @Test
    void bothTienLenVariantsRequireTheStartingCardOnTheOpeningPlay() {
        assertOpeningCardRequired(new TienLenMienNam(4, 0, deckFactory));
        assertOpeningCardRequired(new TienLenMienBac(4, 0, deckFactory));
    }

    private static void assertOpeningCardRequired(TienLen game) {
        StandardCard startingCard = game.getStartingCard();
        StandardCard anotherCard = game.getCurrentPlayer().getAllCards().stream()
                .filter(card -> !card.equals(startingCard))
                .findFirst()
                .orElseThrow();

        game.selectCard(anotherCard);
        assertFalse(game.isValidPlay());

        game.getSelectedCards().empty();
        game.selectCard(startingCard);
        assertTrue(game.isValidPlay());
        assertEquals(1, game.getFlag());
        assertTrue(game.isValidPlay());

        game.playGame();
        assertEquals(0, game.getFlag());
    }
}
