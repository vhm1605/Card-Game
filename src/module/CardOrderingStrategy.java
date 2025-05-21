package module;

public interface CardOrderingStrategy <C extends CardType> {
    int getFaceOrder(C card);
    int getSuitOrder(C card);
}
