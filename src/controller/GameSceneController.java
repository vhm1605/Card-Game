package controller;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class GameSceneController {
	@FXML
	private StackPane topPane;

	@FXML
	private StackPane leftPane;

	@FXML
	private StackPane rightPane;

	@FXML
	private StackPane bottomPane;

	@FXML
	private StackPane centerPane;

	// Bạn có thể thêm các phương thức để tương tác với các layout con
	public void initialize() {
		System.out.println("Controller initialized!");
	}

	public StackPane getCenterPane() {
		return centerPane;
	}

	public StackPane getTopPane() {
		return topPane;
	}

	public StackPane getLeftPane() {
		return leftPane;
	}

	public StackPane getRightPane() {
		return rightPane;
	}

	public StackPane getBottomPane() {
		return bottomPane;
	}
}
