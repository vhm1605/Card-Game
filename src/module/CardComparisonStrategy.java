package module;

public interface CardComparisonStrategy<C extends CardType> {
    int compare(C firstCard, C secondCard);
}
