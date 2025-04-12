package gamelogic;

public class TienLenMienNam extends TienLen {
	public TienLenMienNam(int numberOfPlayers, int numberOfAIPlayers) {
		super(numberOfPlayers, numberOfAIPlayers);
		this.tienLenPlayValidator = new TienLenMienNamPlayValidator();
	}
}
