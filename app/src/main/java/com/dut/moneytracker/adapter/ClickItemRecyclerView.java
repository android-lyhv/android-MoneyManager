package com.dut.moneytracker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by HoVanLy on 6/14/2016.
 */
public class ClickItemRecyclerView implements RecyclerView.OnItemTouchListener {
    private GestureDetector gestureDetector;
    private ClickItemListener clickItemListener;

    public ClickItemRecyclerView(final Context context, final RecyclerView recyclerView, final ClickItemListener clickItemListener) {
        this.clickItemListener = clickItemListener;
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View viewChild = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (viewChild != null && clickItemListener != null) {
                    clickItemListener.onLongClick(viewChild, recyclerView.getChildAdapterPosition(viewChild));
                }
            }
        });

    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View viewChild = rv.findChildViewUnder(e.getX(), e.getY());
        if (viewChild != null && clickItemListener != null && gestureDetector.onTouchEvent(e)) {
            clickItemListener.onClick(viewChild, rv.getChildAdapterPosition(viewChild));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
