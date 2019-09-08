package dev.kxxcn.app_with.data.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dev.kxxcn.app_with.data.model.geocode.v2.Results;
import io.reactivex.Single;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

import static dev.kxxcn.app_with.data.remote.APIPersistence.CONVERT_COORD_TO_ADDRESS;
import static dev.kxxcn.app_with.data.remote.APIPersistence.GSON_DATE_FORMAT;
import static dev.kxxcn.app_with.data.remote.APIPersistence.NAVER_SERVER_URL;

/**
 * Created by kxxcn on 2018-11-05.
 */
public interface OpenAPIService {

    @Headers({
            "Accept: */*",
            "Content-type: application/json",
            "X-NCP-APIGW-API-KEY-ID: 49tfw3peab",
            "X-NCP-APIGW-API-KEY: 0Z8zDRzC3vAqZ3EDCw8pMz5R5hywnWO5KyyFKabD"
    })
    @GET(CONVERT_COORD_TO_ADDRESS)
    Single<Results> convertCoordToAddress(@Query("coords") String query,
                                          @Query("orders") String orders,
                                          @Query("output") String output);

    class Factory {
        static OpenAPIService create() {
            Gson gson = new GsonBuilder()
                    .setDateFormat(GSON_DATE_FORMAT)
                    .create();
            GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(gson);

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(loggingInterceptor);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NAVER_SERVER_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(gsonConverterFactory)
                    .client(httpClient.build())
                    .build();
            return retrofit.create(OpenAPIService.class);
        }
    }
}
