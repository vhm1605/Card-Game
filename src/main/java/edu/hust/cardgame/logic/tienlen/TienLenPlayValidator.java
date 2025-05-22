package main.java.edu.hust.cardgame.logic.tienlen;

import main.java.edu.hust.cardgame.model.CardComboType;
import main.java.edu.hust.cardgame.model.StandardCard;
import main.java.edu.hust.cardgame.model.CardCollection;

public interface TienLenPlayValidator {
    CardComboType determineComboType(CardCollection<StandardCard> cardCollection);
}
