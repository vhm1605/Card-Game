package gamelogic;

import module.*;

public class TienLenMienBacPlayValidator implements TienLenPlayValidator {
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
            StandardCard firstCard = cardCollection.getCardAt(0);
            StandardCard secondCard = cardCollection.getCardAt(1);
            boolean sameFace = TienLenCardOrderingStrategy.getFaceOrder(firstCard) ==
                    TienLenCardOrderingStrategy.getFaceOrder(secondCard);
            boolean sameColor = CardGameUtils.getColorGroup(firstCard) == CardGameUtils.getColorGroup(secondCard);
            if (sameFace && sameColor) {
                return CardComboType.PAIR;
            }
        }
        if (numberOfCards == 3) {
            StandardCard firstCard = cardCollection.getCardAt(0);
            StandardCard secondCard = cardCollection.getCardAt(1);
            StandardCard thirdCard = cardCollection.getCardAt(2);
            if (CardGameUtils.isMatchingPair(firstCard, secondCard) &&
                    CardGameUtils.isMatchingPair(firstCard, thirdCard)) {
                return CardComboType.TRIPLE;
            } else {
                return isValidStraight(cardCollection);
            }
        }
        if (numberOfCards == 4) {
            StandardCard firstCard = cardCollection.getCardAt(0);
            boolean allMatching = CardGameUtils.isMatchingPair(firstCard, cardCollection.getCardAt(1)) &&
                    CardGameUtils.isMatchingPair(firstCard, cardCollection.getCardAt(2)) &&
                    CardGameUtils.isMatchingPair(firstCard, cardCollection.getCardAt(3));
            if (allMatching) {
                return CardComboType.FOUR_OF_A_KIND;
            } else {
                return isValidStraight(cardCollection);
            }
        }
        return isValidStraight(cardCollection);
    }

    @Override
    public CardComboType isValidStraight(CardCollection<StandardCard> cardCollection) {
        int numberOfCards = cardCollection.getSize();
        if (numberOfCards < 3) {
            return CardComboType.INVALID_PLAY;
        }
        sorter.sort(cardCollection);
        int highestFaceOrder = TienLenCardOrderingStrategy.getFaceOrder(cardCollection.getCardAt(numberOfCards - 1));
        if (highestFaceOrder == 15) {
            return CardComboType.INVALID_PLAY;
        }
        for (int i = 0; i < numberOfCards - 1; i++) {
            StandardCard currentCard = cardCollection.getCardAt(i);
            StandardCard nextCard = cardCollection.getCardAt(i + 1);
            int currentFaceOrder = TienLenCardOrderingStrategy.getFaceOrder(currentCard);
            int nextFaceOrder = TienLenCardOrderingStrategy.getFaceOrder(nextCard);
            if (nextFaceOrder - currentFaceOrder != 1) {
                return CardComboType.INVALID_PLAY;
            }
            int currentSuitOrder = TienLenCardOrderingStrategy.getSuitOrder(currentCard);
            int nextSuitOrder = TienLenCardOrderingStrategy.getSuitOrder(nextCard);
            if (nextSuitOrder - currentSuitOrder != 0) {
                return CardComboType.INVALID_PLAY;
            }
        }
        return CardComboType.STRAIGHT;
    }
}
