package edu.ml.tensorflow.service.api.assembler;

import edu.ml.tensorflow.ApplicationProperties;
import edu.ml.tensorflow.model.analyzer.Grid;
import edu.ml.tensorflow.model.analyzer.SetOfCards;
import edu.ml.tensorflow.model.recognition.Recognition;
import edu.ml.tensorflow.service.analyzer.GridAnalyzer;
import edu.ml.tensorflow.service.api.dto.ResultDTO;
import edu.ml.tensorflow.util.IOUtil;
import edu.ml.tensorflow.service.ImageProcessor;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.UUID;

@Service
public class ResultAssembler {
    private final ValidSetAssembler validSetAssembler;
    private final ImageProcessor imageProcessor;
    private final GridAnalyzer gridAnalyzer;
    private final ApplicationProperties applicationProperties;

    public ResultAssembler(final GridAnalyzer gridAnalyzer, final ImageProcessor imageProcessor,
                           final ValidSetAssembler validSetAssembler, final ApplicationProperties applicationProperties) {
        this.imageProcessor = imageProcessor;
        this.gridAnalyzer = gridAnalyzer;
        this.validSetAssembler = validSetAssembler;
        this.applicationProperties = applicationProperties;
    }

    public ResultDTO convertToDTO(final List<Recognition> recognitionList, final byte[] image) {
        ResultDTO resultDTO = new ResultDTO();
        BufferedImage bufferedImage = imageProcessor.progressiveScaleImage(imageProcessor.createImageFromBytes(image),
                applicationProperties.getImageSize(), applicationProperties.getImageSize());
        List<SetOfCards> validSets = gridAnalyzer.detectSet(new Grid(recognitionList));
        resultDTO.setPredictedImage(imageProcessor.labelImage(bufferedImage, recognitionList, IOUtil.getFileName(UUID.randomUUID().toString() + ".jpg")));
        recognitionList.forEach(recognition -> resultDTO.addRecognition(recognition.getTitle() + " " + recognition.getConfidence()));
        validSets.forEach(validSet -> resultDTO.addValidSetDTO(validSetAssembler.convertToDTO(validSet, imageProcessor.labelCards(bufferedImage, validSet))));
        return  resultDTO;
    }

}
