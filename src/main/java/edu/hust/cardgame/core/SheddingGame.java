package main.java.edu.hust.cardgame.core;

import main.java.edu.hust.cardgame.model.CardComboType;
import main.java.edu.hust.cardgame.model.CardCollection;
import main.java.edu.hust.cardgame.model.CardType;

public interface SheddingGame<C extends CardType> extends GeneralGame<C> {
    CardCollection<C> getLastPlayedCards();
    CardComboType determineComboType(CardCollection<C> cards);
}
