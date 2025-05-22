package main.java.edu.hust.cardgame.ai;

import main.java.edu.hust.cardgame.assets.soundaction.ClickSound;
import main.java.edu.hust.cardgame.model.CardCollection;
import main.java.edu.hust.cardgame.model.CardType;
import main.java.edu.hust.cardgame.model.Player;
import main.java.edu.hust.cardgame.core.GeneralGame;

public class AIPlayer<C extends CardType, G extends GeneralGame<C>> extends Player<C> {
    private final AIStrategy<C, G> strategy;

    public AIPlayer(AIStrategy<C, G> strategy) {
        super();
        this.strategy = strategy;
    }

    // AI's turn
    public void makeMove(G game) {
        CardCollection<C> toPlay = strategy.decideMove(game, this);

        game.getSelectedCards().empty();
        toPlay.getAllCards().forEach(game.getSelectedCards()::addCard);

        if (game.isValidPlay()) {
            ClickSound.play();
            game.playGame();
        } else {
            game.passTurn();
        }

        game.getSelectedCards().empty();
    }
}
