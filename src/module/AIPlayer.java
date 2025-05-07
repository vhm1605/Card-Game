package module;
import soundaction.ClickSound;

public class AIPlayer extends Player {
	private final AIStrategy strategy;

	// default
	public AIPlayer() {
		this.strategy = new RandomValidMoveStrategy(100000);
	}

	public AIPlayer(AIStrategy strategy) {
		super();
		this.strategy = strategy;
	}

	// AI's turn
	public void makeMove(PlayableGame game) {
		CardCollection toPlay = strategy.decideMove(game, this);

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
