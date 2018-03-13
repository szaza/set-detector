package edu.tensorflow.client.api;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.tensorflow.client.api.dto.ResultDTO;
import edu.tensorflow.client.model.Result;

/**
 * Created by Zoltan Szabo on 3/13/18.
 */

public class ResultAssembler {
    public List<Result> convertToEntityList(final ResultDTO resultDTO) {
        List<Result> resultList = new ArrayList();

        Result result = new Result();
        result.setURL(resultDTO.getPredictedImage());
        result.setDescription(resultDTO.getRecognitions().stream().collect(Collectors.joining("\n")));
        resultList.add(result);

        resultDTO.getValidSetList().forEach(validSetDTO
                -> resultList.add(new Result(validSetDTO.getImagePath(), validSetDTO.getSetOfCards().toString())));
        return  resultList;
    }
}
