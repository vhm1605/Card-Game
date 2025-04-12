package gamelogic;

import module.CardCollection;

public interface TienLenPlayValidator {
    CardComboType determineComboType(CardCollection cardCollection);
    CardComboType isValidStraight(CardCollection cardCollection);
}
