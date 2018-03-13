package edu.tensorflow.client.camera;

import android.app.ListActivity;
import android.os.Bundle;

import java.util.List;

import edu.tensorflow.client.R;
import edu.tensorflow.client.api.ResultAssembler;
import edu.tensorflow.client.api.dto.ResultDTO;
import edu.tensorflow.client.camera.view.CustomArrayAdapter;
import edu.tensorflow.client.model.Result;

/**
 * Created by Zoltan Szabo on 3/12/18.
 */

public class ListResultActivity extends ListActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ResultAssembler resultAssembler = new ResultAssembler();
        List<Result> resultList = resultAssembler.convertToEntityList((ResultDTO) getIntent().getExtras().get("result"));
        CustomArrayAdapter customArrayAdapter = new CustomArrayAdapter(getApplicationContext(), resultList.size(),
                resultList);
        setListAdapter(customArrayAdapter);
    }
}