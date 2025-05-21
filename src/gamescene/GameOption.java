package gamescene;

public class GameOption {
    public final String name;
    public final int id;
    public final int maxPlayers;
    public final int cardsPerPlayer;

    public GameOption(String name, int id, int maxPlayers, int cardsPerPlayer) {
        this.name = name;
        this.id = id;
        this.maxPlayers = maxPlayers;
        this.cardsPerPlayer = cardsPerPlayer;
    }
}