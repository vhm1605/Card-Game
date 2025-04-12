package gamelogic;

public class TienLenMienBac extends TienLen {
	public TienLenMienBac(int numberOfPlayers, int numberOfAIPlayers) {
		super(numberOfPlayers, numberOfAIPlayers);
		this.tienLenPlayValidator = new TienLenMienBacPlayValidator();
	}
}
