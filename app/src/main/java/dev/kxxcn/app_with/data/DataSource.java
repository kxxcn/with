package dev.kxxcn.app_with.data;

import java.util.List;

import dev.kxxcn.app_with.data.model.diary.Diary;
import dev.kxxcn.app_with.data.model.geocode.ResponseGeocode;
import dev.kxxcn.app_with.data.model.mode.ResponseMode;
import dev.kxxcn.app_with.data.model.nickname.Nickname;
import dev.kxxcn.app_with.data.model.nickname.ResponseNickname;
import dev.kxxcn.app_with.data.model.notice.Notice;
import dev.kxxcn.app_with.data.model.pairing.ResponsePairing;
import dev.kxxcn.app_with.data.model.plan.Plan;
import dev.kxxcn.app_with.data.model.result.ResponseResult;
import dev.kxxcn.app_with.data.model.setting.ResponseSetting;
import dev.kxxcn.app_with.util.Constants;
import io.reactivex.Completable;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;

/**
 * Created by kxxcn on 2018-08-20.
 */
public abstract class DataSource {

	public abstract Single<ResponsePairing> createPairingKey(String uniqueIdentifier, String token, int gender);

	public abstract Single<ResponseResult> signUp(String uniqueIdentifier, int gender, String token);

	public abstract Single<ResponseResult> signOut(String uniqueIdentifier);

	public abstract Single<ResponseResult> authenticate(String uniqueIdentifier, String key, int gender, String token);

	public abstract Single<ResponseResult> isRegisteredUser(String uniqueIdentifier);

	public abstract Single<List<Diary>> getDiary(int flag, String uniqueIdentifier);

	public abstract Single<ResponseResult> registerDiary(Diary diary);

	public abstract Single<ResponseResult> updateDiary(Diary diary);

	public abstract Single<ResponseResult> registerPlan(Plan plan);

	public abstract Single<List<Plan>> getPlan(String uniqueIdentifier);

	public abstract Single<ResponseResult> uploadImage(MultipartBody.Part body);

	public abstract Single<ResponseBody> getImage(String fileName);

	public abstract Single<ResponseResult> removeDiary(int id);

	public abstract Single<ResponseResult> removePlan(int id);

	public abstract Single<ResponseSetting> getNotificationInformation(String identifier);

	public abstract Completable updateReceiveNotification(String uniqueIdentifier, Constants.NotificationFilter which, boolean isOn);

	public abstract Single<ResponseGeocode> convertCoordToAddress(String query);

	public abstract Single<ResponseResult> updateToken(String uniqueIdentifier, String newToken);

	public abstract Single<ResponseNickname> getTitle(String uniqueIdentifier);

	public abstract Single<ResponseResult> registerNickname(Nickname nickname);

	public abstract Single<ResponseMode> checkMode(String uniqueIdentifier);

	public abstract Single<ResponseResult> checkNewNotice(String uniqueIdentifier);

	public abstract Single<List<Notice>> getNotice(String uniqueIdentifier);

}
