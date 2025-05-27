package main.java.edu.hust.cardgame.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardCollection<C extends CardType> {
    private final List<C> cardList = new ArrayList<>();

    public void shuffle() {
        Collections.shuffle(cardList);
    }

    public void addCard(C card) {
        cardList.add(card);
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
        return cardList;
    }

    public C removeCardAt(int index) {
        return cardList.remove(index);
    }

    public void removeCards(CardCollection<C> cards) {
        for (C card : cards.cardList) {
            cardList.remove(card);
        }
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

    public void displayCards() {
        for (C card : cardList) {
            System.out.println(card);
        }
    }

    public int getSize() {
        return cardList.size();
    }

}
