package main.java.edu.hust.cardgame.strategy;

import main.java.edu.hust.cardgame.model.CardCollection;
import main.java.edu.hust.cardgame.model.CardType;

import java.util.List;

public class CardSorter <C extends CardType> {
    private final CardComparisonStrategy<C> strategy;

    public CardSorter(CardComparisonStrategy<C> strategy) {
        this.strategy = strategy;
    }

    public void sort(CardCollection<C> cardCollection) {
        List<C> cards = cardCollection.getAllCards();
        cards.sort(strategy::compare);
    }
}
