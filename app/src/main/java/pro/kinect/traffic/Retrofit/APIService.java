package pro.kinect.traffic.Retrofit;

import java.util.List;

import pro.kinect.traffic.Models.QueryObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by http://kinect.pro © 07.09.16
 * Developer Andrii.Gakhov
 */

public interface APIService {
    @POST("data_consumptions")
    Call<ServerResponse> pushToServer(
            @Header("Authorization") String header,
            @Body() List<QueryObject> objectList
    );
}
