package dev.kxxcn.app_with.data.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dev.kxxcn.app_with.data.model.pairing.Response;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import static dev.kxxcn.app_with.data.remote.APIPersistence.AUTHENTICATE_KEY;
import static dev.kxxcn.app_with.data.remote.APIPersistence.GET_KEY;
import static dev.kxxcn.app_with.data.remote.APIPersistence.GSON_DATE_FORMAT;
import static dev.kxxcn.app_with.data.remote.APIPersistence.IS_REGISTERED_USER;
import static dev.kxxcn.app_with.data.remote.APIPersistence.SERVER_URL;

/**
 * Created by kxxcn on 2018-08-20.
 */
public interface APIService {

	class Factory {
		static APIService create() {
			Gson gson = new GsonBuilder()
					.setDateFormat(GSON_DATE_FORMAT)
					.create();
			GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(gson);

			HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
			loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

			OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
			httpClient.addInterceptor(loggingInterceptor);

			Retrofit retrofit = new Retrofit.Builder()
					.baseUrl(SERVER_URL)
					.addConverterFactory(gsonConverterFactory)
					.client(httpClient.build())
					.build();
			return retrofit.create(APIService.class);
		}
	}

	@FormUrlEncoded
	@POST(GET_KEY)
	Call<Response> createPairingKey(@Field("uniqueIdentifier") String uniqueIdentifier);

	@FormUrlEncoded
	@POST(AUTHENTICATE_KEY)
	Call<dev.kxxcn.app_with.data.model.result.Response> authenticateKey(@Field("uniqueIdentifier") String uniqueIdentifier,
																		@Field("pair") String pair,
																		@Field("gender") int gender);

	@FormUrlEncoded
	@POST(IS_REGISTERED_USER)
	Call<dev.kxxcn.app_with.data.model.result.Response> isRegisteredUser(@Field("uniqueIdentifier") String uniqueIdentifier);

}
