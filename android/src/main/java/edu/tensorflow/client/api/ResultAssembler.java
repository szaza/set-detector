package edu.tensorflow.client.api;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.tensorflow.client.R;
import edu.tensorflow.client.api.dto.ResultDTO;
import edu.tensorflow.client.api.dto.ValidSetDTO;
import edu.tensorflow.client.model.Result;

/**
 * Created by Zoltan Szabo on 3/13/18.
 */

public class ResultAssembler {
    public List<Result> convertToEntityList(final Context context, final ResultDTO resultDTO) {
        List<Result> resultList = new ArrayList();

        Result result = new Result();
        result.setURL(resultDTO.getPredictedImage());
        result.setTitle(context.getString(R.string.recognized_cards));
        result.setDescription(resultDTO.getRecognitions().stream().collect(Collectors.joining("\n")));
        resultList.add(result);

        List<ValidSetDTO> validSetList = resultDTO.getValidSetList();
        for (int i=0; i<validSetList.size(); i++) {
            ValidSetDTO validSetDTO = validSetList.get(i);
            resultList.add(new Result(validSetDTO.getImagePath(),
                    context.getString(R.string.set_of_cards) + "#" + (i+1) + ":",
                    validSetDTO.getSetOfCards().toString()));
        }

        return  resultList;
    }
}
