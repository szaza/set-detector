package edu.tensorflow.client.camera;

import android.app.ListActivity;
import android.os.Bundle;

import edu.tensorflow.client.R;
import edu.tensorflow.client.api.dto.ResultDTO;
import edu.tensorflow.client.api.dto.ValidSetDTO;
import edu.tensorflow.client.camera.view.CustomArrayAdapter;

/**
 * Created by Zoltan Szabo on 3/12/18.
 */

public class ListResultActivity extends ListActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ResultDTO resultDTO = (ResultDTO) getIntent().getExtras().get("result");
        CustomArrayAdapter customArrayAdapter = new CustomArrayAdapter(getApplicationContext(), resultDTO.getValidSetList().size(),
                resultDTO.getValidSetList().toArray(new ValidSetDTO[]{}));
        setListAdapter(customArrayAdapter);
    }
}
