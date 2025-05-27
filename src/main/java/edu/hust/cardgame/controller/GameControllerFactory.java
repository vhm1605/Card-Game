package main.java.edu.hust.cardgame.controller;

import main.java.edu.hust.cardgame.core.DeckFactory;
import main.java.edu.hust.cardgame.core.EnumPairDeckFactory;
import main.java.edu.hust.cardgame.logic.bacay.BaCay;
import main.java.edu.hust.cardgame.logic.tienlen.TienLenMienBac;
import main.java.edu.hust.cardgame.logic.tienlen.TienLenMienNam;
import main.java.edu.hust.cardgame.core.Face;
import main.java.edu.hust.cardgame.ui.view.GameOption;
import main.java.edu.hust.cardgame.core.StandardCard;
import main.java.edu.hust.cardgame.core.Suit;
import main.java.edu.hust.cardgame.strategy.BaCayScoreStrategy;

public class GameControllerFactory {

    public static GameController create(GameOption gameOption, int players, int bots) {
        DeckFactory<StandardCard> factory = new EnumPairDeckFactory<>(Face.class, Suit.class, StandardCard::new);

        switch (gameOption.id) {
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
            default -> throw new IllegalArgumentException("Unknown game option: " + gameOption.name);
        }
    }
}
