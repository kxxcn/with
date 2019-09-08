package dev.kxxcn.app_with.util;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import dev.kxxcn.app_with.R;

/**
 * Created by kxxcn on 2018-08-29.
 */
public class TransitionUtils {

    private static final float ANIMATE_SHOW = 1.0f;
    private static final float ANIMATE_HIDE = 0.0f;

    public static void fade(Activity activity) {
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public static <T extends View> void startAnimationEditText(EditText editText, T view) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int length = charSequence.length();
                if (view.getAlpha() == ANIMATE_HIDE) {
                    if (length >= 1) {
                        view.animate().alpha(ANIMATE_SHOW).setDuration(Constants.ANIMATE_DURATION_LONG);
                    }
                } else if (view.getAlpha() == ANIMATE_SHOW) {
                    if (length == 0) {
                        view.animate().alpha(ANIMATE_HIDE).setDuration(Constants.ANIMATE_DURATION_LONG);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public static void startAnimationLine(View view, boolean isShowing, int animateViewWidth) {
        ValueAnimator animator;
        if (isShowing) {
            animator = ValueAnimator.ofInt(0, animateViewWidth);
        } else {
            animator = ValueAnimator.ofInt(animateViewWidth, 0);
        }
        animator.addUpdateListener(it -> {
            int width = (int) it.getAnimatedValue();
            if (view == null) return;
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.width = width;
            view.setLayoutParams(params);
            if (!isShowing && width == 0) {
                view.setVisibility(View.GONE);
            } else if (isShowing && width == 0) {
                view.setVisibility(View.VISIBLE);
            }

        });
        animator.setDuration(Constants.ANIMATE_DURATION_LONG);
        animator.start();
    }

    public static ValueAnimator setAnimationSlideLayout(View view, int animateViewHeight) {
        ValueAnimator animator;
        animator = ValueAnimator.ofInt(0, animateViewHeight);

        animator.addUpdateListener(it -> {
            int height = (int) it.getAnimatedValue();
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.height = height;
            view.setLayoutParams(params);
        });
        animator.setDuration(Constants.ANIMATE_DURATION_SHORT);
        return animator;
    }
}
