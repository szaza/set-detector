package edu.ml.tensorflow.service.api.dto;

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
}
