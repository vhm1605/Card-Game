package module;

public class TienLenCardOrderingStrategy extends DefaultCardOrderingStrategy {
    public static int getFaceOrder(Card card) {
        return switch (card.getFace()) {
            case ACE -> 14;
            case TWO -> 15;
            default -> DefaultCardOrderingStrategy.getFaceOrder(card);
        };
    }
}
