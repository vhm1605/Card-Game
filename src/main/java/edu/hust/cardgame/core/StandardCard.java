//package module;
//
//public class Card implements CardType {
//	private Face face;
//	private Suit suit;
//
//	public Card() {}
//
//	public Card(Face face, Suit suit) {
//		this.face = face;
//		this.suit = suit;
//	}
//
//	@Override
//	public String toString() {
//		return face.getShortLabel() + suit.getSuitSymbol();
//	}
//
//	public Face getFace() {
//		return face;
//	}
//
//	public void setFace(Face face) {
//		this.face = face;
//	}
//
//	public Suit getSuit() {
//		return suit;
//	}
//
//	public void setSuit(Suit suit) {
//		this.suit = suit;
//	}
//}
package main.java.edu.hust.cardgame.core;

public class StandardCard extends PairCard<Face, Suit> {
	public StandardCard(Face face, Suit suit) {
		super(face, suit);
	}

	@Override
	public String toString() {
		return getFirst().getShortLabel() + getSecond().getSuitSymbol();
	}
}
