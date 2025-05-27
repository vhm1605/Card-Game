package main.java.edu.hust.cardgame.logic.tienlen;

import main.java.edu.hust.cardgame.core.CardCollection;
import main.java.edu.hust.cardgame.core.CardComboType;
import main.java.edu.hust.cardgame.core.StandardCard;
import main.java.edu.hust.cardgame.strategy.CardOrderingStrategy;
import main.java.edu.hust.cardgame.strategy.CardSorter;
import main.java.edu.hust.cardgame.strategy.TienLenCardComparisonStrategy;
import main.java.edu.hust.cardgame.strategy.TienLenCardOrderingStrategy;

public class TienLenMienBacPlayValidator implements TienLenPlayValidator {
    CardSorter<StandardCard> sorter = new CardSorter<>(new TienLenCardComparisonStrategy());
    private final CardOrderingStrategy<StandardCard> order = new TienLenCardOrderingStrategy();

    @Override
    public CardComboType determineComboType(CardCollection<StandardCard> cardCollection) {
        int numberOfCards = cardCollection.getSize();
        if (numberOfCards == 0) {
            return CardComboType.INVALID_PLAY;
        }
        sorter.sort(cardCollection);
        if (numberOfCards == 1) {
            return CardComboType.SINGLE;
        }
        if (numberOfCards == 2) {
            return evaluateTwoCardCombination(cardCollection);
        }
        if (numberOfCards == 3) {
            return evaluateThreeCardCombination(cardCollection);
        }
        if (numberOfCards == 4) {
            return evaluateFourCardCombination(cardCollection);
        }
        return isValidStraight(cardCollection);
    }

    private CardComboType evaluateTwoCardCombination(CardCollection<StandardCard> cardCollection) {
        StandardCard firstCard = cardCollection.getCardAt(0);
        StandardCard secondCard = cardCollection.getCardAt(1);
        boolean sameFace = TienLenUtils.isMatchingPair(firstCard, secondCard);
        boolean sameColor = TienLenUtils.getColorGroup(firstCard) == TienLenUtils.getColorGroup(secondCard);
        if (sameFace && sameColor) {
            return CardComboType.PAIR;
        }
        return CardComboType.INVALID_PLAY;
    }

    private CardComboType evaluateThreeCardCombination(CardCollection<StandardCard> cardCollection) {
        StandardCard firstCard = cardCollection.getCardAt(0);
        StandardCard secondCard = cardCollection.getCardAt(1);
        StandardCard thirdCard = cardCollection.getCardAt(2);
        if (TienLenUtils.isMatchingPair(firstCard, secondCard) && TienLenUtils.isMatchingPair(firstCard, thirdCard)) {
            return CardComboType.TRIPLE;
        }
        return isValidStraight(cardCollection);
    }

    private CardComboType evaluateFourCardCombination(CardCollection<StandardCard> cardCollection) {
        StandardCard firstCard = cardCollection.getCardAt(0);
        StandardCard secondCard = cardCollection.getCardAt(1);
        StandardCard thirdCard = cardCollection.getCardAt(2);
        StandardCard fourthCard = cardCollection.getCardAt(3);
        if (TienLenUtils.isMatchingPair(firstCard, secondCard) && TienLenUtils.isMatchingPair(firstCard, thirdCard) && TienLenUtils.isMatchingPair(firstCard, fourthCard)) {
            return CardComboType.FOUR_OF_A_KIND;
        }
        return isValidStraight(cardCollection);
    }

    private CardComboType isValidStraight(CardCollection<StandardCard> cardCollection) {
        int numberOfCards = cardCollection.getSize();
        if (numberOfCards < 3) {
            return CardComboType.INVALID_PLAY;
        }
        int highestFaceOrder = order.getFaceOrder(cardCollection.getCardAt(numberOfCards - 1));
        if (highestFaceOrder == 15) {
            return CardComboType.INVALID_PLAY;
        }
        for (int i = 0; i < numberOfCards - 1; i++) {
            StandardCard currentCard = cardCollection.getCardAt(i);
            StandardCard nextCard = cardCollection.getCardAt(i + 1);
            int currentFaceOrder = order.getFaceOrder(currentCard);
            int nextFaceOrder = order.getFaceOrder(nextCard);
            if (nextFaceOrder - currentFaceOrder != 1) {
                return CardComboType.INVALID_PLAY;
            }
            int currentSuitOrder = order.getSuitOrder(currentCard);
            int nextSuitOrder = order.getSuitOrder(nextCard);
            if (nextSuitOrder - currentSuitOrder != 0) {
                return CardComboType.INVALID_PLAY;
            }
        }
        return CardComboType.STRAIGHT;
    }
}
