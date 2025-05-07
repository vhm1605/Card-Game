package module;

public class DefaultCardOrderingStrategy {
    public static int getFaceOrder(Card card) {
        return switch (card.getFace()) {
            case ACE -> 1;
            case TWO -> 2;
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
            default -> 0;
        };
    }

    public static int getSuitOrder(Card card) {
        return switch (card.getSuit()) {
            case SPADES -> 1;
            case CLUBS -> 2;
            case DIAMONDS -> 3;
            case HEARTS -> 4;
            default -> 0;
        };
    }
}
