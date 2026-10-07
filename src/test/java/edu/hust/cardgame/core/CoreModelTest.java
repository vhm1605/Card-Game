package edu.hust.cardgame.core;

import edu.hust.cardgame.strategy.BaCayScoreStrategy;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CoreModelTest {
    private final DeckFactory<StandardCard> deckFactory =
            new EnumPairDeckFactory<>(Face.class, Suit.class, StandardCard::new);

    @Test
    void standardDeckContains52UniqueCards() {
        List<StandardCard> deck = deckFactory.createDeck();

        assertEquals(52, deck.size());
        assertEquals(52, new HashSet<>(deck).size());
    }

    @Test
    void cardCollectionDoesNotExposeItsMutableList() {
        CardCollection<StandardCard> cards = new CardCollection<>();
        cards.addCard(new StandardCard(Face.ACE, Suit.SPADES));

        assertThrows(UnsupportedOperationException.class, () -> cards.getAllCards().clear());
        assertEquals(1, cards.getSize());
    }

    @Test
    void baCayScoreUsesTheOnesDigitOfTheHandTotal() {
        CardCollection<StandardCard> hand = new CardCollection<>();
        hand.addCard(new StandardCard(Face.THREE, Suit.SPADES));
        hand.addCard(new StandardCard(Face.SEVEN, Suit.CLUBS));
        hand.addCard(new StandardCard(Face.NINE, Suit.HEARTS));

        assertEquals(9, new BaCayScoreStrategy().computeScore(hand));
    }
}
