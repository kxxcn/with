package dev.kxxcn.app_with.data;

import java.util.List;

import dev.kxxcn.app_with.data.model.diary.Diary;
import dev.kxxcn.app_with.data.model.geocode.ResponseGeocode;
import dev.kxxcn.app_with.data.model.nickname.Nickname;
import dev.kxxcn.app_with.data.model.nickname.ResponseNickname;
import dev.kxxcn.app_with.data.model.pairing.ResponsePairing;
import dev.kxxcn.app_with.data.model.plan.Plan;
import dev.kxxcn.app_with.data.model.result.ResponseResult;
import dev.kxxcn.app_with.data.model.setting.ResponseSetting;
import dev.kxxcn.app_with.data.model.user.ResponseUser;
import dev.kxxcn.app_with.util.Constants;
import io.reactivex.Completable;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;

/**
 * Created by kxxcn on 2018-08-20.
 */
public class DataRepository {

	private DataSource dataSource;

	private static DataRepository dataRepository;

	public DataRepository(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public static synchronized DataRepository getInstance(DataSource dataSource) {
		if (dataRepository == null) {
			dataRepository = new DataRepository(dataSource);
		}
		return dataRepository;
	}

	public Single<ResponsePairing> createPairingKey(String uniqueIdentifier, String token) {
		return dataSource.createPairingKey(uniqueIdentifier, token);
	}

	public Single<ResponseResult> signUp(String uniqueIdentifier, int gender, String token) {
		return dataSource.signUp(uniqueIdentifier, gender, token);
	}

	public Single<ResponseResult> authenticate(String uniqueIdentifier, String key, int gender, String token) {
		return dataSource.authenticate(uniqueIdentifier, key, gender, token);
	}

	public Single<ResponseUser> isRegisteredUser(String uniqueIdentifier) {
		return dataSource.isRegisteredUser(uniqueIdentifier);
	}

	public Single<List<Diary>> getDiary(int flag, String uniqueIdentifier) {
		return dataSource.getDiary(flag, uniqueIdentifier);
	}

	public Single<ResponseResult> registerDiary(Diary diary) {
		return dataSource.registerDiary(diary);
	}

	public Single<ResponseResult> registerPlan(Plan plan) {
		return dataSource.registerPlan(plan);
	}

	public Single<List<Plan>> getPlan(String identifier) {
		return dataSource.getPlan(identifier);
	}

	public Single<ResponseResult> uploadImage(MultipartBody.Part body) {
		return dataSource.uploadImage(body);
	}

	public Single<ResponseBody> getImage(String fileName) {
		return dataSource.getImage(fileName);
	}

	public Single<ResponseResult> removeDiary(int id) {
		return dataSource.removeDiary(id);
	}

	public Single<ResponseResult> removePlan(int id) {
		return dataSource.removePlan(id);
	}

	public Single<ResponseSetting> getNotificationInformation(String identifier) {
		return dataSource.getNotificationInformation(identifier);
	}

	public Completable updateReceiveNotification(String identifier, Constants.NotificationFilter which, boolean isOn) {
		return dataSource.updateReceiveNotification(identifier, which, isOn);
	}

	public Single<ResponseGeocode> convertCoordToAddress(String query) {
		return dataSource.convertCoordToAddress(query);
	}

	public Single<ResponseResult> updateToken(String identifier, String newToken) {
		return dataSource.updateToken(identifier, newToken);
	}

	public Single<ResponseNickname> getTitle(String uniqueIdentifier) {
		return dataSource.getTitle(uniqueIdentifier);
	}

	public Single<ResponseResult> registerNickname(Nickname nickname) {
		return dataSource.registerNickname(nickname);
	}

}
