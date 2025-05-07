package module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardCollection {
	private final List<Card> cardList;

	public CardCollection() {
		this.cardList = new ArrayList<>();
	}

	public void shuffle() {
		Collections.shuffle(cardList);
	}

	public void addCard(Card card) {
		cardList.add(card);
	}

	public void addAll(CardCollection other) {
		for (Card card : other.getAllCards()) {
			this.addCard(card);
		}
	}

	public Card getCardAt(int index) {
		return cardList.get(index);
	}

	public List<Card> getAllCards() {
		return cardList;
	}

	public Card removeCardAt(int index) {
		return cardList.remove(index);
	}

	public void removeCards(CardCollection cards) {
		for (Card card : cards.getAllCards()) {
			cardList.remove(card);
		}
	}

	@Override
	public CardCollection clone() {
		CardCollection copy = new CardCollection();
		copy.addAll(this);
		return copy;
	}

	public void empty() {
		cardList.clear();
	}

	public boolean isEmpty() {
		return cardList.isEmpty();
	}

	public boolean contains(Card card) {
		return cardList.contains(card);
	}

	public void displayCards() {
		for (Card card : cardList) {
			System.out.println(card);
		}
	}

	public int getSize() {
		return cardList.size();
	}
}
