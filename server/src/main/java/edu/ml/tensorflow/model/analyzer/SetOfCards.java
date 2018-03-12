package edu.ml.tensorflow.model.analyzer;

import java.util.ArrayList;
import java.util.List;

public class SetOfCards {
    private List<Card> cards;

    public SetOfCards() {
        cards = new ArrayList();
    }

    public List<Card> getCards() {
        return cards;
    }

    public void add(Card card) {
        cards.add(card);
    }

    public SetOfCards clone() {
        SetOfCards clone = new SetOfCards();
        cards.forEach(card -> clone.add(card));
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SetOfCards{");
        cards.forEach(card -> {stringBuilder.append(card); stringBuilder.append(", ");});
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
