package main.java.edu.hust.cardgame.logic.tienlen;

import main.java.edu.hust.cardgame.model.CardCollection;
import main.java.edu.hust.cardgame.model.CardComboType;
import main.java.edu.hust.cardgame.model.StandardCard;
import main.java.edu.hust.cardgame.strategy.CardOrderingStrategy;
import main.java.edu.hust.cardgame.strategy.CardSorter;
import main.java.edu.hust.cardgame.strategy.TienLenCardComparisonStrategy;
import main.java.edu.hust.cardgame.strategy.TienLenCardOrderingStrategy;

public class TienLenMienNamPlayValidator implements TienLenPlayValidator {
    public final CardOrderingStrategy<StandardCard> order = new TienLenCardOrderingStrategy();
    CardSorter<StandardCard> sorter = new CardSorter<>(new TienLenCardComparisonStrategy());

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
        if (numberOfCards % 2 == 0 && isValidStraight(cardCollection) != CardComboType.STRAIGHT) {
            return isValidConsecutivePairs(cardCollection);
        }
        return isValidStraight(cardCollection);
    }

    private CardComboType evaluateTwoCardCombination(CardCollection<StandardCard> cardCollection) {
        StandardCard firstCard = cardCollection.getCardAt(0);
        StandardCard secondCard = cardCollection.getCardAt(1);
        if (TienLenUtils.isMatchingPair(firstCard, secondCard)) {
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

    private CardComboType isValidConsecutivePairs(CardCollection<StandardCard> cardCollection) {
        int numberOfCards = cardCollection.getSize();
        for (int i = 0; i < numberOfCards; i += 2) {
            if (!TienLenUtils.isMatchingPair(cardCollection.getCardAt(i), cardCollection.getCardAt(i + 1))) {
                return CardComboType.INVALID_PLAY;
            }
        }
        CardCollection<StandardCard> tempCards = new CardCollection<>();
        for (int i = 0; i < numberOfCards; i += 2) {
            tempCards.addCard(cardCollection.getCardAt(i));
        }
        if (isValidStraight(tempCards) == CardComboType.STRAIGHT) {
            return CardComboType.CONSECUTIVE_PAIRS;
        }
        return CardComboType.INVALID_PLAY;
    }

    private CardComboType isValidStraight(CardCollection<StandardCard> cardCollection) {
        int numberOfCards = cardCollection.getSize();
        if (numberOfCards < 3) {
            return CardComboType.INVALID_PLAY;
        }
        sorter.sort(cardCollection);
        int highestFaceOrder = order.getFaceOrder(cardCollection.getCardAt(numberOfCards - 1));
        if (highestFaceOrder == 15) {
            return CardComboType.INVALID_PLAY;
        }
        for (int i = 0; i < numberOfCards - 1; i++) {
            int currentFaceOrder = order.getFaceOrder(cardCollection.getCardAt(i));
            int nextFaceOrder = order.getFaceOrder(cardCollection.getCardAt(i + 1));
            if (nextFaceOrder - currentFaceOrder != 1) {
                return CardComboType.INVALID_PLAY;
            }
        }
        return CardComboType.STRAIGHT;
    }
}
