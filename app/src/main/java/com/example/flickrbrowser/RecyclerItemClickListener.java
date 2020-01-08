package com.example.flickrbrowser;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;

class RecyclerItemClickListener extends RecyclerView.SimpleOnItemTouchListener {

    private static final String TAG = "RecyclerItemClickListen";
    private final OnRecyclerClickListener memberListener;
    private final GestureDetectorCompat gestureDetector;

    interface OnRecyclerClickListener{
        void onItemsClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, final OnRecyclerClickListener memberListener) {
        this.memberListener = memberListener;
        this.gestureDetector = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Log.d(TAG, "onSingleTapUp: Starts");
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if(childView!=null && memberListener!=null)
                {
                    Log.d(TAG, "onSingleTapUp: Calling memberListener on item click");
                    memberListener.onItemsClick(childView, recyclerView.getChildAdapterPosition(childView));
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                Log.d(TAG, "onLongPress: Starts");
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if(childView!=null && memberListener!=null)
                {
                    Log.d(TAG, "onLongPress: calling memberListener on long click");
                    memberListener.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        Log.d(TAG, "onInterceptTouchEvent: Starts");
        if(gestureDetector != null)
        {
            boolean result = gestureDetector.onTouchEvent(e);
            Log.d(TAG, "onInterceptTouchEvent: returned " + result);
            return result;
        }
        else
        {
            Log.d(TAG, "onInterceptTouchEvent: returned false ");
            return false;
        }
    }
}
