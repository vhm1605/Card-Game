package gamescene;

import javafx.scene.Parent;
import javafx.stage.Stage;

public abstract class AbstractGameScene<T> {
    protected T gameLogic;
    protected Stage primaryStage;
    protected boolean isBasic;

    public AbstractGameScene(T gameLogic, Stage primaryStage, boolean isBasic) {
        this.gameLogic = gameLogic;
        this.primaryStage = primaryStage;
        this.isBasic = isBasic;
    }

    public abstract Parent createScene();

    protected abstract void setupUI();

    protected abstract void updateUI();

    protected abstract void handleGameOver();
}