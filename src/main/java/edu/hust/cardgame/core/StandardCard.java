package edu.hust.cardgame.core;

public class StandardCard extends PairCard<Face, Suit> {
	public StandardCard(Face face, Suit suit) {
		super(face, suit);
	}

	@Override
	public String toString() {
		return getFirst().getShortLabel() + getSecond().getSuitSymbol();
	}
}
