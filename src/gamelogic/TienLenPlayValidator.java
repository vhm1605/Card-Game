package gamelogic;

import module.StandardCard;
import module.CardCollection;

public interface TienLenPlayValidator {
    CardComboType determineComboType(CardCollection<StandardCard> cardCollection);
}
