package edu.hust.cardgame.ai;

import edu.hust.cardgame.core.CardCollection;
import edu.hust.cardgame.core.CardType;
import edu.hust.cardgame.core.Player;
import edu.hust.cardgame.core.GeneralGame;

public interface AIStrategy<C extends CardType, G extends GeneralGame<C>> {
    CardCollection<C> decideMove(G game, Player<C> ai);
}
