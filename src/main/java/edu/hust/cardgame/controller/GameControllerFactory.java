package edu.hust.cardgame.controller;

import edu.hust.cardgame.core.DeckFactory;
import edu.hust.cardgame.core.EnumPairDeckFactory;
import edu.hust.cardgame.logic.bacay.BaCay;
import edu.hust.cardgame.logic.tienlen.TienLenMienBac;
import edu.hust.cardgame.logic.tienlen.TienLenMienNam;
import edu.hust.cardgame.core.Face;
import edu.hust.cardgame.core.StandardCard;
import edu.hust.cardgame.core.Suit;
import edu.hust.cardgame.strategy.BaCayScoreStrategy;

public class GameControllerFactory {

    public static GameController create(int id, int players, int bots) {
        DeckFactory<StandardCard> factory = new EnumPairDeckFactory<>(Face.class, Suit.class, StandardCard::new);

        switch (id) {
            case 1 -> {
                TienLenMienNam game = new TienLenMienNam(players+ bots, bots, factory);
                return new TienLenGameController(game);
            }
            case 2 -> {
                TienLenMienBac game = new TienLenMienBac(players+ bots, bots, factory);
                return new TienLenGameController(game);
            }
            case 3 -> {
                BaCay game = new BaCay(players + bots, bots, new BaCayScoreStrategy(), factory);
                return new BaCayGameController(game);
            }
            default -> throw new IllegalArgumentException("Unknown game option: " + id);
        }
    }
}
