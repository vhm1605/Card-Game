package module;

public interface PlayableGame {
    CardCollection getSelectedCards();
    boolean isValidPlay();
    void playGame();
    void passTurn();
    // AI's cards
    CardCollection getHandOf(Player ai);
    int getHandSizeOf(Player ai);
}
