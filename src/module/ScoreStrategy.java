package module;

public interface ScoreStrategy<C extends CardType> {
    int computeScore(CardCollection<C> hand);
}
