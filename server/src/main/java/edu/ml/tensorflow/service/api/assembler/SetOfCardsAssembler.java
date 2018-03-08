package edu.ml.tensorflow.service.api.assembler;

import edu.ml.tensorflow.model.analyzer.SetOfCards;
import edu.ml.tensorflow.service.api.dto.SetOfCardsDTO;
import org.springframework.stereotype.Service;

@Service
public class SetOfCardsAssembler {
    private final CardAssembler cardAssembler;

    public SetOfCardsAssembler(final CardAssembler cardAssembler) {
        this.cardAssembler = cardAssembler;
    }

    public SetOfCardsDTO convertToDTO(final SetOfCards setOfCards) {
        SetOfCardsDTO setOfCardsDTO =  new SetOfCardsDTO();
        setOfCards.getCards().forEach(card -> setOfCardsDTO.add(cardAssembler.convertToDTO(card)));
        return setOfCardsDTO;
    }
}
