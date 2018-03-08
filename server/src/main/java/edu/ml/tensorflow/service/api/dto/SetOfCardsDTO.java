package edu.ml.tensorflow.service.api.dto;

import java.util.ArrayList;
import java.util.List;

public class SetOfCardsDTO {
    private List<CardDTO> cards;

    public SetOfCardsDTO() {
        cards = new ArrayList();
    }

    public void add(CardDTO card) {
        cards.add(card);
    }

    public List<CardDTO> getCards() {
        return cards;
    }

    public void setCards(List<CardDTO> cards) {
        this.cards = cards;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        cards.forEach(card -> {stringBuilder.append(card.getTitle()); stringBuilder.append(", ");});
        return stringBuilder.toString();
    }
}
