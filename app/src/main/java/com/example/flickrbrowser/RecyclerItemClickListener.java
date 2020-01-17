package com.example.flickrbrowser;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;

class RecyclerItemClickListener extends RecyclerView.SimpleOnItemTouchListener {
    //https://developer.android.com/reference/android/support/v7/widget/RecyclerView.SimpleOnItemTouchListener.html
    private final OnRecyclerClickListener memberListener;
    private final GestureDetectorCompat gestureDetector;//for gesture detection

    interface OnRecyclerClickListener//for listening to different types of clicks
    {
        void onItemsClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, final OnRecyclerClickListener memberListener) {
        this.memberListener = memberListener;
        this.gestureDetector = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener(){
            //Detects various gestures and events using the supplied MotionEvents.
            // The GestureDetector.OnGestureListener callback will notify users when a particular motion event has occurred.
            // This class should only be used with MotionEvents reported via touch (don't use for trackball events).
            //https://developer.android.com/reference/android/support/v4/view/GestureDetectorCompat
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                //https://developer.android.com/reference/kotlin/androidx/recyclerview/widget/RecyclerView#findchildviewunder

                if(childView!=null && memberListener!=null)
                {
                    memberListener.onItemsClick(childView, recyclerView.getChildAdapterPosition(childView));
                    //https://developer.android.com/reference/kotlin/androidx/recyclerview/widget/RecyclerView#getchildadapterposition
                }
                return true;//means we have handled it
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if(childView!=null && memberListener!=null)
                {
                    memberListener.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        if(gestureDetector != null)
        {
            boolean result = gestureDetector.onTouchEvent(e);
            return result;
        }
        else
        {
            return false;
        }
    }
}
