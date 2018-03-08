package edu.ml.tensorflow.service.api.assembler;

import edu.ml.tensorflow.model.analyzer.Card;
import edu.ml.tensorflow.service.api.dto.CardDTO;
import org.springframework.stereotype.Service;

@Service
public class CardAssembler {
    public CardDTO convertToDTO(final Card card) {
        CardDTO cardDTO = new CardDTO();
        cardDTO.setTitle(card.toString());
        return cardDTO;
    }
}
