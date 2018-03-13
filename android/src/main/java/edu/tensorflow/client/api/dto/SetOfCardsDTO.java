package edu.tensorflow.client.api.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SetOfCardsDTO implements Serializable {
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
        stringBuilder.append(cards.stream().map(card ->
                card.getTitle()).collect(Collectors.joining(", ")));
        return stringBuilder.toString();
    }
}
