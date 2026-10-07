package edu.hust.cardgame.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class CardCollection<C extends CardType> {
    private final List<C> cardList = new ArrayList<>();

    public void shuffle() {
        Collections.shuffle(cardList);
    }

    public void addCard(C card) {
        cardList.add(Objects.requireNonNull(card, "card"));
    }

    public void addAll(CardCollection<C> other) {
        for (C card : other.getAllCards()) {
            this.addCard(card);
        }
    }

    public C getCardAt(int index) {
        return cardList.get(index);
    }

    public List<C> getAllCards() {
        return Collections.unmodifiableList(cardList);
    }

    public C removeCardAt(int index) {
        return cardList.remove(index);
    }

    public void removeCards(CardCollection<C> cards) {
        for (C card : cards.cardList) {
            cardList.remove(card);
        }
    }

    public boolean removeCard(C card) {
        return cardList.remove(card);
    }

    public void sort(Comparator<? super C> comparator) {
        cardList.sort(comparator);
    }

    @Override
    public CardCollection<C> clone() {
        CardCollection<C> copy = new CardCollection<>();
        copy.addAll(this);
        return copy;
    }

    public void empty() {
        cardList.clear();
    }

    public boolean isEmpty() {
        return cardList.isEmpty();
    }

    public boolean contains(C card) {
        return cardList.contains(card);
    }

    public int getSize() {
        return cardList.size();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CardCollection<?> cards)) {
            return false;
        }
        return cardList.equals(cards.cardList);
    }

    @Override
    public int hashCode() {
        return cardList.hashCode();
    }
}
