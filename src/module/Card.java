package module;

public class Card {
	Face face;
	Suit suit;

	public Card() {}

	public Card(Face face, Suit suit) {
		this.face = face;
		this.suit = suit;
	}

	@Override
	public String toString() {
		return face.getShortLabel() + suit.getSuitSymbol();
	}

	public Face getFace() {
		return face;
	}

	public void setFace(Face face) {
		this.face = face;
	}

	public Suit getSuit() {
		return suit;
	}

	public void setSuit(Suit suit) {
		this.suit = suit;
	}
}
