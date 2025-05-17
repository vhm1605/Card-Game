package gamelogic;

import java.util.ArrayList;
import java.util.List;

import module.*;

public abstract class TienLen extends CardGame<StandardCard> implements SheddingGame<StandardCard> {
	protected CardCollection<StandardCard> lastPlayedCards = new CardCollection<>();
	protected CardCollection<StandardCard> selectedCards = new CardCollection<>();
	protected StandardCard startingCard;
	protected List<Integer> playerRanking = new ArrayList<>();
	protected int flag;
	protected TienLenPlayValidator playValidator;

	CardSorter<StandardCard> sorter = new CardSorter<>(new TienLenCardComparisonStrategy());
	TienLenCardComparisonStrategy comparer = new TienLenCardComparisonStrategy();

	public TienLen() {}

	public TienLen(int numberOfPlayers, int numberOfAIPlayers, DeckFactory<StandardCard> factory) {
		super(numberOfPlayers, numberOfAIPlayers, factory);
		flag = 1;
		startNewGame();
	}

	// Add other AI Player algorithms here
	@Override
	public void deal() {
		players.clear();

		AIStrategy<StandardCard, SheddingGame<StandardCard>> chosen;

		int aiMode = 2;
		switch (aiMode) {
			case 1 -> chosen = new RandomValidMoveStrategy<>(1000);
			case 2 -> chosen = new BacktrackingStrategy<>();
//			case 3 -> chosen = new SomethingStrategy<>(10000);

			default -> chosen = new RandomValidMoveStrategy<>(1000);
		}

		int humans = numberOfPlayers - numberOfAIPlayers;
		for (int i = 0; i < humans; i++) {
			players.add(new Player<>());
		}

		// bots
		for (int i = 0; i < numberOfAIPlayers; i++) {
			players.add(new AIPlayer<>(chosen));
		}

		for (Player<StandardCard> p : players) {
			for (int j = 0; j < 13; j++) {
				p.receiveCard(deck.removeCardAt(0));
			}
			sorter.sort(p.getHand());
		}
	}

	public void startNewGame() {
		initializeDeck();
		deal();
		if (flag == 1) {
			for (int i = 1; i < numberOfPlayers; i++) {
				if (comparer.compare(players.get(i).getCardAt(0), players.get(currentPlayerIndex).getCardAt(0)) < 0) {
					currentPlayerIndex = i;
				}
			}
			startingCard = players.get(currentPlayerIndex).getCardAt(0);
		} else {
			currentPlayerIndex = playerRanking.getFirst() - 1;
			playerRanking.clear();
		}
		resetRound();
	}

	public void resetRound() {
		for (Player<StandardCard> player : players) {
			if (player.getState() != PlayerState.OUT_OF_CARDS) {
				player.setState(PlayerState.IN_ROUND);
			}
		}
		lastPlayedCards.empty();
	}

	public void playGame() {
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
			flag--;
			restorePlayersWhoPassed();
		}
	}

	private void restorePlayersWhoPassed() {
		for (Player<StandardCard> player : players) {
			if (player.getState() == PlayerState.PASSED) {
				player.setState(PlayerState.IN_ROUND);
			}
		}
	}

	private void updatePlayerRankingIfNeeded() {
		if (playerRanking.size() == numberOfPlayers - 1) {
			playerRanking.add(currentPlayerIndex + 1);
			getCurrentPlayer().setState(PlayerState.OUT_OF_CARDS);
		}
	}

	public void passTurn() {
		int activeCount = countPlayersInRound();
		if (lastPlayedCards.getAllCards().isEmpty()) {
			selectedCards.empty();
			return;
		}
		if (flag < 0 && activeCount == 2) {
			players.get(currentPlayerIndex).setState(PlayerState.PASSED);
			moveToNextPlayer();
			selectedCards.empty();
			flag = 0;
			return;
		}
		if (activeCount <= 2) {
			if (activeCount == 1) {
				resetRound();
				moveToNextPlayer();
			} else {
				moveToNextPlayer();
				resetRound();
			}
			selectedCards.empty();
			return;
		}
		if (flag != 1) {
			players.get(currentPlayerIndex).setState(PlayerState.PASSED);
			moveToNextPlayer();
			selectedCards.empty();
		}
	}

	private int countPlayersInRound() {
		int count = 0;
		for (Player<StandardCard> player : players) {
			if (player.getState() == PlayerState.IN_ROUND) {
				count++;
			}
		}
		return count;
	}

	public void moveToNextPlayer() {
		int start = currentPlayerIndex;
		do {
			currentPlayerIndex = (currentPlayerIndex + 1) % numberOfPlayers;
			if (currentPlayerIndex == start) {
				break;
			}
		} while (players.get(currentPlayerIndex).getState() != PlayerState.IN_ROUND);
	}

	public abstract boolean isValidPlay();

	@Override
	public CardComboType determineComboType(CardCollection<StandardCard> cardCollection) {
		return playValidator.determineComboType(cardCollection);
	}

	public CardComboType isValidStraight(CardCollection<StandardCard> cardCollection) {
		return playValidator.isValidStraight(cardCollection);
	}

	public boolean isGameOver() {
		return (playerRanking.size() == numberOfPlayers);
	}

	public void resetGame() {
		selectedCards.empty();
		lastPlayedCards.empty();
		for (int i = 0; i < numberOfPlayers; i++) {
			players.get(i).getAllCards().clear();
			players.get(i).setState(PlayerState.IN_ROUND);
		}
		startNewGame();
	}

	public String playerRankingToString() {
		StringBuilder builder = new StringBuilder();
		for (Integer rank : playerRanking) {
			builder.append(rank).append(" ");
		}
		return builder.toString();
	}

	public CardCollection<StandardCard> getSelectedCards() {
		return selectedCards;
	}

	public void deselectCard(StandardCard card) {
		selectedCards.getAllCards().remove(card);
	}

	public void selectCard(StandardCard card) {
		if (!selectedCards.contains(card) && players.get(currentPlayerIndex).contains(card)) {
			selectedCards.addCard(card);
		}
	}

	@Override
	public CardCollection<StandardCard> getLastPlayedCards() {
		return lastPlayedCards;
	}
}
