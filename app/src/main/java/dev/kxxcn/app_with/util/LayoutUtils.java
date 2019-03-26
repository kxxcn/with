package dev.kxxcn.app_with.util;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import static dev.kxxcn.app_with.util.Constants.POSITION_BOTTOM;
import static dev.kxxcn.app_with.util.Constants.POSITION_CENTER;
import static dev.kxxcn.app_with.util.Constants.POSITION_TOP;

/**
 * Created by kxxcn on 2018-10-31.
 */
public class LayoutUtils {
	public static RelativeLayout.LayoutParams getRelativeLayoutParams(int position) {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		if (position == POSITION_CENTER) {
			params.addRule(RelativeLayout.CENTER_VERTICAL);
		} else if (position == POSITION_BOTTOM) {
			params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		} else if (position == POSITION_TOP) {
			params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		}
		return params;
	}

	public static <T extends View> void setViewPosition(ConstraintLayout constraintLayout, int position, T view) {
		ConstraintSet set = new ConstraintSet();
		set.clone(constraintLayout);
		if (position == POSITION_CENTER) {
			set.connect(view.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP, 0);
			set.connect(view.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM, 0);
		} else if (position == POSITION_BOTTOM) {
			set.connect(view.getId(), ConstraintSet.TOP, ConstraintSet.UNSET, ConstraintSet.TOP, 0);
			set.connect(view.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM, 50);
		} else if (position == POSITION_TOP) {
			set.connect(view.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP, 50);
			set.connect(view.getId(), ConstraintSet.BOTTOM, ConstraintSet.UNSET, ConstraintSet.BOTTOM, 0);
		}
		set.applyTo(constraintLayout);
	}

	public static <T extends View> void setViewPosition(ConstraintLayout constraintLayout, int position, T view, T relativeView) {
		ConstraintSet set = new ConstraintSet();
		set.clone(constraintLayout);
		if (position == POSITION_CENTER) {
			set.connect(view.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP, 0);
			set.connect(view.getId(), ConstraintSet.BOTTOM, relativeView.getId(), ConstraintSet.TOP, 0);
		} else if (position == POSITION_BOTTOM) {
			set.connect(view.getId(), ConstraintSet.TOP, ConstraintSet.UNSET, ConstraintSet.TOP, 0);
			set.connect(view.getId(), ConstraintSet.BOTTOM, relativeView.getId(), ConstraintSet.TOP, 100);
		} else if (position == POSITION_TOP) {
			set.connect(view.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP, 100);
			set.connect(view.getId(), ConstraintSet.BOTTOM, ConstraintSet.UNSET, ConstraintSet.BOTTOM, 0);
		}
		set.applyTo(constraintLayout);
	}

	public static <T extends View> void setViewPosition2(ConstraintLayout constraintLayout, int position, T view, T relativeView) {
		ConstraintSet set = new ConstraintSet();
		set.clone(constraintLayout);
		if (position == POSITION_CENTER) {
			set.connect(view.getId(), ConstraintSet.TOP, relativeView.getId(), ConstraintSet.BOTTOM, 0);
			set.connect(view.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM, 0);
		} else if (position == POSITION_BOTTOM) {
			set.connect(view.getId(), ConstraintSet.TOP, ConstraintSet.UNSET, ConstraintSet.TOP, 0);
			set.connect(view.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM, 30);
		} else if (position == POSITION_TOP) {
			set.connect(view.getId(), ConstraintSet.TOP, relativeView.getId(), ConstraintSet.BOTTOM, 10);
			set.connect(view.getId(), ConstraintSet.BOTTOM, ConstraintSet.UNSET, ConstraintSet.BOTTOM, 0);
		}
		set.applyTo(constraintLayout);
	}


	public static <T extends View> void setViewPosition3(ConstraintLayout constraintLayout, int position, T view, T relativeView) {
		ConstraintSet set = new ConstraintSet();
		set.clone(constraintLayout);
		set.connect(view.getId(), ConstraintSet.TOP, relativeView.getId(), ConstraintSet.BOTTOM, 100);
		set.connect(view.getId(), ConstraintSet.BOTTOM, ConstraintSet.UNSET, ConstraintSet.BOTTOM, 0);
		set.applyTo(constraintLayout);
	}

}
