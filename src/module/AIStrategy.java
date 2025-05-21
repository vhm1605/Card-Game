package module;

public interface AIStrategy<C extends CardType, G extends GeneralGame<C>> {
    CardCollection<C> decideMove(G game, Player<C> ai);
}
