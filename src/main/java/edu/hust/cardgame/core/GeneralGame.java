package main.java.edu.hust.cardgame.core;

public interface GeneralGame<C extends CardType> {
    CardCollection<C> getSelectedCards();
    boolean isValidPlay();
    void playGame();
    void passTurn();
    CardCollection<C> getHandOf(Player<C> p);
//    int getHandSizeOf(Player<C> p);
}
