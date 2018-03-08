package edu.ml.tensorflow.service.api.dto;

import java.util.ArrayList;
import java.util.List;

public class ResultDTO {
    private String predictedImage;
    private List<String> recognitions;
    private List<ValidSetDTO> validSetList;

    public ResultDTO() {
        recognitions = new ArrayList();
        validSetList = new ArrayList();
    }

    public String getPredictedImage() {
        return predictedImage;
    }

    public void setPredictedImage(final String predictedImage) {
        this.predictedImage = predictedImage;
    }

    public List<ValidSetDTO> getValidSetList() {
        return validSetList;
    }

    public void setValidSetList(final List<ValidSetDTO> validSetList) {
        this.validSetList = validSetList;
    }

    public void addValidSetDTO(final ValidSetDTO validSetDTO) {
        this.validSetList.add(validSetDTO);
    }

    public List<String> getRecognitions() {
        return recognitions;
    }

    public void setRecognitions(final List<String> recognitions) {
        this.recognitions = recognitions;
    }

    public void addRecognition(final String recognition) {
        this.recognitions.add(recognition);
    }
}
