package module;

public class TienLenCardComparisonStrategy implements CardComparisonStrategy<StandardCard> {
    @Override
    public int compare(StandardCard firstCard, StandardCard secondCard) {
        int faceDiff = TienLenCardOrderingStrategy.getFaceOrder(firstCard) - TienLenCardOrderingStrategy.getFaceOrder(secondCard);
        if (faceDiff != 0) return faceDiff;

        return TienLenCardOrderingStrategy.getSuitOrder(firstCard) - TienLenCardOrderingStrategy.getSuitOrder(secondCard);
    }
}
