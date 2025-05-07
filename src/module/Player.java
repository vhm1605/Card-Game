package module;

import java.util.List;
import java.util.Objects;

public class Player {
	private PlayerState state;
	private String name;
	private int PlayerIndex;
	private int finishOrder;
	private final CardCollection hand = new CardCollection();

	public Player() {}

	public Player(String name, int PlayerIndex) {
		this.state = PlayerState.IN_ROUND;
		this.name = name;
		this.PlayerIndex = PlayerIndex;
		this.finishOrder = 0;
	}

	public Boolean isActive() {
		return Objects.requireNonNull(state) == PlayerState.IN_ROUND;
	}

	public PlayerState getState() {
		return state;
	}

	public String getName() {
		return name;
	}

	public int getPlayerIndex() {
		return PlayerIndex;
	}

	public int getFinishOrder() {
		return finishOrder;
	}

	public void setState(PlayerState state) {
		this.state = state;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void shuffleHand() {
		hand.shuffle();
	}

	public void receiveCard(Card card) {
		hand.addCard(card);
	}

	public Card getCardAt(int index) {
		return hand.getCardAt(index);
	}

	public List<Card> getAllCards() {
		return hand.getAllCards();
	}

	public boolean contains(Card card) {
		return hand.contains(card);
	}

	public Card useCardAt(int index) {
		return hand.removeCardAt(index);
	}

	public void useCards(CardCollection cards) {
		hand.removeCards(cards);
	}

	public CardCollection cloneHand() {
		return hand.clone();
	}

	public void clearHand() {
		hand.empty();
	}

	public void showHand() {
		hand.displayCards();
	}

	public int getHandSize() {
		return hand.getSize();
	}

	public CardCollection getHand() {
		return hand;
	}
}
