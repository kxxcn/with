package dev.kxxcn.app_with.ui.main;

import dev.kxxcn.app_with.util.Constants;

/**
 * Created by kxxcn on 2018-08-14.
 */
public interface MainContract {
	@FunctionalInterface
	interface OnItemClickListener {
		void onItemClickListener(int position, Constants.TypeFilter typeFilter);

	}

	@FunctionalInterface
	interface OnRegisteredDiary {
		void onRegisteredDiary(int type);
	}

	@FunctionalInterface
	interface OnPageChangeListener {
		void onPageChangeListener(int type);
	}
}
