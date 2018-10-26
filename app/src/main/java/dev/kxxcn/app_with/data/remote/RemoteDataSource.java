package dev.kxxcn.app_with.data.remote;

import java.util.List;

import dev.kxxcn.app_with.data.DataSource;
import dev.kxxcn.app_with.data.model.diary.Diary;
import dev.kxxcn.app_with.data.model.pairing.ResponsePairing;
import dev.kxxcn.app_with.data.model.plan.Plan;
import dev.kxxcn.app_with.data.model.result.ResponseResult;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;

/**
 * Created by kxxcn on 2018-08-20.
 */
public class RemoteDataSource extends DataSource {

	private static RemoteDataSource remoteDataSource;

	private APIService service;

	private RemoteDataSource() {
		this.service = APIService.Factory.create();
	}

	public static synchronized RemoteDataSource getInstance() {
		if (remoteDataSource == null) {
			remoteDataSource = new RemoteDataSource();
		}
		return remoteDataSource;
	}

	@Override
	public Single<ResponsePairing> onCreatePairingKey(String uniqueIdentifier) {
		return service.createPairingKey(uniqueIdentifier)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

	@Override
	public Single<ResponseResult> onAuthenticate(String uniqueIdentifier, String key, int gender) {
		return service.authenticateKey(uniqueIdentifier, key, gender)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

	@Override
	public Single<ResponseResult> isRegisteredUser(String uniqueIdentifier) {
		return service.isRegisteredUser(uniqueIdentifier)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

	@Override
	public Single<List<Diary>> onGetDiary(int flag, String uniqueIdentifier) {
		return service.getDiary(flag, uniqueIdentifier)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

	@Override
	public Single<ResponseResult> onRegisterDiary(Diary diary) {
		return service.registerDiary(diary)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

	@Override
	public Single<ResponseResult> onRegisterPlan(Plan plan) {
		return service.registerPlan(plan)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

	@Override
	public Single<List<Plan>> onGetPlan(String uniqueIdentifier) {
		return service.getPlan(uniqueIdentifier)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

	@Override
	public Single<ResponseResult> uploadImage(MultipartBody.Part body) {
		return service.uploadImage(body)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

	@Override
	public synchronized Single<ResponseBody> onGetImage(String fileName) {
		return service.getImage(fileName)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

	@Override
	public Single<ResponseResult> onRemoveDiary(int id) {
		return service.removeDiary(id)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}


	@Override
	public Single<ResponseResult> onRemovePlan(int id) {
		return service.removePlan(id)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}

}
