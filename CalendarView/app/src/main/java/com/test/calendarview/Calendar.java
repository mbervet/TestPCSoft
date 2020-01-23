package com.test.calendarview;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.CalendarView;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;

public class Calendar extends CalendarView {

    private static final int ACTION_SCROLL_UP = 1;
    private static final int ACTION_SCROLL_DOWN = 2;

    private int TouchSlop;
    private int Action;

    public Calendar(@NonNull Context context) {
        super(context);
        setFirstDayOfWeek(2);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ViewConfiguration vc = ViewConfiguration.get(context);
        TouchSlop = vc.getScaledTouchSlop();
        Action = 0;
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
            if (yDiff < 0){
                Action = ACTION_SCROLL_DOWN;
            }
            else if (yDiff > 0){
                Action = ACTION_SCROLL_UP;
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
        final long msYear = TimeUnit.DAYS.toMillis(365);

        if (Action == ACTION_SCROLL_UP){
            setDate(getDate() + msYear);
        }
        else if (Action == ACTION_SCROLL_DOWN){
            setDate(getDate() - msYear);
        }

        return super.performClick();
    }
}
