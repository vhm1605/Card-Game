package main.java.edu.hust.cardgame.model;

import java.util.List;
import java.util.Objects;

public class Player <C extends CardType> implements Cloneable {
	private PlayerState state;
	private String name;
	private int PlayerIndex;
	private int finishOrder;
	private CardCollection<C> hand = new CardCollection<>();

	public Player() {}

	public Player(String name, int PlayerIndex) {
		this.state = PlayerState.IN_ROUND;
		this.name = name;
		this.PlayerIndex = PlayerIndex;
		this.finishOrder = 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Player<C> clone() {
		try {
			Player<C> clone = (Player<C>) super.clone();
			clone.hand = this.hand.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("Cloning failed", e);
		}
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

	public void receiveCard(C card) {
		hand.addCard(card);
	}

	public C getCardAt(int index) {
		return hand.getCardAt(index);
	}

	public List<C> getAllCards() {
		return hand.getAllCards();
	}

	public boolean contains(C card) {
		return hand.contains(card);
	}

	public C useCardAt(int index) {
		return hand.removeCardAt(index);
	}

	public void useCards(CardCollection<C> cards) {
		hand.removeCards(cards);
	}
	public CardCollection<C> cloneHand() {
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

	public CardCollection<C> getHand() {
		return hand;
	}
}
