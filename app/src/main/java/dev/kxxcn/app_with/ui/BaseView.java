package dev.kxxcn.app_with.ui;

/**
 * Created by kxxcn on 2018-08-13.
 */
public interface BaseView<T> {
	void setPresenter(T presenter);

	void showLoadingIndicator(boolean isShowing);
}
