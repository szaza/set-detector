package edu.tensorflow.client.api;

import edu.tensorflow.client.api.dto.ResultDTO;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Zoltan Szabo on 3/8/18.
 */

public interface SetCardDetectorService {
    @Multipart
    @POST("mobile")
    Call<ResultDTO> detectObjects(@Part MultipartBody.Part file);
}
