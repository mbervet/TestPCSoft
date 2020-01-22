package com.test.calendarview;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.CalendarView;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;

public class Calendar extends CalendarView {

    private int TouchSlop;
    private long DateChange;

    public Calendar(@NonNull Context context) {
        super(context);
        setFirstDayOfWeek(2);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ViewConfiguration vc = ViewConfiguration.get(context);
        TouchSlop = vc.getScaledTouchSlop();
        DateChange = TimeUnit.DAYS.toMillis(365);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        final int action = event.getActionMasked();

        if(event.getHistorySize() == 0 || action != MotionEvent.ACTION_MOVE){
            return false;// Do not intercept touch event, let the child handle it
        }

        float xDiff = Math.abs(event.getHistoricalX(0) - event.getX());
        float yDiff = event.getHistoricalY(0) - event.getY();

        if (Math.abs(yDiff) > xDiff && Math.abs(yDiff) > TouchSlop){
            if (yDiff < 0 && DateChange > 0){
                DateChange *= -1;
            }

            if (yDiff > 0 && DateChange < 0){
                DateChange *= -1;
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_UP){
            performClick();
            return true;
        }

        return false;
    }

    @Override
    public boolean performClick(){
        setDate(getDate() + DateChange);
        return super.performClick();
    }
}
