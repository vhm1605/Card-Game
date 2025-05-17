package module;

public class TienLenCardOrderingStrategy extends DefaultCardOrderingStrategy {
    public static int getFaceOrder(StandardCard card) {
        return switch (card.getFirst()) {
            case ACE -> 14;
            case TWO -> 15;
            default -> DefaultCardOrderingStrategy.getFaceOrder(card);
        };
    }
}
