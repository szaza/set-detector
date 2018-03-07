package edu.ml.tensorflow.service.api.assembler;

import edu.ml.tensorflow.model.analyzer.Grid;
import edu.ml.tensorflow.model.analyzer.SetOfCards;
import edu.ml.tensorflow.model.recognition.Recognition;
import edu.ml.tensorflow.service.analyzer.GridAnalyzer;
import edu.ml.tensorflow.service.api.dto.ResultDTO;
import edu.ml.tensorflow.util.IOUtil;
import edu.ml.tensorflow.service.ImageProcessor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ResultAssembler {
    private ValidSetAssembler validSetAssembler;
    private ImageProcessor imageProcessor;
    private final GridAnalyzer gridAnalyzer;

    public ResultAssembler(final GridAnalyzer gridAnalyzer, final ImageProcessor imageProcessor, final ValidSetAssembler validSetAssembler) {
        this.imageProcessor = imageProcessor;
        this.gridAnalyzer = gridAnalyzer;
        this.validSetAssembler = validSetAssembler;
    }

    public ResultDTO convertToDTO(final List<Recognition> recognitionList, final byte[] image) {
        ResultDTO resultDTO = new ResultDTO();
        List<SetOfCards> validSets = gridAnalyzer.detectSet(new Grid(recognitionList));
        resultDTO.setPredictedImage(imageProcessor.labelImage(image, recognitionList, IOUtil.getFileName(UUID.randomUUID().toString() + ".jpg")));
        recognitionList.forEach(recognition -> resultDTO.addRecognition(recognition.getTitle() + " " + recognition.getConfidence()));
        validSets.forEach(validSet -> resultDTO.addValidSetDTO(validSetAssembler.convertToDTO(validSet, imageProcessor.labelCards(image, validSet))));

        return  resultDTO;
    }

}
