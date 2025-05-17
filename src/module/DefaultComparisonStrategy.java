package module;

public class DefaultComparisonStrategy implements CardComparisonStrategy<StandardCard> {
    @Override
    public int compare(StandardCard firstCard, StandardCard secondCard) {
        int faceDiff = DefaultCardOrderingStrategy.getFaceOrder(firstCard) - DefaultCardOrderingStrategy.getFaceOrder(secondCard);
        if (faceDiff != 0) {
            return faceDiff;
        }
        return DefaultCardOrderingStrategy.getSuitOrder(firstCard) - DefaultCardOrderingStrategy.getSuitOrder(secondCard);
    }
}
