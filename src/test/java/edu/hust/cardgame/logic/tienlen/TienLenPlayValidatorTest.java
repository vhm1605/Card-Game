package edu.hust.cardgame.logic.tienlen;

import edu.hust.cardgame.core.CardCollection;
import edu.hust.cardgame.core.CardComboType;
import edu.hust.cardgame.core.Face;
import edu.hust.cardgame.core.StandardCard;
import edu.hust.cardgame.core.Suit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TienLenPlayValidatorTest {
    private final TienLenMienNamPlayValidator southern = new TienLenMienNamPlayValidator();
    private final TienLenMienBacPlayValidator northern = new TienLenMienBacPlayValidator();

    @Test
    void southernVariantAcceptsPairsRegardlessOfColor() {
        assertEquals(
                CardComboType.PAIR,
                southern.determineComboType(cards(
                        card(Face.FIVE, Suit.SPADES),
                        card(Face.FIVE, Suit.HEARTS)
                ))
        );
    }

    @Test
    void northernVariantRequiresPairsOfTheSameColor() {
        assertEquals(
                CardComboType.INVALID_PLAY,
                northern.determineComboType(cards(
                        card(Face.FIVE, Suit.SPADES),
                        card(Face.FIVE, Suit.HEARTS)
                ))
        );
        assertEquals(
                CardComboType.PAIR,
                northern.determineComboType(cards(
                        card(Face.FIVE, Suit.SPADES),
                        card(Face.FIVE, Suit.CLUBS)
                ))
        );
    }

    @Test
    void northernStraightRequiresOneSuit() {
        assertEquals(
                CardComboType.STRAIGHT,
                northern.determineComboType(cards(
                        card(Face.THREE, Suit.SPADES),
                        card(Face.FOUR, Suit.SPADES),
                        card(Face.FIVE, Suit.SPADES)
                ))
        );
        assertEquals(
                CardComboType.INVALID_PLAY,
                northern.determineComboType(cards(
                        card(Face.THREE, Suit.SPADES),
                        card(Face.FOUR, Suit.CLUBS),
                        card(Face.FIVE, Suit.SPADES)
                ))
        );
    }

    @Test
    void southernVariantRecognizesConsecutivePairs() {
        assertEquals(
                CardComboType.CONSECUTIVE_PAIRS,
                southern.determineComboType(cards(
                        card(Face.THREE, Suit.SPADES), card(Face.THREE, Suit.HEARTS),
                        card(Face.FOUR, Suit.CLUBS), card(Face.FOUR, Suit.DIAMONDS),
                        card(Face.FIVE, Suit.SPADES), card(Face.FIVE, Suit.DIAMONDS)
                ))
        );
    }

    @Test
    void straightsCannotContainTwos() {
        CardCollection<StandardCard> straightWithTwo = cards(
                card(Face.KING, Suit.SPADES),
                card(Face.ACE, Suit.CLUBS),
                card(Face.TWO, Suit.HEARTS)
        );

        assertEquals(CardComboType.INVALID_PLAY, southern.determineComboType(straightWithTwo));
    }

    @Test
    void southernVariantAllowsFourOfAKindToBeatThreeConsecutivePairs() {
        TienLenMienNam game = southernGame();
        game.flag = 0;
        game.lastPlayedCards = cards(
                card(Face.THREE, Suit.SPADES), card(Face.THREE, Suit.HEARTS),
                card(Face.FOUR, Suit.CLUBS), card(Face.FOUR, Suit.DIAMONDS),
                card(Face.FIVE, Suit.SPADES), card(Face.FIVE, Suit.DIAMONDS)
        );
        game.selectedCards = cards(
                card(Face.SIX, Suit.SPADES), card(Face.SIX, Suit.CLUBS),
                card(Face.SIX, Suit.DIAMONDS), card(Face.SIX, Suit.HEARTS)
        );

        assertTrue(game.isValidPlay());
    }

    @Test
    void southernVariantAllowsFourConsecutivePairsToBeatFourOfAKind() {
        TienLenMienNam game = southernGame();
        game.flag = 0;
        game.lastPlayedCards = cards(
                card(Face.THREE, Suit.SPADES), card(Face.THREE, Suit.CLUBS),
                card(Face.THREE, Suit.DIAMONDS), card(Face.THREE, Suit.HEARTS)
        );
        game.selectedCards = cards(
                card(Face.FOUR, Suit.SPADES), card(Face.FOUR, Suit.HEARTS),
                card(Face.FIVE, Suit.CLUBS), card(Face.FIVE, Suit.DIAMONDS),
                card(Face.SIX, Suit.SPADES), card(Face.SIX, Suit.DIAMONDS),
                card(Face.SEVEN, Suit.CLUBS), card(Face.SEVEN, Suit.HEARTS)
        );

        assertTrue(game.isValidPlay());
    }

    private static TienLenMienNam southernGame() {
        return new TienLenMienNam(
                4,
                0,
                new edu.hust.cardgame.core.EnumPairDeckFactory<>(
                        Face.class,
                        Suit.class,
                        StandardCard::new
                )
        );
    }

    private static StandardCard card(Face face, Suit suit) {
        return new StandardCard(face, suit);
    }

    private static CardCollection<StandardCard> cards(StandardCard... cards) {
        CardCollection<StandardCard> collection = new CardCollection<>();
        for (StandardCard card : cards) {
            collection.addCard(card);
        }
        return collection;
    }
}
