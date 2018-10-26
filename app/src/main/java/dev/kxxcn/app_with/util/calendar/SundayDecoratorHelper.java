package dev.kxxcn.app_with.util.calendar;

import android.graphics.Color;
import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import org.threeten.bp.DayOfWeek;

/**
 * Created by kxxcn on 2018-10-01.
 */
public class SundayDecoratorHelper implements DayViewDecorator {

	@Override
	public boolean shouldDecorate(CalendarDay calendarDay) {
		return calendarDay.getDate().getDayOfWeek() == DayOfWeek.SUNDAY;
	}

	@Override
	public void decorate(DayViewFacade dayViewFacade) {
		dayViewFacade.addSpan(new ForegroundColorSpan(Color.RED));
	}

}
