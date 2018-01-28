package com.avantgarde.timy.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.avantgarde.timy.App;
import com.avantgarde.timy.R;

public class SwipeCallback extends ItemTouchHelper.SimpleCallback{

    private OnSwipeListener onSwipeListener;
    public interface OnSwipeListener{
        void onSwipe(int direction, int position);
    }
    public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
        this.onSwipeListener = onSwipeListener;
    }

    private Paint paint;

    private int leftColor;
    private Drawable leftDrawable;

    private int rightColor;
    private Drawable rightDrawable;

    public SwipeCallback(int swipeDirs) {
        super(0, swipeDirs);
        paint = new Paint();
    }

    public void configureLeft( int color, int drawableId){
        leftColor = color;
        leftDrawable = AppCompatResources.getDrawable(App.get(), drawableId);
    }

    public void configureRight( int color, int drawableId){
        rightColor = color;
        rightDrawable = AppCompatResources.getDrawable(App.get(), drawableId);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Log.d("SwipeCallback", "onSwiped: "+viewHolder.getAdapterPosition()+"  "+viewHolder.getLayoutPosition()+"  "+viewHolder.getOldPosition());
        if (onSwipeListener != null) onSwipeListener.onSwipe(direction, viewHolder.getAdapterPosition());
    }

    @Override
    public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

            View itemView = viewHolder.itemView;
            int width =  itemView.getHeight() / 3;

            if (dX > 0) {
                draw(canvas, leftDrawable, leftColor,
                        new Rect(itemView.getLeft(), itemView.getTop(), (int) dX, itemView.getBottom()),
                        new Rect(itemView.getLeft() + width, itemView.getTop() + width, itemView.getLeft() + 2 * width, itemView.getBottom() - width));
            } else {
                draw(canvas, rightDrawable, rightColor,
                        new Rect(itemView.getRight() + (int)dX, itemView.getTop(), itemView.getRight(), itemView.getBottom()),
                        new Rect(itemView.getRight() - 2 * width, itemView.getTop() + width, itemView.getRight() - width, itemView.getBottom() - width));
            }

        }
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    private void draw(Canvas c, Drawable drawable, int color, Rect background, Rect icon){
        paint.setColor(color);
        c.drawRect(background, paint);
        drawable.setBounds(icon);
        drawable.draw(c);
    }

    public void attach(RecyclerView recyclerView) {
        new ItemTouchHelper(this).attachToRecyclerView(recyclerView);
    }

}
