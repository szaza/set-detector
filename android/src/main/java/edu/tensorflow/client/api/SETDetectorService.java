package edu.tensorflow.client.api;

import java.io.File;

import edu.tensorflow.client.Config;
import edu.tensorflow.client.api.dto.ResultDTO;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Service class which communicates with the server.
 * Created by Zoltan Szabo on 3/8/18.
 */

public class SETDetectorService {
    private final SetCardDetectorService setCardDetectorService;

    public SETDetectorService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.host)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        setCardDetectorService = retrofit.create(SetCardDetectorService.class);
    }

    public Call<ResultDTO> detect(final File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        return setCardDetectorService.detectObjects(body);
    }
}
