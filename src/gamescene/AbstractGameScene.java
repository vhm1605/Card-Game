package gamescene;

import controller.GameSceneController;
import imageaction.BackgroundImage;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public abstract class AbstractGameScene<T> {
    protected T gameLogic;
    protected Stage primaryStage;
    protected boolean isBasic;
    protected StackPane centerPane;
    protected StackPane bottomPane;
    protected StackPane topPane;
    protected StackPane leftPane;
    protected StackPane rightPane;

    public AbstractGameScene(T gameLogic, Stage primaryStage, boolean isBasic) {
        this.gameLogic = gameLogic;
        this.primaryStage = primaryStage;
        this.isBasic = isBasic;
    }

    public Parent createScene() {
        try {
            URL fxmlLocation = getClass().getResource("GamePlayScene.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();
            GameSceneController controller = loader.getController();

            this.centerPane = controller.getCenterPane();
            this.bottomPane = controller.getBottomPane();
            this.topPane = controller.getTopPane();
            this.leftPane = controller.getLeftPane();
            this.rightPane = controller.getRightPane();

            ((Pane) root).setBackground(BackgroundImage.set("/resources/card/backgroundgameplay.png"));

            setPanePadding();
            setupUI();

            return root;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void setPanePadding() {
        bottomPane.setPadding(new Insets(10, 0, 10, 0));
        topPane.setPadding(new Insets(10, 0, 10, 0));
        leftPane.setPadding(new Insets(80, 150, 80, 150));
        rightPane.setPadding(new Insets(80, 150, 80, 150));
    }

    protected abstract void setupUI();

    protected abstract void updateUI();

    protected abstract void handleGameOver();
}