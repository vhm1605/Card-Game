package module;
import java.util.List;

public interface ScoringGame<C extends CardType> extends GeneralGame<C> {
    void deal();
    int getScoreFor(Player<C> p);
    List<Integer> getRanking();
}
