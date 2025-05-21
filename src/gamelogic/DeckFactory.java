package gamelogic;

import module.CardType;

import java.util.List;

public interface DeckFactory<C extends CardType> {
    List<C> createDeck();
}
