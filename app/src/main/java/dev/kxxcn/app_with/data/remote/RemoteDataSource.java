package dev.kxxcn.app_with.data.remote;

import javax.annotation.ParametersAreNonnullByDefault;

import dev.kxxcn.app_with.data.DataSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

	@ParametersAreNonnullByDefault
	@Override
	public void onCreatePairingKey(final GetKeyCallback callback, String uniqueIdentifier) {
		Call<dev.kxxcn.app_with.data.model.pairing.Response> call = service.createPairingKey(uniqueIdentifier);
		call.enqueue(new Callback<dev.kxxcn.app_with.data.model.pairing.Response>() {
			@Override
			public void onResponse(Call<dev.kxxcn.app_with.data.model.pairing.Response> call, Response<dev.kxxcn.app_with.data.model.pairing.Response> response) {
				if (response.isSuccessful()) {
					callback.onSuccess(response.body().getKey());
				} else {
					callback.onNetworkFailure();
				}
			}

			@Override
			public void onFailure(Call<dev.kxxcn.app_with.data.model.pairing.Response> call, Throwable t) {
				callback.onFailure(t);
			}
		});
	}

	@ParametersAreNonnullByDefault
	@Override
	public void onAuthenticate(GetResultCallback callback, String uniqueIdentifier, String key, int gender) {
		Call<dev.kxxcn.app_with.data.model.result.Response> call = service.authenticateKey(uniqueIdentifier, key, gender);
		call.enqueue(new Callback<dev.kxxcn.app_with.data.model.result.Response>() {
			@Override
			public void onResponse(Call<dev.kxxcn.app_with.data.model.result.Response> call, Response<dev.kxxcn.app_with.data.model.result.Response> response) {
				if (response.isSuccessful()) {
					if (response.body().getRc() == 200) {
						callback.onSuccess();

					} else if (response.body().getRc() == 201) {
						callback.onRequestFailure(response.body().getStat());
					}
				}
			}

			@Override
			public void onFailure(Call<dev.kxxcn.app_with.data.model.result.Response> call, Throwable t) {
				callback.onFailure(t);
			}
		});
	}

	@ParametersAreNonnullByDefault
	@Override
	public void isRegisteredUser(GetResultCallback callback, String uniqueIdentifier) {
		Call<dev.kxxcn.app_with.data.model.result.Response> call = service.isRegisteredUser(uniqueIdentifier);
		call.enqueue(new Callback<dev.kxxcn.app_with.data.model.result.Response>() {
			@Override
			public void onResponse(Call<dev.kxxcn.app_with.data.model.result.Response> call, Response<dev.kxxcn.app_with.data.model.result.Response> response) {
				if (response.isSuccessful()) {
					if (response.body().getRc() == 200) {
						callback.onSuccess();

					} else if (response.body().getRc() == 201) {
						callback.onRequestFailure(response.body().getStat());
					}
				}
			}

			@Override
			public void onFailure(Call<dev.kxxcn.app_with.data.model.result.Response> call, Throwable t) {
				callback.onFailure(t);
			}
		});
	}

}
