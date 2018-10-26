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

	public Single<ResponsePairing> onCreatePairingKey(String uniqueIdentifier) {
		return dataSource.onCreatePairingKey(uniqueIdentifier);
	}

	public Single<ResponseResult> onAuthenticate(String uniqueIdentifier, String key, int gender) {
		return dataSource.onAuthenticate(uniqueIdentifier, key, gender);
	}

	public Single<ResponseResult> isRegisteredUser(String uniqueIdentifier) {
		return dataSource.isRegisteredUser(uniqueIdentifier);
	}

	public Single<List<Diary>> onGetDiary(int flag, String uniqueIdentifier) {
		return dataSource.onGetDiary(flag, uniqueIdentifier);
	}

	public Single<ResponseResult> onRegisterDiary(Diary diary) {
		return dataSource.onRegisterDiary(diary);
	}

	public Single<ResponseResult> onRegisterPlan(Plan plan) {
		return dataSource.onRegisterPlan(plan);
	}

	public Single<List<Plan>> onGetPlan(String identifier) {
		return dataSource.onGetPlan(identifier);
	}

	public Single<ResponseResult> uploadImage(MultipartBody.Part body) {
		return dataSource.uploadImage(body);
	}

	public Single<ResponseBody> onGetImage(String fileName) {
		return dataSource.onGetImage(fileName);
	}

	public Single<ResponseResult> onRemoveDiary(int id) {
		return dataSource.onRemoveDiary(id);
	}

	public Single<ResponseResult> onRemovePlan(int id) {
		return dataSource.onRemovePlan(id);
	}

}
