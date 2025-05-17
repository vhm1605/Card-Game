//package module;
//
//import java.util.Comparator;
//import java.util.List;
//
//public class CardSorter {
//    private final CardComparisonStrategy strategy;
//
//    public CardSorter() {
//        this.strategy = new DefaultComparisonStrategy();
//    }
//
//    public CardSorter(CardComparisonStrategy strategy) {
//        this.strategy = strategy;
//    }
//
//    public void sort(CardCollection cardCollection) {
//        List<StandardCard> cards = cardCollection.getAllCards();
//        cards.sort(new Comparator<StandardCard>() {
//            @Override
//            public int compare(StandardCard firstCard, StandardCard secondCard) {
//                return strategy.compare(firstCard, secondCard);
//            }
//        });
//    }
//}
package module;

import java.util.Comparator;
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

