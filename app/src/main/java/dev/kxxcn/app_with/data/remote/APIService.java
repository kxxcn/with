package dev.kxxcn.app_with.data.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import dev.kxxcn.app_with.data.model.diary.Diary;
import dev.kxxcn.app_with.data.model.mode.ResponseMode;
import dev.kxxcn.app_with.data.model.nickname.Nickname;
import dev.kxxcn.app_with.data.model.nickname.ResponseNickname;
import dev.kxxcn.app_with.data.model.pairing.ResponsePairing;
import dev.kxxcn.app_with.data.model.plan.Plan;
import dev.kxxcn.app_with.data.model.result.ResponseResult;
import dev.kxxcn.app_with.data.model.setting.ResponseSetting;
import dev.kxxcn.app_with.util.Constants;
import io.reactivex.Completable;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Streaming;

import static dev.kxxcn.app_with.data.remote.APIPersistence.AUTHENTICATE_KEY;
import static dev.kxxcn.app_with.data.remote.APIPersistence.CHECK_MODE;
import static dev.kxxcn.app_with.data.remote.APIPersistence.GET_DIARY;
import static dev.kxxcn.app_with.data.remote.APIPersistence.GET_IMAGE;
import static dev.kxxcn.app_with.data.remote.APIPersistence.GET_KEY;
import static dev.kxxcn.app_with.data.remote.APIPersistence.GET_NOTIFICATION_INFORMATION;
import static dev.kxxcn.app_with.data.remote.APIPersistence.GET_PLAN;
import static dev.kxxcn.app_with.data.remote.APIPersistence.GET_TITLE;
import static dev.kxxcn.app_with.data.remote.APIPersistence.GSON_DATE_FORMAT;
import static dev.kxxcn.app_with.data.remote.APIPersistence.IS_REGISTERED_USER;
import static dev.kxxcn.app_with.data.remote.APIPersistence.REGISTER_DIARY;
import static dev.kxxcn.app_with.data.remote.APIPersistence.REGISTER_IMAGE;
import static dev.kxxcn.app_with.data.remote.APIPersistence.REGISTER_NICKNAME;
import static dev.kxxcn.app_with.data.remote.APIPersistence.REGISTER_PLAN;
import static dev.kxxcn.app_with.data.remote.APIPersistence.REMOVE_DIARY;
import static dev.kxxcn.app_with.data.remote.APIPersistence.REMOVE_PLAN;
import static dev.kxxcn.app_with.data.remote.APIPersistence.SERVER_URL;
import static dev.kxxcn.app_with.data.remote.APIPersistence.SIGN_UP;
import static dev.kxxcn.app_with.data.remote.APIPersistence.UPDATE_RECEIVE_NOTIFICATION;
import static dev.kxxcn.app_with.data.remote.APIPersistence.UPDATE_TOKEN;

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
					.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
					.addConverterFactory(gsonConverterFactory)
					.client(httpClient.build())
					.build();
			return retrofit.create(APIService.class);
		}
	}

	@FormUrlEncoded
	@POST(GET_KEY)
	Single<ResponsePairing> createPairingKey(@Field("uniqueIdentifier") String uniqueIdentifier,
											 @Field("token") String token);

	@FormUrlEncoded
	@POST(AUTHENTICATE_KEY)
	Single<ResponseResult> authenticateKey(@Field("uniqueIdentifier") String uniqueIdentifier,
										   @Field("pair") String pair,
										   @Field("gender") int gender,
										   @Field("token") String token);

	@FormUrlEncoded
	@POST(SIGN_UP)
	Single<ResponseResult> signUp(@Field("uniqueIdentifier") String uniqueIdentifier,
								  @Field("gender") int gender,
								  @Field("token") String token);

	@FormUrlEncoded
	@POST(IS_REGISTERED_USER)
	Single<ResponseResult> isRegisteredUser(@Field("uniqueIdentifier") String uniqueIdentifier);

	@FormUrlEncoded
	@POST(GET_DIARY)
	Single<List<Diary>> getDiary(@Field("flag") int flag,
								 @Field("uniqueIdentifier") String uniqueIdentifier);

	@POST(REGISTER_DIARY)
	Single<ResponseResult> registerDiary(@Body Diary diary);

	@POST(REGISTER_PLAN)
	Single<ResponseResult> registerPlan(@Body Plan plan);

	@FormUrlEncoded
	@POST(GET_PLAN)
	Single<List<Plan>> getPlan(@Field("uniqueIdentifier") String uniqueIdentifier);

	@Multipart
	@POST(REGISTER_IMAGE)
	Single<ResponseResult> uploadImage(@Part MultipartBody.Part image);

	@FormUrlEncoded
	@POST(GET_IMAGE)
	@Streaming
	Single<ResponseBody> getImage(@Field("fileName") String fileName);

	@FormUrlEncoded
	@POST(REMOVE_DIARY)
	Single<ResponseResult> removeDiary(@Field("id") int id);

	@FormUrlEncoded
	@POST(REMOVE_PLAN)
	Single<ResponseResult> removePlan(@Field("id") int id);

	@FormUrlEncoded
	@POST(GET_NOTIFICATION_INFORMATION)
	Single<ResponseSetting> getNotificationInformation(@Field("uniqueIdentifier") String uniqueIdentifier);

	@FormUrlEncoded
	@POST(UPDATE_RECEIVE_NOTIFICATION)
	Completable updateReceiveNotification(@Field("uniqueIdentifier") String uniqueIdentifier,
										  @Field("which") Constants.NotificationFilter which,
										  @Field("on") boolean isOn);

	@FormUrlEncoded
	@POST(UPDATE_TOKEN)
	Single<ResponseResult> updateToken(@Field("uniqueIdentifier") String uniqueIdentifier,
									   @Field("newToken") String newToken);

	@FormUrlEncoded
	@POST(GET_TITLE)
	Single<ResponseNickname> getTitle(@Field("uniqueIdentifier") String uniqueIdentifier);

	@POST(REGISTER_NICKNAME)
	Single<ResponseResult> registerNickname(@Body Nickname nickname);

	@FormUrlEncoded
	@POST(CHECK_MODE)
	Single<ResponseMode> checkMode(@Field("uniqueIdentifier") String uniqueIdentifier);

}
