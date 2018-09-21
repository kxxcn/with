package dev.kxxcn.app_with.data;

import java.util.List;

import dev.kxxcn.app_with.data.model.diary.Diary;

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

	public void onCreatePairingKey(final DataSource.GetKeyCallback callback, String uniqueIdentifier) {
		dataSource.onCreatePairingKey(new DataSource.GetKeyCallback() {
			@Override
			public void onSuccess(String key) {
				callback.onSuccess(key);
			}

			@Override
			public void onNetworkFailure() {
				callback.onNetworkFailure();
			}

			@Override
			public void onFailure(Throwable throwable) {
				callback.onFailure(throwable);
			}
		}, uniqueIdentifier);
	}

	public void onAuthenticate(final DataSource.GetResultCallback callback, String uniqueIdentifier, String key, int gender) {
		dataSource.onAuthenticate(new DataSource.GetResultCallback() {
			@Override
			public void onSuccess() {
				callback.onSuccess();
			}

			@Override
			public void onFailure(Throwable throwable) {
				callback.onFailure(throwable);
			}

			@Override
			public void onRequestFailure(String stat) {
				callback.onRequestFailure(stat);
			}
		}, uniqueIdentifier, key, gender);
	}

	public void isRegisteredUser(DataSource.GetGenderCallback callback, String uniqueIdentifier) {
		dataSource.isRegisteredUser(new DataSource.GetGenderCallback() {
			@Override
			public void onSuccess(int gender) {
				callback.onSuccess(gender);
			}

			@Override
			public void onFailure(Throwable throwable) {
				callback.onFailure(throwable);
			}

			@Override
			public void onRequestFailure(String stat) {
				callback.onRequestFailure(stat);
			}
		}, uniqueIdentifier);
	}

	public void onGetDiary(DataSource.GetDiaryCallback callback, int flag, String uniqueIdentifier) {
		dataSource.onGetDiary(new DataSource.GetDiaryCallback() {
			@Override
			public void onSuccess(List<Diary> diaryList) {
				callback.onSuccess(diaryList);
			}

			@Override
			public void onFailure(Throwable throwable) {
				callback.onFailure(throwable);
			}

			@Override
			public void onNetworkFailure() {
				callback.onNetworkFailure();
			}
		}, flag, uniqueIdentifier);
	}

	public void onRegisterDiary(DataSource.GetResultCallback callback, Diary diary) {
		dataSource.onRegisterDiary(new DataSource.GetResultCallback() {
			@Override
			public void onSuccess() {
				callback.onSuccess();
			}

			@Override
			public void onFailure(Throwable throwable) {
				callback.onFailure(throwable);
			}

			@Override
			public void onRequestFailure(String stat) {
				callback.onRequestFailure(stat);
			}
		}, diary);
	}

}
