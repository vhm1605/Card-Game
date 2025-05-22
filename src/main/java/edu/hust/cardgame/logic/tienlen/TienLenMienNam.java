package main.java.edu.hust.cardgame.logic.tienlen;

import main.java.edu.hust.cardgame.core.DeckFactory;
import main.java.edu.hust.cardgame.model.CardComboType;
import main.java.edu.hust.cardgame.strategy.CardOrderingStrategy;
import main.java.edu.hust.cardgame.model.PlayerState;
import main.java.edu.hust.cardgame.model.StandardCard;
import main.java.edu.hust.cardgame.strategy.TienLenCardOrderingStrategy;

public class TienLenMienNam extends TienLen {
    public final CardOrderingStrategy<StandardCard> order = new TienLenCardOrderingStrategy();
    public TienLenMienNam(int numberOfPlayers, int numberOfAIPlayers, DeckFactory<StandardCard> factory) {
        super(numberOfPlayers, numberOfAIPlayers, factory);
        this.playValidator = new TienLenMienNamPlayValidator();
    }

    @Override
    public boolean isValidPlay() {
        sorter.sort(selectedCards);
        if (players.get(currentPlayerIndex).getState() == PlayerState.OUT_OF_CARDS) {
            moveToNextPlayer();
            return false;
        }
        if (flag == 1 && getCurrentPlayer().getCardAt(0).equals(startingCard) && (selectedCards.getSize() == 0 || !selectedCards.getCardAt(0).equals(startingCard))) {
            return false;
        }
        if (selectedCards.getSize() == 0) {
            return false;
        }

        CardComboType selectedCardsType = determineComboType(selectedCards);
        CardComboType lastPlayCardsType = determineComboType(lastPlayedCards);
        int lastIndexOfSelectedCards = selectedCards.getSize() - 1;
        int lastIndexOfLastPlayCards = lastPlayedCards.getSize() - 1;

        if (lastPlayCardsType == CardComboType.INVALID_PLAY) {
            return handleInitialPlay(selectedCardsType);
        }
        if (selectedCardsType == lastPlayCardsType) {
            return handleSameCombo(selectedCardsType, lastIndexOfSelectedCards, lastIndexOfLastPlayCards);
        }
        return handleDifferentCombo(selectedCardsType, lastPlayCardsType, lastIndexOfSelectedCards, lastIndexOfLastPlayCards);
    }

    private boolean handleInitialPlay(CardComboType selectedCardsType) {
        if (selectedCardsType == CardComboType.INVALID_PLAY) {
            return false;
        }
        flag = 0;
        return true;
    }

    private boolean handleSameCombo(CardComboType type, int lastIndexOfSelectedCards, int lastIndexOfLastPlayCards) {
        switch (type) {
            case STRAIGHT:
                if (lastIndexOfSelectedCards == lastIndexOfLastPlayCards && comparer.compare(selectedCards.getCardAt(lastIndexOfSelectedCards), lastPlayedCards.getCardAt(lastIndexOfLastPlayCards)) > 0) {
                    flag = 0;
                    return true;
                }
            case CONSECUTIVE_PAIRS:
                if ((lastIndexOfSelectedCards > lastIndexOfLastPlayCards) || (lastIndexOfSelectedCards == lastIndexOfLastPlayCards && comparer.compare(selectedCards.getCardAt(lastIndexOfSelectedCards), lastPlayedCards.getCardAt(lastIndexOfLastPlayCards)) > 0)) {
                    flag = 0;
                    return true;
                }
                return false;
            default:
                if (comparer.compare(selectedCards.getCardAt(lastIndexOfSelectedCards), lastPlayedCards.getCardAt(lastIndexOfLastPlayCards)) > 0) {
                    flag = 0;
                    return true;
                }
        }
        return false;
    }

    private boolean handleDifferentCombo(CardComboType selectedCardsType, CardComboType lastPlayCardsType, int lastIndexOfSelectedCards, int lastIndexOfLastPlayCards) {
        if (selectedCardsType == CardComboType.FOUR_OF_A_KIND && lastPlayCardsType == CardComboType.CONSECUTIVE_PAIRS && lastIndexOfLastPlayCards + 1 == 6) {
            flag = 0;
            return true;
        }
        if (lastPlayCardsType == CardComboType.FOUR_OF_A_KIND && selectedCardsType == CardComboType.CONSECUTIVE_PAIRS && lastIndexOfLastPlayCards + 1 >= 8) {
            flag = 0;
            return true;
        }
        if (order.getFaceOrder(lastPlayedCards.getCardAt(lastIndexOfLastPlayCards)) == 15) {
            return handleHighestSpecial(selectedCardsType, lastIndexOfLastPlayCards, lastIndexOfSelectedCards);
        }
        return false;
    }

    private boolean handleHighestSpecial(CardComboType selectedCardsType, int lastIndexOfLastPlayCards, int lastIndexOfSelectedCards) {
        if (lastIndexOfLastPlayCards == 0) {
            if (selectedCardsType == CardComboType.FOUR_OF_A_KIND || selectedCardsType == CardComboType.CONSECUTIVE_PAIRS) {
                flag = 0;
                return true;
            }
        } else if (lastIndexOfLastPlayCards == 1) {
            if (selectedCardsType == CardComboType.FOUR_OF_A_KIND || (selectedCardsType == CardComboType.CONSECUTIVE_PAIRS && lastIndexOfSelectedCards + 1 >= 8)) {
                flag = 0;
                return true;
            }
        }
        return false;
    }
}
