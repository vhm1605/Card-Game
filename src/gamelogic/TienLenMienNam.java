package gamelogic;

import module.PlayerState;
import module.TienLenCardOrderingStrategy;
import java.util.stream.Collectors;

public class TienLenMienNam extends TienLen {
	public TienLenMienNam(int numberOfPlayers, int numberOfAIPlayers) {
		super(numberOfPlayers, numberOfAIPlayers);
		this.playValidator = new TienLenMienNamPlayValidator();
	}

	@Override
	public boolean isValidPlay() {
		System.out.println("Trying play: " +
				selectedCards.getAllCards().stream()
						.map(c -> c.toString())
						.collect(Collectors.joining(", ")) +
				"  vs  last: " +
				lastPlayedCards.getAllCards().stream()
						.map(c -> c.toString())
						.collect(Collectors.joining(", ")));

		sorter.sort(selectedCards);
		if (players.get(currentPlayerIndex).getState() == PlayerState.OUT_OF_CARDS) {
			moveToNextPlayer();
			return false;
		}
		if (flag == 1 && getCurrentPlayer().getCardAt(0).equals(startingCard) && (selectedCards.getSize() == 0 || !selectedCards.getCardAt(0).equals(startingCard))) {
			return false;
		}
		if (selectedCards.getSize() == 0) {
			return false;
		}

		CardComboType selectedType = determineComboType(selectedCards);
		CardComboType lastType = determineComboType(lastPlayedCards);
		int selLast = selectedCards.getSize() - 1;
		int lastLast = lastPlayedCards.getSize() - 1;

		if (lastType == CardComboType.INVALID_PLAY) {
			return handleInitialPlay(selectedType);
		} else if (selectedType == lastType) {
			return handleSameCombo(selectedType, selLast, lastLast);
		} else {
			return handleDifferentCombo(selectedType, lastType, selLast, lastLast);
		}
	}

	private boolean handleInitialPlay(CardComboType selectedType) {
		if (selectedType == CardComboType.INVALID_PLAY) {
			return false;
		}
		flag = 0;
		return true;
	}

	private boolean handleSameCombo(CardComboType type, int selLast, int lastLast) {
		switch (type) {
			case STRAIGHT:
				if (selLast == lastLast && comparer.compare(selectedCards.getCardAt(selLast), lastPlayedCards.getCardAt(lastLast)) > 0) {
					flag = 0;
					return true;
				}
			case CONSECUTIVE_PAIRS:
				if ((selLast > lastLast) || (selLast == lastLast && comparer.compare(selectedCards.getCardAt(selLast), lastPlayedCards.getCardAt(lastLast)) > 0)) {
					flag = 0;
					return true;
				}
				return false;
			default:
				if (comparer.compare(selectedCards.getCardAt(selLast), lastPlayedCards.getCardAt(lastLast)) > 0) {
					flag = 0;
					return true;
				}
		}
		return false;
	}

	private boolean handleDifferentCombo(CardComboType selType, CardComboType lastType, int selLast, int lastLast) {
		if (selType == CardComboType.FOUR_OF_A_KIND && lastType == CardComboType.CONSECUTIVE_PAIRS && lastLast + 1 == 6) {
			flag = 0;
			return true;
		}
		if (lastType == CardComboType.FOUR_OF_A_KIND && selType == CardComboType.CONSECUTIVE_PAIRS && lastLast + 1 >= 8) {
			flag = 0;
			return true;
		}
		if (TienLenCardOrderingStrategy.getFaceOrder(lastPlayedCards.getCardAt(lastLast)) == 15) {
			return handleHighestSpecial(selType, lastLast, selLast);
		}
		return false;
	}

	private boolean handleHighestSpecial(CardComboType selType, int lastLast, int selLast) {
		if (lastLast == 0) {
			if (selType == CardComboType.FOUR_OF_A_KIND || selType == CardComboType.CONSECUTIVE_PAIRS) {
				flag = 0;
				return true;
			}
		} else if (lastLast == 1) {
			if (selType == CardComboType.FOUR_OF_A_KIND || (selType == CardComboType.CONSECUTIVE_PAIRS && selLast > 6)) {
				flag = 0;
				return true;
			}
		}
		return false;
	}
}
