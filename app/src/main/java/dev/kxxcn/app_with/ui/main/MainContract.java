package dev.kxxcn.app_with.ui.main;

/**
 * Created by kxxcn on 2018-08-14.
 */
public interface MainContract {
	interface OnClickCallback {
		void onClickCallback();
	}

	interface OnItemClickListener {
		void onItemClickListener(int position, int type);
	}
}
