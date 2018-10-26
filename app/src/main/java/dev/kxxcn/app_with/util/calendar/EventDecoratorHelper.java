package dev.kxxcn.app_with.util.calendar;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by kxxcn on 2018-10-01.
 */
public class EventDecoratorHelper implements DayViewDecorator {

	private int color;

	private HashSet<CalendarDay> dates;

	public EventDecoratorHelper(int color, Collection<CalendarDay> dates) {
		this.color = color;
		this.dates = new HashSet<>(dates);
	}

	@Override
	public boolean shouldDecorate(CalendarDay calendarDay) {
		return dates.contains(calendarDay);
	}

	@Override
	public void decorate(DayViewFacade dayViewFacade) {
		dayViewFacade.addSpan(new DotSpan(7, color));
	}

}
