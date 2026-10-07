package edu.hust.cardgame.logic.tienlen;

import edu.hust.cardgame.core.CardComboType;
import edu.hust.cardgame.core.StandardCard;
import edu.hust.cardgame.core.CardCollection;

public interface TienLenPlayValidator {
    CardComboType determineComboType(CardCollection<StandardCard> cardCollection);
}
