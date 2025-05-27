package main.java.edu.hust.cardgame.logic.tienlen;

import main.java.edu.hust.cardgame.core.DeckFactory;
import main.java.edu.hust.cardgame.model.CardCollection;
import main.java.edu.hust.cardgame.model.CardComboType;
import main.java.edu.hust.cardgame.model.PlayerState;
import main.java.edu.hust.cardgame.model.StandardCard;
import main.java.edu.hust.cardgame.strategy.CardOrderingStrategy;
import main.java.edu.hust.cardgame.strategy.TienLenCardOrderingStrategy;

public class TienLenMienBac extends TienLen implements Cloneable {
    public final CardOrderingStrategy<StandardCard> order = new TienLenCardOrderingStrategy();
    public TienLenMienBac(int numberOfPlayers, int numberOfAIPlayers, DeckFactory<StandardCard> factory) {
        super(numberOfPlayers, numberOfAIPlayers, factory);
        this.playValidator = new TienLenMienBacPlayValidator();
    }

    @Override
    public TienLenMienBac clone() {
        return (TienLenMienBac) super.clone();
    }

    @Override
    public boolean isValidPlay() {
        sorter.sort(selectedCards);
        if (players.get(currentPlayerIndex).getState() == PlayerState.OUT_OF_CARDS) {
            moveToNextPlayer();
            return false;
        }

        if (selectedCards.getSize() == 0) {
            return false;
        }

        CardCollection<StandardCard> remainingCards = players.get(currentPlayerIndex).getHand().clone();
        remainingCards.removeCards(selectedCards);
        if (!remainingCards.isEmpty()) {
            boolean allTwos = true;
            for (int i = 0; i < remainingCards.getSize(); i++) {
                if (order.getFaceOrder(remainingCards.getCardAt(i)) != 15) {
                    allTwos = false;
                    break;
                }
            }
            if (allTwos) {
                return false;
            }
            if (remainingCards.getSize() == 4
                    && determineComboType(remainingCards) == CardComboType.FOUR_OF_A_KIND) {
                return false;
            }
        }

        CardComboType selectedCardsType = determineComboType(selectedCards);
        CardComboType lastPlayCardsType = determineComboType(lastPlayedCards);

        if (lastPlayCardsType == CardComboType.INVALID_PLAY) {
            return handleInitialPlay(selectedCardsType);
        }
        if (selectedCardsType == lastPlayCardsType) {
            return handleSameCombo(selectedCardsType);
        }
        return handleDifferentCombo(selectedCardsType, lastPlayCardsType);
    }

    private boolean handleInitialPlay(CardComboType selectedCardsType) {
        if (selectedCardsType == CardComboType.INVALID_PLAY) {
            return false;
        }
        flag = 0;
        return true;
    }

    private boolean handleSameCombo(CardComboType type) {
        int lastIndexOfSelectedCards = selectedCards.getSize() - 1;
        int lastIndexOfLastPlayCards = lastPlayedCards.getSize() - 1;
        StandardCard highestCardOfSelectedCards = selectedCards.getCardAt(lastIndexOfSelectedCards);
        StandardCard highestCardOfLastPlayCards = lastPlayedCards.getCardAt(lastIndexOfLastPlayCards);

        int faceOfHighestCardOfSelectedCards = order.getFaceOrder(highestCardOfSelectedCards);
        int faceOfHighestCardOfLastPlayCards = order.getFaceOrder(highestCardOfLastPlayCards);
        int suitOfHighestCardOfSelectedCards = order.getSuitOrder(highestCardOfSelectedCards);
        int suitOfHighestCardOfLastPlayCards = order.getSuitOrder(highestCardOfLastPlayCards);

        switch (type) {
            case SINGLE:
                if (faceOfHighestCardOfSelectedCards == 15) {
                    if (faceOfHighestCardOfLastPlayCards != 15) {
                        flag = 0;
                        return true;
                    }
                    if (suitOfHighestCardOfSelectedCards > suitOfHighestCardOfLastPlayCards) {
                        flag = 0;
                        return true;
                    }
                    return false;
                }
                if (suitOfHighestCardOfSelectedCards == suitOfHighestCardOfLastPlayCards && faceOfHighestCardOfSelectedCards > faceOfHighestCardOfLastPlayCards) {
                    flag = 0;
                    return true;
                }
                return false;

            case STRAIGHT:
                if (selectedCards.getSize() == lastPlayedCards.getSize()
                        && comparer.compare(highestCardOfSelectedCards, highestCardOfLastPlayCards) > 0) {
                    flag = 0;
                    return true;
                }
                return false;

            case PAIR:
                if (TienLenUtils.getColorGroup(highestCardOfSelectedCards) == TienLenUtils.getColorGroup(highestCardOfLastPlayCards) && faceOfHighestCardOfSelectedCards > faceOfHighestCardOfLastPlayCards) {
                    flag = 0;
                    return true;
                }
                if (faceOfHighestCardOfSelectedCards == 15 && faceOfHighestCardOfLastPlayCards == 15) {
                    if (suitOfHighestCardOfSelectedCards > suitOfHighestCardOfLastPlayCards) {
                        flag = 0;
                        return true;
                    }
                    return false;
                }
                return false;

            case TRIPLE:
                if (faceOfHighestCardOfSelectedCards > faceOfHighestCardOfLastPlayCards && suitOfHighestCardOfSelectedCards == suitOfHighestCardOfLastPlayCards && order.getSuitOrder(selectedCards.getCardAt(0)) == order.getSuitOrder(lastPlayedCards.getCardAt(0))) {
                    flag = 0;
                    return true;
                }
                return false;
            case FOUR_OF_A_KIND:
                if (faceOfHighestCardOfSelectedCards > faceOfHighestCardOfLastPlayCards) {
                    flag = 0;
                    return true;
                }
            default:
                return false;
        }
    }

    private boolean handleDifferentCombo(CardComboType selectedCardsType, CardComboType lastPlayCardsType) {
        if (selectedCardsType == CardComboType.FOUR_OF_A_KIND && lastPlayCardsType == CardComboType.SINGLE && order.getFaceOrder(lastPlayedCards.getCardAt(0)) == 15) {
            flag = 0;
            return true;
        }
        return false;
    }
}
