package dev.kxxcn.app_with.data.remote;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import dev.kxxcn.app_with.data.DataSource;
import dev.kxxcn.app_with.data.model.diary.Diary;
import okhttp3.ResponseBody;
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
					dev.kxxcn.app_with.data.model.pairing.Response res = response.body();
					if (res != null) {
						callback.onSuccess(res.getKey());
					}
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
					dev.kxxcn.app_with.data.model.result.Response res = response.body();
					if (res != null) {
						if (res.getRc() == 200) {
							callback.onSuccess();

						} else if (res.getRc() == 201) {
							callback.onRequestFailure(res.getStat());
						}
					}
				} else {
					ResponseBody res = response.errorBody();
					if (res != null) {
						callback.onRequestFailure(res.toString());
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
	public void isRegisteredUser(GetGenderCallback callback, String uniqueIdentifier) {
		Call<dev.kxxcn.app_with.data.model.result.Response> call = service.isRegisteredUser(uniqueIdentifier);
		call.enqueue(new Callback<dev.kxxcn.app_with.data.model.result.Response>() {
			@Override
			public void onResponse(Call<dev.kxxcn.app_with.data.model.result.Response> call, Response<dev.kxxcn.app_with.data.model.result.Response> response) {
				if (response.isSuccessful()) {
					dev.kxxcn.app_with.data.model.result.Response res = response.body();
					if (res != null) {
						if (res.getRc() == 200) {
							callback.onSuccess(Integer.parseInt(res.getStat()));
						} else if (res.getRc() == 201) {
							callback.onRequestFailure(res.getStat());
						}
					}
				} else {
					ResponseBody res = response.errorBody();
					if (res != null) {
						callback.onRequestFailure(res.toString());
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
	public void onGetDiary(GetDiaryCallback callback, int flag, String uniqueIdentifier) {
		Call<List<Diary>> call = service.getDiary(flag, uniqueIdentifier);
		call.enqueue(new Callback<List<Diary>>() {
			@Override
			public void onResponse(Call<List<Diary>> call, Response<List<Diary>> response) {
				if (response.isSuccessful()) {
					callback.onSuccess(response.body());
				} else {
					callback.onNetworkFailure();
				}
			}

			@Override
			public void onFailure(Call<List<Diary>> call, Throwable t) {
				callback.onFailure(t);
			}
		});
	}

	@ParametersAreNonnullByDefault
	@Override
	public void onRegisterDiary(GetResultCallback callback, Diary diary) {
		Call<dev.kxxcn.app_with.data.model.result.Response> call = service.saveDiary(diary);
		call.enqueue(new Callback<dev.kxxcn.app_with.data.model.result.Response>() {
			@Override
			public void onResponse(Call<dev.kxxcn.app_with.data.model.result.Response> call, Response<dev.kxxcn.app_with.data.model.result.Response> response) {
				if (response.isSuccessful()) {
					dev.kxxcn.app_with.data.model.result.Response res = response.body();
					if (res != null) {
						if (res.getRc() == 200) {
							callback.onSuccess();
						} else if (res.getRc() == 201) {
							callback.onRequestFailure(res.getStat());
						}
					}
				} else {
					ResponseBody res = response.errorBody();
					if (res != null) {
						callback.onRequestFailure(res.toString());
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
