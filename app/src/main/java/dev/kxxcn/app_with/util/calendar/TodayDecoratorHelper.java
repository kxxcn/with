package dev.kxxcn.app_with.util.calendar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import dev.kxxcn.app_with.R;

/**
 * Created by kxxcn on 2018-10-01.
 */
public class TodayDecoratorHelper implements DayViewDecorator {

	private Context context;

	private CalendarDay today;

	public TodayDecoratorHelper(Context context) {
		this.context = context;
		this.today = CalendarDay.today();
	}

	@Override
	public boolean shouldDecorate(CalendarDay calendarDay) {
		return calendarDay.equals(today);
	}

	@Override
	public void decorate(DayViewFacade dayViewFacade) {
		dayViewFacade.addSpan(new StyleSpan(Typeface.BOLD));
		dayViewFacade.addSpan(new ForegroundColorSpan(Color.WHITE));
		dayViewFacade.setSelectionDrawable(context.getResources().getDrawable(R.drawable.drawable_circle_today));
	}

}
