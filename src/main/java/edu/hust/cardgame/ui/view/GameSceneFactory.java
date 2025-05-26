package main.java.edu.hust.cardgame.ui.view;

import main.java.edu.hust.cardgame.controller.*;

import main.java.edu.hust.cardgame.model.GameOption;

public class GameSceneFactory {

    public static GameScene create(GameOption gameOption, GameController controller) {
        switch (gameOption.id) {
            case 1, 2 -> {
                return new TienLenGameScene((TienLenGameController) controller);
            }
            case 3 -> {
                return new BaCayGameScene((BaCayGameController) controller);
            }
            default -> throw new IllegalArgumentException("Unknown game option: " + gameOption.name);
        }
    }
}
