package main.java.edu.hust.cardgame.model;

public class StandardCard extends PairCard<Face, Suit> {
	public StandardCard(Face face, Suit suit) {
		super(face, suit);
	}

	@Override
	public String toString() {
		return getFirst().getShortLabel() + getSecond().getSuitSymbol();
	}
}
