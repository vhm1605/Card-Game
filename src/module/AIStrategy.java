package module;

public interface AIStrategy {
    CardCollection decideMove(PlayableGame game, Player ai);
}
