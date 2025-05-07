package gamelogic;

import module.Card;
import module.CardCollection;
import module.PlayerState;
import module.TienLenCardOrderingStrategy;

public class TienLenMienBac extends TienLen {
	public TienLenMienBac(int numberOfPlayers, int numberOfAIPlayers) {
		super(numberOfPlayers, numberOfAIPlayers);
		this.playValidator = new TienLenMienBacPlayValidator();
	}

	@Override
	public boolean isValidPlay() {
		sorter.sort(selectedCards);
		if (players.get(currentPlayerIndex).getState() == PlayerState.OUT_OF_CARDS) {
			moveToNextPlayer();
			return false;
		}

		if (selectedCards.getSize() == 0) {
			return false;
		}

		CardCollection remaining = players.get(currentPlayerIndex).getHand().clone();
		remaining.removeCards(selectedCards);
		if (!remaining.isEmpty()) {
			boolean allTwos = true;
			for (int i = 0; i < remaining.getSize(); i++) {
				if (TienLenCardOrderingStrategy.getFaceOrder(remaining.getCardAt(i)) != 15) {
					allTwos = false;
					break;
				}
			}
			if (allTwos) {
				return false;
			}
			if (remaining.getSize() == 4
					&& determineComboType(remaining) == CardComboType.FOUR_OF_A_KIND) {
				return false;
			}
		}

		CardComboType selectedType = determineComboType(selectedCards);
		CardComboType lastType = determineComboType(lastPlayedCards);

		if (lastType == CardComboType.INVALID_PLAY) {
			return handleInitialPlay(selectedType);
		} else if (selectedType == lastType) {
			return handleSameCombo(selectedType);
		} else {
			return handleDifferentCombo(selectedType, lastType);
		}
	}

	private boolean handleInitialPlay(CardComboType selectedType) {
		if (selectedType == CardComboType.INVALID_PLAY) {
			return false;
		}
		flag = 0;
		return true;
	}

	private boolean handleSameCombo(CardComboType type) {
		int idxSel = selectedCards.getSize() - 1;
		int idxLast = lastPlayedCards.getSize() - 1;
		Card selHigh  = selectedCards.getCardAt(idxSel);
		Card lastHigh = lastPlayedCards.getCardAt(idxLast);

		int selFace = TienLenCardOrderingStrategy.getFaceOrder(selHigh);
		int lastFace = TienLenCardOrderingStrategy.getFaceOrder(lastHigh);
		int selSuit = TienLenCardOrderingStrategy.getSuitOrder(selHigh);
		int lastSuit = TienLenCardOrderingStrategy.getSuitOrder(lastHigh);

		switch (type) {
			case SINGLE:
				if (selFace == 15 && lastFace != 15) {
					flag = 0;
					return true;
				}
				if (selFace == 15 && lastFace == 15) {
					if (selSuit > lastSuit) {
						flag = 0;
						return true;
					}
					return false;
				}
				if (selSuit == lastSuit && selFace > lastFace) {
					flag = 0;
					return true;
				}
				return false;

			case STRAIGHT:
				if (selectedCards.getSize() == lastPlayedCards.getSize()
						&& comparer.compare(selHigh, lastHigh) > 0) {
					flag = 0; return true;
				}
				return false;

			case PAIR:
			case TRIPLE:
				int firstSuitSel = TienLenCardOrderingStrategy.getSuitOrder(selectedCards.getCardAt(0));
				boolean sameSuitSel = true;
				for (int i = 1; i < selectedCards.getSize(); i++) {
					if (TienLenCardOrderingStrategy.getSuitOrder(selectedCards.getCardAt(i)) != firstSuitSel) {
						sameSuitSel = false;
						break;
					}
				}
				int firstSuitLast = TienLenCardOrderingStrategy.getSuitOrder(lastPlayedCards.getCardAt(0));
				boolean sameSuitLast = true;
				for (int i = 1; i < lastPlayedCards.getSize(); i++) {
					if (TienLenCardOrderingStrategy.getSuitOrder(lastPlayedCards.getCardAt(i)) != firstSuitLast) {
						sameSuitLast = false;
						break;
					}
				}
				if (sameSuitSel && sameSuitLast) {
					if (firstSuitSel == firstSuitLast && selFace > lastFace) {
						flag = 0; return true;
					}
					if (selFace == 15 && lastFace == 15 && firstSuitSel > firstSuitLast) {
						flag = 0; return true;
					}
				}
				return false;
			case FOUR_OF_A_KIND:
				if (comparer.compare(selectedCards.getCardAt(0), lastPlayedCards.getCardAt(0)) > 0) {
					flag = 0;
					return true;
				}
			default:
				return false;
		}
	}

	private boolean handleDifferentCombo(CardComboType selType, CardComboType lastType) {
		if (selType == CardComboType.FOUR_OF_A_KIND
				&& lastType == CardComboType.SINGLE
				&& TienLenCardOrderingStrategy.getFaceOrder(lastPlayedCards.getCardAt(0)) == 15) {
			flag = 0;
			return true;
		}
		return false;
	}
}
