package pro.kinect.traffic.Retrofit;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by http://kinect.pro © 07.09.16
 * Developer Andrii.Gakhov
 */

public interface APIService {
    @FormUrlEncoded
    @POST("data_consumptions")
    Call<ServerResponse> pushToServer(
            @Header("Authorization Token") String header
    );
}
