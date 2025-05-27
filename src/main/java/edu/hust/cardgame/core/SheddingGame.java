package main.java.edu.hust.cardgame.core;

public interface SheddingGame<C extends CardType> extends GeneralGame<C> {
    CardCollection<C> getLastPlayedCards();
    CardComboType determineComboType(CardCollection<C> cards);
}
