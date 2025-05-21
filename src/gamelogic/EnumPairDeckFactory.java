package gamelogic;

import module.PairCard;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class EnumPairDeckFactory<A extends Enum<A>, B extends Enum<B>, C extends PairCard<A, B>> implements DeckFactory<C> {
    private final Class<A> classA;
    private final Class<B> classB;
    private final BiFunction<A, B, C> ctor;

    public EnumPairDeckFactory(
            Class<A> classA,
            Class<B> classB,
            BiFunction<A, B, C> ctor
    ) {
        this.classA = classA;
        this.classB = classB;
        this.ctor = ctor;
    }

    @Override
    public List<C> createDeck() {
        List<C> deck = new ArrayList<>();
        for (A a : classA.getEnumConstants()) {
            for (B b : classB.getEnumConstants()) {
                deck.add(ctor.apply(a, b));
            }
        }
        return deck;
    }
}
