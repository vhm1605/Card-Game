package module;

public class TienLenCardOrderingStrategy extends DefaultCardOrderingStrategy {
    public static int getFaceOrder(Card card) {
        return switch (card.getFace()) {
            case THREE -> 3;
            case FOUR -> 4;
            case FIVE -> 5;
            case SIX -> 6;
            case SEVEN -> 7;
            case EIGHT -> 8;
            case NINE -> 9;
            case TEN -> 10;
            case JACK -> 11;
            case QUEEN -> 12;
            case KING -> 13;
            case ACE -> 14;
            case TWO -> 15;
            default -> 0;
        };
    }
}
