package dev.kxxcn.app_with.data;

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


	public abstract void onCreatePairingKey(GetKeyCallback callback, String uniqueIdentifier);

	public abstract void onAuthenticate(GetResultCallback callback, String uniqueIdentifier, String key, int gender);

	public abstract void isRegisteredUser(GetResultCallback callback, String uniqueIdentifier);
}
