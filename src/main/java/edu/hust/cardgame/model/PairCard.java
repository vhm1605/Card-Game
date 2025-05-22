package main.java.edu.hust.cardgame.model;

public class PairCard<A extends Enum<A>, B extends Enum<B>> implements CardType {
    private final A a;
    private final B b;

    public PairCard(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A getFirst() {
        return a;
    }
    public B getSecond(){
        return b;
    }

    @Override
    public String toString() {
        return a.name() + "_" + b.name();
    }
}
