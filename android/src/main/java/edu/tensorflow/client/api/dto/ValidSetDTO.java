package edu.tensorflow.client.api.dto;

import java.io.Serializable;

/**
 * Data Transfer Object for a valid set.
 * Created by Zoltan Szabo on 3/8/18.
 */

public class ValidSetDTO implements Serializable {
    private SetOfCardsDTO setOfCards;
    private String imagePath;

    public SetOfCardsDTO getSetOfCards() {
        return setOfCards;
    }

    public void setSetOfCards(SetOfCardsDTO setOfCards) {
        this.setOfCards = setOfCards;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (CardDTO card : setOfCards.getCards()) {
            stringBuilder.append(card.toString());
            stringBuilder.append(" ");
        }

        return stringBuilder.toString();
    }
}
