package dev.kxxcn.app_with.data;

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

	public void isRegisteredUser(DataSource.GetResultCallback callback, String uniqueIdentifier) {
		dataSource.isRegisteredUser(new DataSource.GetResultCallback() {
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
		}, uniqueIdentifier);
	}

}
