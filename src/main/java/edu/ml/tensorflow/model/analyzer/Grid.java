package edu.ml.tensorflow.model.analyzer;

import edu.ml.tensorflow.model.recognition.Recognition;
import edu.ml.tensorflow.util.analyzer.CardUtil;

import java.util.LinkedHashSet;
import java.util.List;

public class Grid {
    private LinkedHashSet<Card> grid;

    public Grid(final LinkedHashSet<Card> cards) {
        this.grid = cards;
    }

    public Grid(final List<Recognition> recognitionList) {
        this.grid = new LinkedHashSet();
        for (Recognition recognition : recognitionList) {
            grid.add(CardUtil.convertRecognitionToCard(recognition));
        }
    }

    public LinkedHashSet<Card> getGrid() {
        return grid;
    }
}
