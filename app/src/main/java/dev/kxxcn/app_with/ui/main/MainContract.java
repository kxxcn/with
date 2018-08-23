package dev.kxxcn.app_with.ui.main;

import dev.kxxcn.app_with.util.Constants;

/**
 * Created by kxxcn on 2018-08-14.
 */
public interface MainContract {
	interface OnItemClickListener {
		void onItemClickListener(int position, Constants.TypeFilter typeFilter);
	}
}
