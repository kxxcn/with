package dev.kxxcn.app_with.data;

import java.util.List;

import dev.kxxcn.app_with.data.model.diary.Diary;
import dev.kxxcn.app_with.data.model.pairing.ResponsePairing;
import dev.kxxcn.app_with.data.model.plan.Plan;
import dev.kxxcn.app_with.data.model.result.ResponseResult;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;

/**
 * Created by kxxcn on 2018-08-20.
 */
public abstract class DataSource {

	public abstract Single<ResponsePairing> onCreatePairingKey(String uniqueIdentifier);

	public abstract Single<ResponseResult> onAuthenticate(String uniqueIdentifier, String key, int gender);

	public abstract Single<ResponseResult> isRegisteredUser(String uniqueIdentifier);

	public abstract Single<List<Diary>> onGetDiary(int flag, String uniqueIdentifier);

	public abstract Single<ResponseResult> onRegisterDiary(Diary diary);

	public abstract Single<ResponseResult> onRegisterPlan(Plan plan);

	public abstract Single<List<Plan>> onGetPlan(String uniqueIdentifier);

	public abstract Single<ResponseResult> uploadImage(MultipartBody.Part body);

	public abstract Single<ResponseBody> onGetImage(String fileName);

	public abstract Single<ResponseResult> onRemoveDiary(int id);

	public abstract Single<ResponseResult> onRemovePlan(int id);

}
