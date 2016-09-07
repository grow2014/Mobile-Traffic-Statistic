package pro.kinect.traffic.Retrofit;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.internal.Platform;
import okhttp3.logging.HttpLoggingInterceptor;
import pro.kinect.traffic.App;
import pro.kinect.traffic.BuildConfig;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by http://kinect.pro © 07.09.16
 * Developer Andrii.Gakhov
 */

public class Calls {

    private Retrofit retrofit;
    private OkHttpClient.Builder okHttpBuilder;
    private HttpLoggingInterceptor logging;
    private APIService service;
    private Gson gson = null;
    private SimpleDateFormat simpleDateFormat =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()); //2016-08-25 10:06:01
    private static Calls calls;


    /**
     * Class where we may make some requests to a server.
     */
    private Calls() {

        if (logging == null) {
            logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.d(App.LOG, "Calls.class -> OKHTTP " + message);
                    Platform.get().log(message);
                }
            });

            //as detailed logs output in LogCat
            logging.setLevel(HttpLoggingInterceptor.Level.BODY); //more detailed
//            logging.setLevel(HttpLoggingInterceptor.Level.BASIC); //normal detailed
        }

        if (okHttpBuilder == null) {
            okHttpBuilder = new OkHttpClient.Builder();
            okHttpBuilder.addInterceptor(logging); //add logging as interceptor
            okHttpBuilder.readTimeout(10000, TimeUnit.MILLISECONDS);
            okHttpBuilder.connectTimeout(10000, TimeUnit.MILLISECONDS);
            okHttpBuilder.writeTimeout(10000, TimeUnit.MILLISECONDS);
        }

        if (gson == null) {
            gson = new GsonBuilder() //
                    .setDateFormat("yyyy-MM-dd HH:mm:ss") //2016-08-25 10:06:01
                    .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                    .create();
        }


        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL + BuildConfig.API_PATH) //API URL path
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpBuilder.build())
                    .build();
        }

        if (service == null) { //singleton
            service = retrofit.create(APIService.class);
        }
    }

    public static Calls getInstance() {
        if (calls == null) { //singleton
            calls = new Calls();
        }
        return calls;
    }

    private String getHeader() {
        return String.format("%s", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczovL2FwcDMudXNldGltZS5jby9hcGkvdjEvYXV0aC9sb2dpbiIsImlhdCI6MTQ3MjA5NDc2OSwibmJmIjoxNDcyMDk0NzY5LCJqdGkiOiIyZDNlZjU2MzVmODNiOTMzZDBhZjdmNTdhMmUwYTc4YSIsInN1YiI6MTU3fQ.ejQXucFzaoisunWg04oaVEsN3A13DgYVQkGg639wjYc");
    }


    public void pushDataToServer() {
        Log.d(App.LOG, "Calls.class -> pushDataToServer()");

        Call<ServerResponse> call = service.pushToServer(getHeader(), "");
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                if (response.code() == 200) {
                    Log.d(App.LOG, "Calls.class -> pushDataToServer() -> Retrofit returned 200");

                } else { //not 200
                    Log.d(App.LOG, "Calls.class -> pushDataToServer() -> Retrofit returned " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.d(App.LOG, "Calls.class -> pushDataToServer() -> onFailure() - " + t.getMessage());
            }
        });
    }



}