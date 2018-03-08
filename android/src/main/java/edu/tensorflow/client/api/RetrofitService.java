package edu.tensorflow.client.api;

import java.io.File;

import edu.tensorflow.client.api.dto.ResultDTO;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Zoltan Szabo on 3/8/18.
 */

public class RetrofitService {
    private final static String SERVER_PATH = "http://192.168.48.106:8080/";
    private final SetCardDetectorService setCardDetectorService;

    public RetrofitService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_PATH)
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
