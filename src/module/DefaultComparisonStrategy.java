package module;

public class DefaultComparisonStrategy implements CardComparisonStrategy {
    @Override
    public int compare(Card firstCard, Card secondCard) {
        int faceDiff = DefaultCardOrderingStrategy.getFaceOrder(firstCard) - DefaultCardOrderingStrategy.getFaceOrder(secondCard);
        if (faceDiff != 0) {
            return faceDiff;
        }
        return DefaultCardOrderingStrategy.getSuitOrder(firstCard) - DefaultCardOrderingStrategy.getSuitOrder(secondCard);
    }
}
