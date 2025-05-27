package main.java.edu.hust.cardgame.ai;

import main.java.edu.hust.cardgame.core.GeneralGame;
import main.java.edu.hust.cardgame.core.CardCollection;
import main.java.edu.hust.cardgame.core.CardType;
import main.java.edu.hust.cardgame.core.Player;

public class AIPlayer<C extends CardType, G extends GeneralGame<C>> extends Player<C> implements Cloneable {
    private final AIStrategy<C, G> strategy;

    public AIPlayer(AIStrategy<C, G> strategy) {
        super();
        this.strategy = strategy;
    }

    @Override
    public AIPlayer<C, G> clone() {
        AIPlayer<C, G> clone = (AIPlayer<C, G>) super.clone();
        // Strategy is shared; adjust if cloning is needed
        return clone;
    }

    public void makeMove(G game) {
        CardCollection<C> toPlay = strategy.decideMove(game, this);
        game.getSelectedCards().empty();
        toPlay.getAllCards().forEach(game.getSelectedCards()::addCard);

        if (game.isValidPlay()) {
            game.playGame();
        } else {
            game.passTurn();
        }

        game.getSelectedCards().empty();
    }
}