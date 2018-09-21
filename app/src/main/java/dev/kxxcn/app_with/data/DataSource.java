package dev.kxxcn.app_with.data;

import java.util.List;

import dev.kxxcn.app_with.data.model.diary.Diary;

/**
 * Created by kxxcn on 2018-08-20.
 */
public abstract class DataSource {
	public interface GetCommonCallback {
		void onSuccess();

		void onFailure(Throwable throwable);
	}

	public interface GetKeyCallback {
		void onSuccess(String key);

		void onFailure(Throwable throwable);

		void onNetworkFailure();
	}

	public interface GetResultCallback {
		void onSuccess();

		void onFailure(Throwable throwable);

		void onRequestFailure(String stat);
	}

	public interface GetGenderCallback {
		void onSuccess(int gender);

		void onFailure(Throwable throwable);

		void onRequestFailure(String stat);
	}

	public interface GetDiaryCallback {
		void onSuccess(List<Diary> diaryList);

		void onFailure(Throwable throwable);

		void onNetworkFailure();
	}


	public abstract void onCreatePairingKey(GetKeyCallback callback, String uniqueIdentifier);

	public abstract void onAuthenticate(GetResultCallback callback, String uniqueIdentifier, String key, int gender);

	public abstract void isRegisteredUser(GetGenderCallback callback, String uniqueIdentifier);

	public abstract void onGetDiary(GetDiaryCallback callback, int flag, String uniqueIdentifier);

	public abstract void onRegisterDiary(GetResultCallback callback, Diary diary);
}
