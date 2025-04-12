package gamelogic;

import java.util.ArrayList;
import java.util.List;

import module.*;

public abstract class TienLen extends CardGame {
	protected CardCollection lastPlayedCards;
	protected CardCollection selectedCards;
	protected Card startingCard = new Card();
	protected int turnLockFlag;
	public List<Integer> playerRanking;
	protected TienLenPlayValidator tienLenPlayValidator;

	public TienLen() {}

	public TienLen(int numberOfPlayers, int numberOfAIPlayers) {
		super(numberOfPlayers, numberOfAIPlayers);
		playerRanking = new ArrayList<>();
		selectedCards = new CardCollection();
		turnLockFlag = 1;
		this.deal();
		this.showAllPlayerHands();
		this.lastPlayedCards = new CardCollection();
		this.startNewGame();
	}

	@Override
	public void deal() {
		players.clear();
		CardSorter sorter = new CardSorter(new TienLenCardComparisonStrategy());
		int numberOfHumanPlayers = numberOfPlayers - numberOfAIPlayers;
		for (int i = 0; i < numberOfHumanPlayers; i++) {
			Player humanPlayer = new Player();
			for (int j = 0; j < 13; j++) {
				humanPlayer.receiveCard(deck.removeCardAt(0));
			}
			sorter.sort(humanPlayer.getHand());
			players.add(humanPlayer);
		}
		for (int i = 0; i < numberOfAIPlayers; i++) {
			AIPlayer aiPlayer = new AIPlayer();
			for (int j = 0; j < 13; j++) {
				aiPlayer.receiveCard(deck.removeCardAt(0));
			}
			sorter.sort(aiPlayer.getHand());
			players.add(aiPlayer);
		}
	}

	public void startNewGame() {
		if (roundNumber == 0) {
			this.startFirstRound();
		}
		roundNumber++;
	}

	public void startFirstRound() {
		TienLenCardComparisonStrategy comparer = new TienLenCardComparisonStrategy();
		for (int i = 1; i < numberOfPlayers; i++) {
			Card candidateCard = players.get(i).getCardAt(0);
			Card currentMinCard = players.get(currentPlayerIndex).getCardAt(0);
			if (comparer.compare(candidateCard, currentMinCard) < 0) {
				currentPlayerIndex = i;
			}
		}
		startingCard = players.get(currentPlayerIndex).getCardAt(0);
		this.resetRoundStates();
	}

	public void resetRoundStates() {
		for (Player playerTemp : players) {
			if (playerTemp.getState() != PlayerState.OUT_OF_CARDS) {
				playerTemp.setState(PlayerState.IN_ROUND);
			}
		}
		lastPlayedCards.getAllCards().clear();
	}

	public void playGame() {
		CardSorter sorter = new CardSorter(new TienLenCardComparisonStrategy());
		if (isGameOver()) {
			return;
		}
		if (getCurrentPlayer().getState() == PlayerState.IN_ROUND) {
			sorter.sort(selectedCards);
			if (isValidPlay()) {
				processValidPlay();
			}
			moveToNextPlayer();
			updatePlayerRankingIfNeeded();
		}
	}

	private void processValidPlay() {
		lastPlayedCards = selectedCards.clone();
		getCurrentPlayer().useCards(lastPlayedCards);
		if (getCurrentPlayer().getAllCards().isEmpty()) {
			playerRanking.add(currentPlayerIndex + 1);
			getCurrentPlayer().setState(PlayerState.OUT_OF_CARDS);
			turnLockFlag--;
			restorePlayersWhoPassed();
		}
	}

	private void restorePlayersWhoPassed() {
		for (Player p : players) {
			if (p.getState() == PlayerState.PASSED) {
				p.setState(PlayerState.IN_ROUND);
			}
		}
	}

	private void updatePlayerRankingIfNeeded() {
		if (playerRanking.size() == numberOfPlayers - 1) {
			playerRanking.add(currentPlayerIndex + 1);
			getCurrentPlayer().setState(PlayerState.OUT_OF_CARDS);
		}
	}

	public boolean passTurn() {
		int playersStillInRound = 0;
		for (Player player : players) {
			if (player.getState() == PlayerState.IN_ROUND) {
				playersStillInRound++;
			}
		}
		if (lastPlayedCards.getAllCards().isEmpty()) {
			selectedCards.empty();
			return false;
		}
		if (turnLockFlag < 0 && playersStillInRound == 2) {
			players.get(currentPlayerIndex).setState(PlayerState.PASSED);
			moveToNextPlayer();
			selectedCards.empty();
			turnLockFlag = 0;
			return true;
		}
		if (playersStillInRound <= 2) {
			if (playersStillInRound == 1) {
				resetRoundStates();
				moveToNextPlayer();
			} else {
				moveToNextPlayer();
				resetRoundStates();
			}
			selectedCards.empty();
			return true;
		}
		if (turnLockFlag != 1) {
			players.get(currentPlayerIndex).setState(PlayerState.PASSED);
			selectedCards.empty();
			moveToNextPlayer();
			return true;
		}
		return false;
	}

	public void moveToNextPlayer() {
		int startingIndex = currentPlayerIndex;
		do {
			currentPlayerIndex = (currentPlayerIndex + 1) % numberOfPlayers;
			if (currentPlayerIndex == startingIndex) {
				break;
			}
		} while (players.get(currentPlayerIndex).getState() != PlayerState.IN_ROUND);
	}

