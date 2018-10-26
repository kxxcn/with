package dev.kxxcn.app_with.util;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import dev.kxxcn.app_with.ui.main.plan.PlanAdapter;

/**
 * Created by kxxcn on 2018-10-19.
 */
public class RecyclerViewItemTouchHelper extends ItemTouchHelper.SimpleCallback {

	private RecyclerViewItemTouchHelperListener mListener;

	public RecyclerViewItemTouchHelper(int dragDirs, int swipeDirs, RecyclerViewItemTouchHelperListener listener) {
		super(dragDirs, swipeDirs);
		this.mListener = listener;
	}

	@Override
	public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
		return true;
	}

	@Override
	public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
		if (viewHolder != null) {
			final View foregroundView = ((PlanAdapter.ViewHolder) viewHolder).cv_foreground;

			getDefaultUIUtil().onSelected(foregroundView);
		}
	}

	@Override
	public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
								RecyclerView.ViewHolder viewHolder, float dX, float dY,
								int actionState, boolean isCurrentlyActive) {
		final View foregroundView = ((PlanAdapter.ViewHolder) viewHolder).cv_foreground;
		getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
				actionState, isCurrentlyActive);
	}

	@Override
	public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
		final View foregroundView = ((PlanAdapter.ViewHolder) viewHolder).cv_foreground;
		getDefaultUIUtil().clearView(foregroundView);
	}

	@Override
	public void onChildDraw(Canvas c, RecyclerView recyclerView,
							RecyclerView.ViewHolder viewHolder, float dX, float dY,
							int actionState, boolean isCurrentlyActive) {
		final View foregroundView = ((PlanAdapter.ViewHolder) viewHolder).cv_foreground;

		getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
				actionState, isCurrentlyActive);
	}

	@Override
	public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
		mListener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
	}

	@Override
	public int convertToAbsoluteDirection(int flags, int layoutDirection) {
		return super.convertToAbsoluteDirection(flags, layoutDirection);
	}

	public interface RecyclerViewItemTouchHelperListener {
		void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
	}

}
