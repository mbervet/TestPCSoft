package com.test.calendarview;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.CalendarView;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;

public class Calendar extends CalendarView {

    private final int ACTION_SCROLL_UP = 1;
    private final int ACTION_SCROLL_DOWN = 2;
    private final int ACTION_LONG_CLICK = 3;
    private final int LongClickTimeUP = 300;

    private int TouchSlop;
    private int Action = 0;

    public Calendar(@NonNull Context context) {
        super(context);
        setFirstDayOfWeek(2);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setLongClickable(true);
        ViewConfiguration vc = ViewConfiguration.get(context);
        TouchSlop = vc.getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if(event.getActionMasked() != MotionEvent.ACTION_MOVE && event.getHistorySize() == 0){
            if (event.getEventTime() - event.getDownTime() > LongClickTimeUP){
                Action = ACTION_LONG_CLICK;

                return true;
            }

            return false;
        }

        System.out.println(event.getHistorySize());
        System.out.println(event.getDownTime() - event.getEventTime());

        float xDiff = Math.abs(event.getHistoricalX(0) - event.getX());
        float yDiff = event.getHistoricalY(0) - event.getY();
        System.out.println(xDiff + " " + yDiff);

        if (Math.abs(yDiff) > xDiff && Math.abs(yDiff) > TouchSlop) {
            if (yDiff < 0) {
                Action = ACTION_SCROLL_DOWN;
            } else if (yDiff > 0) {
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
        if (Action == ACTION_SCROLL_UP){
            setDate(getDate() + TimeUnit.DAYS.toMillis(365));
        }
        else if (Action == ACTION_SCROLL_DOWN){
            setDate(getDate() - TimeUnit.DAYS.toMillis(365));
        }
        else if (Action == ACTION_LONG_CLICK){
            System.out.println("LongPress");
        }

        return super.performClick();
    }
}
