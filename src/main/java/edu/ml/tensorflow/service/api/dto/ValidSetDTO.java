package edu.ml.tensorflow.service.api.dto;

import edu.ml.tensorflow.model.analyzer.Card;
import edu.ml.tensorflow.model.analyzer.SetOfCards;

public class ValidSetDTO {
    private SetOfCards setOfCards;
    private String imagePath;

    public SetOfCards getSetOfCards() {
        return setOfCards;
    }

    public void setSetOfCards(SetOfCards setOfCards) {
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
        for (Card card : setOfCards.getCards()) {
            stringBuilder.append(card.toString());
            stringBuilder.append(" ");
        }

        return stringBuilder.toString();
    }
}
