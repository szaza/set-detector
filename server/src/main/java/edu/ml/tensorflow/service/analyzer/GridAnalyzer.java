package edu.ml.tensorflow.service.analyzer;

import edu.ml.tensorflow.model.analyzer.Card;
import edu.ml.tensorflow.model.analyzer.Grid;
import edu.ml.tensorflow.model.analyzer.SetOfCards;
import edu.ml.tensorflow.util.analyzer.AttributeConverter;
import edu.ml.tensorflow.util.analyzer.CardUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static edu.ml.tensorflow.config.AnalyzerConfig.ATTR_NR;
import static edu.ml.tensorflow.config.AnalyzerConfig.SET_SIZE;

@Service
public class GridAnalyzer {
    private final int CATEGORY_NR = 4;
    private final static Logger LOG = LoggerFactory.getLogger(GridAnalyzer.class);
    private List<Map<String, Set<Card>>> categorizedCards = new ArrayList<>();
    /*
     * 0 - number
     * 1 - color
     * 2 - fill
     * 3 - shape
     */
    public GridAnalyzer() {
        for (int i = 0; i< CATEGORY_NR; i++) {
            categorizedCards.add(new HashMap());
        }
    }

    /**
     * Checks if the grid contains a SET
     * @param grid to analyze
     * @return list of sets
     */
    public List<SetOfCards> detectSet(final Grid grid) {
        if (grid.getGrid().size() > 2) {
            categorizeCards(grid);
            return detectSetByCategory(getLowestMaximum());
        } else {
            return Collections.emptyList();
        }
    }

    private void categorizeCards(final Grid grid) {
        for (Card card : grid.getGrid()) {
            for (int i = 0; i< CATEGORY_NR; i++) {
                addCardToSet(i, card, categorizedCards.get(i));
            }
        }
    }

    private void addCardToSet(int categoryIndex, final Card card, final Map<String, Set<Card>> cards) {
        String attr = AttributeConverter.convertToAttr(categoryIndex, getAttrByIndex(card, categoryIndex));

        if (cards.get(attr) == null) {
            cards.put(attr, new HashSet());
        }

        cards.get(attr).add(card);
    }

    private Integer getAttrByIndex(final Card card, int index) {
        switch (index) {
            case 0: return card.getNumber();
            case 1: return card.getColor();
            case 2: return card.getFill();
            case 3: return card.getShape();
        }

        return null;
    }

    /**
     * Look for the category which contains the fewer cards per attribute
     * it means that the distribution of cards is almost uniform, so we have to do fewer
     * checks like otherwise.
     * @return the id of the best category
     */
    private int getLowestMaximum() {
        int min = Integer.MAX_VALUE;
        int bestCategory = 0;

        StringBuilder logMessage = new StringBuilder();
        logMessage.append("Cards by category");
        for (int i = 0; i < CATEGORY_NR; i++) {
            logMessage.append(AttributeConverter.convertToCategory(i) + System.getProperty("line.separator"));
            Map<String, Set<Card>> cardsByCategory = categorizedCards.get(i);

            int max = 0;
            for (int j = 0; j< ATTR_NR; j++) {
                String key = AttributeConverter.convertToAttr(i, (byte) j);
                logMessage.append("\t " + key + ": ");

                Set<Card> cards = cardsByCategory.get(key);
                if (cards != null) {
                    for (Card card : cards) {
                        logMessage.append(card + ", ");
                    }

                    if (max < cards.size()) {
                        max = cards.size();
                    }
                }

                logMessage.append(System.getProperty("line.separator"));
            }

            if (min > max) {
                min = max;
                bestCategory = i;
            }
        }

        LOG.debug(logMessage.toString());
        LOG.debug("The lowest maximum is: {}.", min);
        LOG.debug("The best category to process is: {} - {}.", bestCategory, AttributeConverter.convertToCategory(bestCategory));

        return bestCategory;
    }

    private List<SetOfCards> detectSetByCategory(final int categoryIndex) {
        List<SetOfCards> validSet = new ArrayList();
        for (SetOfCards soc : generatePossibleSets(categoryIndex)) {
            if (CardUtil.isSet(soc, categoryIndex)) {
                validSet.add(soc);
            }
        }

        return validSet;
    }

    private List<SetOfCards> generatePossibleSets(final int categoryIndex) {
        List<SetOfCards> possibleSets = new ArrayList();
        Map<String, Set<Card>> cardsByCategory = categorizedCards.get(categoryIndex);

        // Combination of cards by line
        for (int i=0; i<ATTR_NR; i++) {
            Set<Card> cardsByAttr = cardsByCategory.get(AttributeConverter.convertToAttr(categoryIndex, (byte) i));
            if (cardsByAttr != null && cardsByAttr.size() >= SET_SIZE) {
                combinationOfCardsByLine(cardsByAttr.toArray(new Card[]{}), possibleSets, new SetOfCards(), 0, SET_SIZE);
            }
        }

        // Combination of cards by column
        for (Card card1 : cardsByCategory.get(AttributeConverter.convertToAttr(categoryIndex, (byte) 0))) {
            for (Card card2 : cardsByCategory.get(AttributeConverter.convertToAttr(categoryIndex, (byte) 1))) {
                for (Card card3 : cardsByCategory.get(AttributeConverter.convertToAttr(categoryIndex, (byte) 2))) {
                    SetOfCards setOfCards = new SetOfCards();
                    setOfCards.add(card1);
                    setOfCards.add(card2);
                    setOfCards.add(card3);
                    possibleSets.add(setOfCards);
                }
            }
        }

        printOutPossibleSets(possibleSets);

        return possibleSets;
    }

    private void combinationOfCardsByLine(final Card[] cards, final List<SetOfCards> possibleSets,
                                          final SetOfCards setOfCards, int index, int depth) {
        if (depth == 0) {
            possibleSets.add(setOfCards);
        } else {
            for (int i=index; i<cards.length; i++) {
                SetOfCards clone = setOfCards.clone();
                clone.add(cards[i]);
                combinationOfCardsByLine(cards, possibleSets, clone, i+1, depth-1);
            }
        }
    }

    private void printOutPossibleSets(final List<SetOfCards> possibleSets) {
        LOG.debug("{} possible sets.", possibleSets.size());
        for (SetOfCards set : possibleSets) {
            LOG.debug(set.toString());
        }
    }
}
