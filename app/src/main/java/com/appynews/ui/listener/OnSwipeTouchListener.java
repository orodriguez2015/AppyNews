package com.appynews.ui.listener;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

/**
 * Clase OnSwipeTouchListener que permite detectar cuando el usuario toca sobre un elemento
 * de un ListView, y detectar si el movimiento realizado encaja con un swipe hacia la derecha o
 * hacia la izquierda
 *
 * @author oscar

 */
public class OnSwipeTouchListener implements View.OnTouchListener {
    private ListView list = null;
    private GestureDetector gestureDetector = null;
    private Context context = null;
    private RecyclerView recyclerView = null;

    /**
     * Constructor
     * @param ctx: Context
     * @param list: ListView
     */
    public OnSwipeTouchListener(Context ctx, ListView list) {
        gestureDetector = new GestureDetector(ctx, new GestureListener());
        context = ctx;
        this.list = list;
    }


    public OnSwipeTouchListener(Context ctx, RecyclerView recyclerView) {
        gestureDetector = new GestureDetector(ctx, new GestureListener());
        context = ctx;
        this.recyclerView = recyclerView;
    }





    /**
     * Constructor
     */
    public OnSwipeTouchListener() {
        super();
    }

    /**
     * onTouch
     * @param v View
     * @param event MotionEvent
     * @return boolean
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }


    /**
     * onSwipeRight
     * @param pos int
     */
    public void onSwipeRight(int pos) {
    }

    /**
     * onSwipeLeft
     */
    public void onSwipeLeft() {
    }


    /**
     * GestureListener
     *
     */
    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        private int getPosition(MotionEvent e1) {
            return list.pointToPosition((int) e1.getX(), (int) e1.getY());

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float distanceX = e2.getX() - e1.getX();
            float distanceY = e2.getY() - e1.getY();
            if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (distanceX > 0)
                    onSwipeRight(getPosition(e1));
                else
                    onSwipeLeft();
                return true;
            }
            return false;
        }
    }
}
