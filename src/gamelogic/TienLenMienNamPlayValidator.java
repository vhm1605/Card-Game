package gamelogic;

import module.CardCollection;
import module.CardSorter;
import module.TienLenCardComparisonStrategy;
import module.TienLenCardOrderingStrategy;

public class TienLenMienNamPlayValidator implements TienLenPlayValidator {
    @Override
    public CardComboType determineComboType(CardCollection cardCollection) {
        int numberOfCards = cardCollection.getSize();
        if (numberOfCards == 1) {
            return CardComboType.SINGLE;
        } else if (numberOfCards == 2) {
            if (CardGameUtils.isMatchingPair(cardCollection.getCardAt(0), cardCollection.getCardAt(1))) {
                return CardComboType.PAIR;
            }
        } else if (numberOfCards == 3) {
            if (CardGameUtils.isMatchingPair(cardCollection.getCardAt(0), cardCollection.getCardAt(1)) &&
                    CardGameUtils.isMatchingPair(cardCollection.getCardAt(0), cardCollection.getCardAt(2))) {
                return CardComboType.TRIPLE;
            } else {
                return isValidStraight(cardCollection);
            }
        } else if (numberOfCards == 4) {
            if (CardGameUtils.isMatchingPair(cardCollection.getCardAt(0), cardCollection.getCardAt(1)) &&
                    CardGameUtils.isMatchingPair(cardCollection.getCardAt(0), cardCollection.getCardAt(2)) &&
                    CardGameUtils.isMatchingPair(cardCollection.getCardAt(0), cardCollection.getCardAt(3))) {
                return CardComboType.FOUR_OF_A_KIND;
            } else {
                return isValidStraight(cardCollection);
            }
        } else if (numberOfCards == 0) {
            return CardComboType.INVALID_PLAY;
        } else if (numberOfCards % 2 == 0 && isValidStraight(cardCollection) != CardComboType.STRAIGHT) {
            for (int i = 0; i < numberOfCards; i += 2) {
                if (!CardGameUtils.isMatchingPair(cardCollection.getCardAt(i), cardCollection.getCardAt(i + 1))) {
                    return CardComboType.INVALID_PLAY;
                }
            }
            CardCollection tempCards = new CardCollection();
            for (int i = 0; i < numberOfCards; i += 2) {
                tempCards.addCard(cardCollection.getCardAt(i));
            }
            if (isValidStraight(tempCards) == CardComboType.STRAIGHT) {
                return CardComboType.CONSECUTIVE_PAIRS;
            }
        } else {
            return isValidStraight(cardCollection);
        }
        return CardComboType.INVALID_PLAY;
    }

    @Override
    public CardComboType isValidStraight(CardCollection cardCollection) {
        int numberOfCards = cardCollection.getSize();
        if (numberOfCards < 3) {
            return CardComboType.INVALID_PLAY;
        }
        CardSorter sorter = new CardSorter(new TienLenCardComparisonStrategy());
        sorter.sort(cardCollection);
        int highestFaceOrder = TienLenCardOrderingStrategy.getFaceOrder(cardCollection.getCardAt(numberOfCards - 1));
        if (highestFaceOrder == 15) {
            return CardComboType.INVALID_PLAY;
        }
        for (int i = 0; i < numberOfCards - 1; i++) {
            int currentFaceOrder = TienLenCardOrderingStrategy.getFaceOrder(cardCollection.getCardAt(i));
            int nextFaceOrder = TienLenCardOrderingStrategy.getFaceOrder(cardCollection.getCardAt(i + 1));
            if (nextFaceOrder - currentFaceOrder != 1) {
                return CardComboType.INVALID_PLAY;
            }
        }
        return CardComboType.STRAIGHT;
    }
}
