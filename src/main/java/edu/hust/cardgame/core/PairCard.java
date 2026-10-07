package edu.hust.cardgame.core;

import java.util.Objects;

public class PairCard<A extends Enum<A>, B extends Enum<B>> implements CardType {
    private final A a;
    private final B b;

    public PairCard(A a, B b) {
        this.a = Objects.requireNonNull(a, "first value");
        this.b = Objects.requireNonNull(b, "second value");
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

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof PairCard<?, ?> card)) {
            return false;
        }
        return a == card.a && b == card.b;
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }
}
