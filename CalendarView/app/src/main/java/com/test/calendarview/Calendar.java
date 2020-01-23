package com.test.calendarview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.CalendarView;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Calendar extends CalendarView {

    private final int ACTION_SCROLL_UP = 1;
    private final int ACTION_SCROLL_DOWN = 2;
    private final int ACTION_LONG_CLICK = 3;

    private int TouchSlop;
    private int LongClickSlop;
    private int Action = 0;

    public Calendar(@NonNull Context context) {
        super(context);
        InitializeSlops(context);
    }

    public Calendar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        InitializeSlops(context);
    }

    public Calendar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        InitializeSlops(context);
    }

    private void InitializeSlops(Context context){
        ViewConfiguration vc = ViewConfiguration.get(context);
        TouchSlop = vc.getScaledTouchSlop();
        LongClickSlop = 1000;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean result = false;

        if(event.getHistorySize() == 0 || event.getActionMasked() != MotionEvent.ACTION_MOVE){
            if (event.getEventTime() - event.getDownTime() > LongClickSlop){
                Action = ACTION_LONG_CLICK;

                result = true;
            }
            else {
                result = false;
            }
        }
        else {
            float xDiff = Math.abs(event.getHistoricalX(0) - event.getX());
            float yDiff = event.getHistoricalY(0) - event.getY();

            if (Math.abs(yDiff) > xDiff && Math.abs(yDiff) > TouchSlop) {
                if (yDiff < 0) {
                    Action = ACTION_SCROLL_DOWN;
                } else if (yDiff > 0) {
                    Action = ACTION_SCROLL_UP;
                }

                result = true;
            }
        }

        return result;
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
            Log.d("Calendar","LongPress");
        }

        return super.performClick();
    }
}
