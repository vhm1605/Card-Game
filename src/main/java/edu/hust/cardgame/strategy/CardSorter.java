package edu.hust.cardgame.strategy;

import edu.hust.cardgame.core.CardCollection;
import edu.hust.cardgame.core.CardType;

public class CardSorter <C extends CardType> {
    private final CardComparisonStrategy<C> strategy;

    public CardSorter(CardComparisonStrategy<C> strategy) {
        this.strategy = strategy;
    }

    public void sort(CardCollection<C> cardCollection) {
        cardCollection.sort(strategy::compare);
    }
}
