package com.avantgarde.timy.view.main.dashboard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.CardView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.avantgarde.timy.R;

public class TimeCardView extends CardView implements View.OnClickListener{

    private ArgbEvaluator evaluator;

    private int accentColor;

    private boolean active;
    private boolean timeVisible;

    private TextView headView;
    private TextView timeView;
    private ImageView closeView;

    private int whiteTransparentColor;

    private float centerX;
    private float centerY;

    private float timeY;
    private float timeX;

    int h;
    int m;

    String time;

    boolean hours = true;

    float sp;

    private OnStateChangedListener onStateChangedListener;

    public interface OnStateChangedListener{
        void onStateChanged(boolean active);
    }

    public void setOnStateChangedListener(OnStateChangedListener onStateChangedListener) {
        this.onStateChangedListener = onStateChangedListener;
    }

    public TimeCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        sp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 1, getResources().getDisplayMetrics());
        accentColor = ContextCompat.getColor(context, R.color.colorAccent);
        whiteTransparentColor = Color.parseColor("#8affffff");
        evaluator = new ArgbEvaluator();

        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TimeCardView, 0, 0);

        LayoutInflater.from(context).inflate(R.layout.time_card, this, true);

        headView = findViewById(R.id.text);
        timeView = findViewById(R.id.time);
        closeView = findViewById(R.id.close);

        closeView.setOnClickListener(view -> {
            if (timeVisible)
                setTimeVisible(false);
            if(active) onClick(this);
        });

        headView.setText(array.getString(R.styleable.TimeCardView_text));

        headView.post(() -> {
            centerX = headView.getX();
            centerY = headView.getY();

            timeX = timeView.getX();
            timeY = timeView.getY();

            if (array.getBoolean(R.styleable.TimeCardView_showTime, false)) {

                timeView.setY(timeY + 5 * sp);
                timeView.setAlpha(1);

                headView.setAlpha(0.54f);
                headView.setTextSize(10);
                headView.post(() -> {
                    headView.setY(timeY - 5 * sp);
                    headView.setX(timeX);
                });
            }

        });

        setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        active = !active;

        if (onStateChangedListener != null) {
            onStateChangedListener.onStateChanged(active);
        }

        setActive(active);

    }

    public void setTime(String time){
        this.time = time;
    }

    public String getTime(){
        return time;
    }

    public int getMinutes() {
        return h * 60 + m;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;

        ValueAnimator color = ValueAnimator.ofFloat(active ? 0 : 1, active ? 1 : 0);
        color.setDuration(200);
        color.addUpdateListener(valueAnimator -> {

            float value = (float) valueAnimator.getAnimatedValue();
            int backColor = (int) evaluator.evaluate(value, Color.WHITE, accentColor);
            int headColor = (int) evaluator.evaluate(value, accentColor, Color.WHITE);
            int timeColor = (int) evaluator.evaluate(value, accentColor, whiteTransparentColor);

            this.setCardBackgroundColor(backColor);
            headView.setTextColor(headColor);
            timeView.setText(decorTime(headColor, timeColor, timeColor, hours, !hours));
            DrawableCompat.setTint(closeView.getDrawable(),headColor);

        });
        color.start();

    }

    public void setTimeVisible(boolean visible) {

        timeVisible = visible;

        if (visible) {
            timeView.animate()
                    .setDuration(200)
                    .y(timeY + 5 * sp)
                    .alpha(1);
            headView.animate()
                    .setDuration(200)
                    .alpha(0.54f)
                    .y(timeY - 5 * sp)
                    .x(timeX);
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(headView.getTextSize() / sp, 10);
            valueAnimator.setDuration(200);
            valueAnimator.addUpdateListener(valueAnimator1 -> {
                headView.setTextSize((float) valueAnimator1.getAnimatedValue());
            });
            valueAnimator.start();

            closeView.animate()
                    .setDuration(200)
                    .alpha(1)
                    .rotation(90);

        } else {
            timeView.animate()
                    .setDuration(200)
                    .y(timeY)
                    .alpha(0);
            headView.animate()
                    .setDuration(200)
                    .alpha(1)
                    .y(centerY)
                    .x(centerX);
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(headView.getTextSize() / sp, 18);
            valueAnimator.setDuration(200);
            valueAnimator.addUpdateListener(valueAnimator1 -> {
                headView.setTextSize((float) valueAnimator1.getAnimatedValue());
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    time = null;
                }
            });
            valueAnimator.start();

            closeView.animate()
                    .setDuration(200)
                    .alpha(0)
                    .rotationBy(-90);
        }

        //anim
    }

    public void changeTime(boolean hours) {
        this.hours = hours;

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(200);
        valueAnimator.addUpdateListener(valueAnimator1 -> {

            float value = (float) valueAnimator1.getAnimatedValue();
            int color = (int) evaluator.evaluate(value, whiteTransparentColor, Color.WHITE);
            int colorT = (int) evaluator.evaluate(value, Color.WHITE, whiteTransparentColor);
            timeView.setText(decorTime(color, whiteTransparentColor, colorT, hours, !hours));

        });
        valueAnimator.start();

    }

    public void setTime(int h, int m) {

        this.h = h;
        this.m = m;
        time = (h < 10 ? "0" : "") + h + ":" + (m < 10 ? "0" : "") + m;

    }

    public void setDecor(int h, int m) {
        setTime(h, m);
        timeView.setText(decorTime(Color.WHITE, whiteTransparentColor, whiteTransparentColor, hours, !hours));
    }

    public void activate() {
        timeView.setText(decorTime(accentColor, accentColor, accentColor, hours, !hours));
    }

    private Spannable decorTime(int color, int colorD ,int colorT, boolean first, boolean second) {
        Spannable span = new SpannableString(time);
        ForegroundColorSpan c = new ForegroundColorSpan(color);
        ForegroundColorSpan c2 = new ForegroundColorSpan(colorT);
        span.setSpan(first ? c : c2, 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ForegroundColorSpan(colorD), 2, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(second ? c : c2, 3, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }

}
