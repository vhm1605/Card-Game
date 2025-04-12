package module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardCollection {
	private final List<Card> cardList;

	public CardCollection() {
		cardList = new ArrayList<>();
	}

	public void shuffle() {
		Collections.shuffle(cardList);
	}

	public void addCard(Card card) {
		cardList.add(card);
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

	public CardCollection clone() {
		CardCollection clonedCardCollection = new CardCollection();
		for (Card card : this.getAllCards()) {
			clonedCardCollection.addCard(card);
		}
		return clonedCardCollection;
	}

	public void empty() {
		cardList.clear();
	}

	public void displayCards() {
		for (Card card : cardList) {
			System.out.println(card.toString());
		}
	}

	public int getSize() {
		return cardList.size();
	}
}