	public boolean isValidPlay() {
		CardSorter sorter = new CardSorter(new TienLenCardComparisonStrategy());
		TienLenCardComparisonStrategy comparator = new TienLenCardComparisonStrategy();
		sorter.sort(selectedCards);
		CardComboType selectedComboType = determineComboType(selectedCards);
		CardComboType lastComboType = determineComboType(lastPlayedCards);
		if (players.get(currentPlayerIndex).getState() == PlayerState.OUT_OF_CARDS) {
			moveToNextPlayer();
			return false;
		}
		if (turnLockFlag == 1 &&
				getCurrentPlayer().getCardAt(0).equals(startingCard) &&
				!selectedCards.getCardAt(0).equals(startingCard)) {
			return false;
		}

		if (selectedCards.getSize() > 0) {
			int selectedLastIndex = selectedCards.getSize() - 1;
			int lastPlayedLastIndex = lastPlayedCards.getSize() - 1;

			if (lastComboType == CardComboType.INVALID_PLAY) {
				if (selectedComboType == CardComboType.INVALID_PLAY) {
					return false;
				}
				turnLockFlag = 0;
				return true;
			} else if (selectedComboType == lastComboType) {
				if (selectedComboType == CardComboType.STRAIGHT) {
					if (selectedLastIndex == lastPlayedLastIndex &&
							comparator.compare(selectedCards.getCardAt(selectedLastIndex),
									lastPlayedCards.getCardAt(lastPlayedLastIndex)) > 0) {
						turnLockFlag = 0;
						return true;
					}
				} else if (selectedComboType == CardComboType.CONSECUTIVE_PAIRS) {
					if (selectedLastIndex > lastPlayedLastIndex ||
							(selectedLastIndex == lastPlayedLastIndex &&
									comparator.compare(selectedCards.getCardAt(selectedLastIndex),
											lastPlayedCards.getCardAt(lastPlayedLastIndex)) > 0)) {
						turnLockFlag = 0;
						return true;
					} else {
						return false;
					}
				} else if (comparator.compare(selectedCards.getCardAt(selectedLastIndex),
						lastPlayedCards.getCardAt(lastPlayedLastIndex)) > 0) {
					turnLockFlag = 0;
					return true;
				}
			} else {
				if (selectedComboType == CardComboType.FOUR_OF_A_KIND &&
						lastComboType == CardComboType.CONSECUTIVE_PAIRS &&
						lastPlayedLastIndex + 1 == 6) {
					turnLockFlag = 0;
					return true;
				} else if (lastComboType == CardComboType.FOUR_OF_A_KIND &&
						selectedComboType == CardComboType.CONSECUTIVE_PAIRS &&
						lastPlayedLastIndex + 1 >= 8) {
					turnLockFlag = 0;
					return true;
				} else if (TienLenCardOrderingStrategy.getFaceOrder(lastPlayedCards.getCardAt(lastPlayedLastIndex)) == 15) {
					if (lastPlayedLastIndex == 0) {
						if (selectedComboType == CardComboType.FOUR_OF_A_KIND ||
								selectedComboType == CardComboType.CONSECUTIVE_PAIRS) {
							turnLockFlag = 0;
							return true;
						}
					} else if (lastPlayedLastIndex == 1) {
						if (selectedComboType == CardComboType.FOUR_OF_A_KIND ||
								(selectedComboType == CardComboType.CONSECUTIVE_PAIRS && selectedLastIndex > 6)) {
							turnLockFlag = 0;
							return true;
						}
					}
				}
			}
		}
		return false;
	}


	public CardComboType determineComboType(CardCollection cardCollection) {
		return tienLenPlayValidator.determineComboType(cardCollection);
	}

	public CardComboType isValidStraight(CardCollection cardCollection) {
		return tienLenPlayValidator.isValidStraight(cardCollection);
	}

	public boolean isGameOver() {
		return playerRanking.size() == numberOfPlayers;
	}

	public void resetGame() {
		selectedCards.empty();
		lastPlayedCards.empty();
		for (int i = 0; i < numberOfPlayers; i++) {
			players.get(i).getAllCards().clear();
			players.get(i).setState(PlayerState.IN_ROUND);
		}
		this.initializeDeck();
		this.deal();
		this.showAllPlayerHands();
		this.roundNumber = 0;
		this.currentPlayerIndex = 0;
		this.startNewGame();
		currentPlayerIndex = playerRanking.getFirst() - 1;
		this.playerRanking.clear();
	}

	public String playerRankingToString() {
		StringBuilder builder = new StringBuilder();
		for (Integer rank : playerRanking) {
			builder.append(rank).append(" ");
		}
		return builder.toString();
	}

	public void cancelSelection() {
		selectedCards.empty();
	}

	public CardCollection getSelectedCards() {
		return selectedCards;
	}

	public void deselectCard(Card card) {
		selectedCards.getAllCards().remove(card);
	}

	public void selectCard(Card card) {
		if (!selectedCards.getAllCards().contains(card) && players.get(currentPlayerIndex).getAllCards().contains(card)) {
			selectedCards.addCard(card);
		}
	}

	public CardCollection getLastPlayedCards() {
		return lastPlayedCards;
	}
}
