package main.java.edu.hust.cardgame.logic.tienlen;

import main.java.edu.hust.cardgame.core.CardComboType;
import main.java.edu.hust.cardgame.core.StandardCard;
import main.java.edu.hust.cardgame.core.CardCollection;

public interface TienLenPlayValidator {
    CardComboType determineComboType(CardCollection<StandardCard> cardCollection);
}
