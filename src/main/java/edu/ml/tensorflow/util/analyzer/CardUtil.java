package edu.ml.tensorflow.util.analyzer;

import edu.ml.tensorflow.model.analyzer.Card;
import edu.ml.tensorflow.model.analyzer.SetOfCards;
import edu.ml.tensorflow.model.recognition.Recognition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static edu.ml.tensorflow.config.AnalyzerConfig.CATEGORY_NR;
import static edu.ml.tensorflow.config.AnalyzerConfig.SET_SIZE;

public final class CardUtil {
    private final static Logger LOG = LoggerFactory.getLogger(CardUtil.class);

    public static String convertToColor(final Integer color) {
        switch (color) {
            case 0: return "red";
            case 1: return "green";
            case 2: return "purple";
            default: return "unknown";
        }
    }

    public static String convertToShape(final Integer shape) {
        switch (shape) {
            case 0: return "croissant";
            case 1: return "diamond";
            case 2: return "oval";
            default: return "unknown";
        }
    }

    public static String convertToFill(final Integer fill) {
        switch (fill) {
            case 0: return "blank";
            case 1: return "striped";
            case 2: return "filled";
            default: return "unknown";
        }
    }

    public static Integer convertColorToInt(final String color) {
        switch (color) {
            case "r": return 0;
            case "g": return 1;
            case "p": return 2;
            default: return -1;
        }
    }

    public static Integer convertFillToInt(final String fill) {
        switch (fill) {
            case "b": return 0;
            case "s": return 1;
            case "f": return 2;
            default: return -1;
        }
    }

    public static Integer convertShapeToInt(final String shape) {
        switch (shape) {
            case "c": return 0;
            case "d": return 1;
            case "o": return 2;
            default: return -1;
        }
    }

    public static Card convertRecognitionToCard(final Recognition recognition) {
        String[] cardAttributes = recognition.getTitle().split("-");
        Card card = new Card();

        // Set number
        try {
            card.setNumber(Integer.parseInt(cardAttributes[0]) - 1);
        } catch (NumberFormatException ex) {
            LOG.error("Invalid number: {}", cardAttributes[0]);
        }

        // Set color
        card.setColor(convertColorToInt(cardAttributes[1]));
        // Set fill
        card.setFill(convertFillToInt(cardAttributes[2]));
        // Set shape
        card.setShape(convertShapeToInt(cardAttributes[3]));

        return card;
    }

    /**
     * Checks if the cards form a SET
     * @param soc - to check
     * @param categoryIndex - we check the cards based on this category, so in this category they always form a set
     * @return true if the cards form a SET
     */
    public static boolean isSet(final SetOfCards soc, final int categoryIndex) {
        boolean isSet = true;

        for (int i = 0; i < CATEGORY_NR; i++) {
            if (i == categoryIndex) {
                continue;
            }

            isSet = isSet && isSetByCategory(soc, i);
        }

        return isSet;
    }

    /**
     * Check if the cards form a SET inside a category
     * eg. the color of the cards is the same or is different on each card
     * @param soc
     * @param categoryIndex
     * @return
     */
    private static boolean isSetByCategory(final SetOfCards soc, final int categoryIndex) {
        List<Card> cards = soc.getCards();

        // Checks if an attribute applies for each card eg. all of the is green
        boolean isSameOnEach = true;
        for (int i = 1; i < SET_SIZE; i++) {
            isSameOnEach = isSameOnEach && cards.get(i).getAttrByCategoryIndex(categoryIndex)
                    .equals(cards.get(i-1).getAttrByCategoryIndex(categoryIndex));
        }

        // Can happen that the attribute differs on each cards eg. their colors is different red, green, purple
        if (!isSameOnEach) {
            return !cards.get(0).getAttrByCategoryIndex(categoryIndex).equals(cards.get(1).getAttrByCategoryIndex(categoryIndex))
                    && !cards.get(1).getAttrByCategoryIndex(categoryIndex).equals(cards.get(2).getAttrByCategoryIndex(categoryIndex))
                    && !cards.get(0).getAttrByCategoryIndex(categoryIndex).equals(cards.get(2).getAttrByCategoryIndex(categoryIndex));
        }

        return true;
    }
}
