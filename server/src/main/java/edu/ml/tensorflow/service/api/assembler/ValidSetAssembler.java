package edu.ml.tensorflow.service.api.assembler;

import edu.ml.tensorflow.model.analyzer.SetOfCards;
import edu.ml.tensorflow.service.api.dto.ValidSetDTO;
import org.springframework.stereotype.Service;

@Service
public class ValidSetAssembler {
    private final SetOfCardsAssembler setOfCardsAssembler;

    public ValidSetAssembler(final SetOfCardsAssembler setOfCardsAssembler) {
        this.setOfCardsAssembler = setOfCardsAssembler;
    }

    public ValidSetDTO convertToDTO(final SetOfCards setOfCards, final String imagePath) {
        ValidSetDTO validSetDTO = new ValidSetDTO();
        validSetDTO.setSetOfCards(setOfCardsAssembler.convertToDTO(setOfCards));
        validSetDTO.setImagePath(imagePath);
        return validSetDTO;
    }
}
