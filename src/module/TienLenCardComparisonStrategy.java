package module;

public class TienLenCardComparisonStrategy implements CardComparisonStrategy {
    @Override
    public int compare(Card firstCard, Card secondCard) {
        int faceDiff = TienLenCardOrderingStrategy.getFaceOrder(firstCard) - TienLenCardOrderingStrategy.getFaceOrder(secondCard);
        if (faceDiff != 0) return faceDiff;

        return TienLenCardOrderingStrategy.getSuitOrder(firstCard) - TienLenCardOrderingStrategy.getSuitOrder(secondCard);
    }
}
