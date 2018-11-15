package dev.kxxcn.app_with.data.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dev.kxxcn.app_with.data.model.geocode.ResponseGeocode;
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

	@Headers({
			"Accept: */*",
			"Content-type: application/json",
			"X-Naver-Client-Id: bFDyHZxaQ7BKmClk232I",
			"X-Naver-Client-Secret: ieKOmI9pds"
	})
	@GET(CONVERT_COORD_TO_ADDRESS)
	Single<ResponseGeocode> convertCoordToAddress(@Query("encoding") String encoding,
												  @Query("coordType") String coordType,
												  @Query("query") String query);

}
