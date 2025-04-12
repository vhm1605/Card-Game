package module;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CardSorter {
    private final CardComparisonStrategy strategy;

    public CardSorter() {
        this.strategy = new DefaultComparisonStrategy();
    }

    public CardSorter(CardComparisonStrategy strategy) {
        this.strategy = strategy;
    }

    public void sort(CardCollection cardCollection) {
        List<Card> cards = cardCollection.getAllCards();
        cards.sort(new Comparator<Card>() {
            @Override
            public int compare(Card c1, Card c2) {
                return strategy.compare(c1, c2);
            }
        });
    }
}
