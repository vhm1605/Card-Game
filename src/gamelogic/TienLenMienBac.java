package gamelogic;

import module.StandardCard;
import module.CardCollection;
import module.PlayerState;
import module.TienLenCardOrderingStrategy;

public class TienLenMienBac extends TienLen {
	public TienLenMienBac(int numberOfPlayers, int numberOfAIPlayers, DeckFactory<StandardCard> factory) {
		super(numberOfPlayers, numberOfAIPlayers, factory);
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

//		CardCollection remainingCards = players.get(currentPlayerIndex).getHand().clone();
		CardCollection<StandardCard> remainingCards = players.get(currentPlayerIndex).getHand().clone();
		remainingCards.removeCards(selectedCards);
		if (!remainingCards.isEmpty()) {
			boolean allTwos = true;
			for (int i = 0; i < remainingCards.getSize(); i++) {
				if (TienLenCardOrderingStrategy.getFaceOrder(remainingCards.getCardAt(i)) != 15) {
					allTwos = false;
					break;
				}
			}
			if (allTwos) {
				return false;
			}
			if (remainingCards.getSize() == 4
					&& determineComboType(remainingCards) == CardComboType.FOUR_OF_A_KIND) {
				return false;
			}
		}

		CardComboType selectedCardsType = determineComboType(selectedCards);
		CardComboType lastPlayCardsType = determineComboType(lastPlayedCards);

		if (lastPlayCardsType == CardComboType.INVALID_PLAY) {
			return handleInitialPlay(selectedCardsType);
		} else if (selectedCardsType == lastPlayCardsType) {
			return handleSameCombo(selectedCardsType);
		} else {
			return handleDifferentCombo(selectedCardsType, lastPlayCardsType);
		}
	}

	private boolean handleInitialPlay(CardComboType selectedCardsType) {
		if (selectedCardsType == CardComboType.INVALID_PLAY) {
			return false;
		}
		flag = 0;
		return true;
	}

	private boolean handleSameCombo(CardComboType type) {
		int lastIndexOfSelectedCards = selectedCards.getSize() - 1;
		int lastIndexOfLastPlayCards = lastPlayedCards.getSize() - 1;
		StandardCard highestCardOfSelectedCards  = selectedCards.getCardAt(lastIndexOfSelectedCards);
		StandardCard highestCardOfLastPlayCards = lastPlayedCards.getCardAt(lastIndexOfLastPlayCards);

		int faceOfHighestCardOfSelectedCards = TienLenCardOrderingStrategy.getFaceOrder(highestCardOfSelectedCards);
		int faceOfHighestCardOfLastPlayCards = TienLenCardOrderingStrategy.getFaceOrder(highestCardOfLastPlayCards);
		int suitOfHighestCardOfSelectedCards = TienLenCardOrderingStrategy.getSuitOrder(highestCardOfSelectedCards);
		int suitOfHighestCardOfLastPlayCards = TienLenCardOrderingStrategy.getSuitOrder(highestCardOfLastPlayCards);

		switch (type) {
			case SINGLE:
				if (faceOfHighestCardOfSelectedCards == 15) {
					if (faceOfHighestCardOfLastPlayCards != 15) {
						flag = 0;
						return true;
					}
					if (suitOfHighestCardOfSelectedCards > suitOfHighestCardOfLastPlayCards) {
						flag = 0;
						return true;
					}
					return false;
				}
				if (suitOfHighestCardOfSelectedCards == suitOfHighestCardOfLastPlayCards && faceOfHighestCardOfSelectedCards > faceOfHighestCardOfLastPlayCards) {
					flag = 0;
					return true;
				}
				return false;

			case STRAIGHT:
				if (selectedCards.getSize() == lastPlayedCards.getSize()
						&& comparer.compare(highestCardOfSelectedCards, highestCardOfLastPlayCards) > 0) {
					flag = 0;
					return true;
				}
				return false;

			case PAIR:
				if (CardGameUtils.getColorGroup(highestCardOfSelectedCards) == CardGameUtils.getColorGroup(highestCardOfLastPlayCards) && faceOfHighestCardOfSelectedCards > faceOfHighestCardOfLastPlayCards) {
					flag = 0;
					return true;
				}
				if (faceOfHighestCardOfSelectedCards == 15 && faceOfHighestCardOfLastPlayCards == 15) {
					if (suitOfHighestCardOfSelectedCards > suitOfHighestCardOfLastPlayCards) {
						flag = 0;
						return true;
					}
					return false;
				}
				return false;

			case TRIPLE:
				if (faceOfHighestCardOfSelectedCards > faceOfHighestCardOfLastPlayCards && suitOfHighestCardOfSelectedCards == suitOfHighestCardOfLastPlayCards && TienLenCardOrderingStrategy.getSuitOrder(selectedCards.getCardAt(0)) == TienLenCardOrderingStrategy.getSuitOrder(lastPlayedCards.getCardAt(0))) {
					flag = 0;
					return true;
				}
				return false;
			case FOUR_OF_A_KIND:
				if (faceOfHighestCardOfSelectedCards > faceOfHighestCardOfLastPlayCards) {
					flag = 0;
					return true;
				}
			default:
				return false;
		}
	}

	private boolean handleDifferentCombo(CardComboType selectedCardsType, CardComboType lastPlayCardsType) {
		if (selectedCardsType == CardComboType.FOUR_OF_A_KIND
				&& lastPlayCardsType == CardComboType.SINGLE
				&& TienLenCardOrderingStrategy.getFaceOrder(lastPlayedCards.getCardAt(0)) == 15) {
			flag = 0;
			return true;
		}
		return false;
	}
}
