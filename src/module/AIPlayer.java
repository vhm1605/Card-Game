package module;

import gamelogic.TienLen;
import soundeffect.ClickSound;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AIPlayer extends Player {
	public AIPlayer() {
		super();
	}

	public CardCollection selectRandomPlayableCards(int numberOfCardsToSelect) {
		if (numberOfCardsToSelect <= 0 || this.getHandSize() == 0) {
			return new CardCollection();
		}
		if (this.getHandSize() == 1 || numberOfCardsToSelect >= this.getHandSize()) {
			return this.cloneHand();
		}
		List<Card> shuffledHand = new ArrayList<>(this.getAllCards());
		Collections.shuffle(shuffledHand );
		CardCollection selectedCards = new CardCollection();
		for (int i = 0; i < numberOfCardsToSelect; i++) {
			selectedCards.addCard(shuffledHand .get(i));
		}
		return selectedCards;
	}

	public <TypeOfTienLen extends TienLen> boolean makeMove(TypeOfTienLen gameInstance) {
		int numberOfCardsToPlay = decideNumberOfCardsToPlay(gameInstance);
		CardCollection validMove = findValidMove(gameInstance, numberOfCardsToPlay);
		if (validMove != null) {
			useSelectedCards(gameInstance, validMove);
			return true;
		}
		passTurn(gameInstance);
		return false;
	}

	private int decideNumberOfCardsToPlay(TienLen gameType) {
		int lastPlaySize = gameType.getLastPlayedCards().getSize();
		return Math.min(lastPlaySize > 0 ? lastPlaySize : 1, this.getHandSize());
	}

	private CardCollection findValidMove(TienLen gameType, int numberOfCardsToTry) {
		int maxAttempts = 1000;
		while (maxAttempts-- > 0) {
			CardCollection candidateCards = selectRandomPlayableCards(numberOfCardsToTry);
			gameType.getSelectedCards().empty();
			for (int i = 0; i < candidateCards.getSize(); i++) {
				try {
					gameType.getSelectedCards().addCard(candidateCards.getCardAt(i));
				} catch (Exception e) {
					break;
				}
			}
			if (gameType.isValidPlay()) {
				return candidateCards;
			}
		}
		return null;
	}

	private void useSelectedCards(TienLen gameType, CardCollection chosenCards) {
		ClickSound.play();
		gameType.playGame();
		gameType.getSelectedCards().empty();
	}

	private void passTurn(TienLen gameType) {
		ClickSound.play();
		gameType.passTurn();
	}
}
