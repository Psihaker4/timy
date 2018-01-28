package com.avantgarde.timy.view.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.avantgarde.timy.R;

public class ActionTimePicker extends FrameLayout {

    private Paint paint;
    private Paint chooserPaint;
    private Paint textPaint;
    private Paint hmTextPaint;

//    int choosed = 12;
    boolean hours = true;

    int hour = 0;
    int min = 0;

    float dyMin;
    float dyHour;
    float dText;

    float minTextSize;
    float hourTextSize;

    float center;

    float fullRadius;
    float mediumRadius;
    float medium2Radius;
    float centerRadius;

    float highlightHourRadius;
    float highlightMinRadius;

    float firstHourRadius;
    float secondHourRadius;
    float minRadius;

    float expandPickerRadius;
    float expand2PickerRadius;
    float delta;
    float animProgress;

    PointF[] points = new PointF[12];

    ValueAnimator animator;

    int colorBold;
    int colorNormal;

    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        if(!hours) swap();
        invalidate();
    }

    public interface OnTimeChangedListener {
        void onChange(int h, int m);
    }

    private OnTimeChangedListener onTimeChangedListener;

    public void setOnTimeChangedListener(OnTimeChangedListener onTimeChangedListener) {
        this.onTimeChangedListener = onTimeChangedListener;
    }

    public interface OnHMChangedListener {
        void onChange(boolean hours);
    }

    private OnHMChangedListener onHMChangedListener;

    public void setOnHMChangedListener(OnHMChangedListener onHMChangedListener) {
        this.onHMChangedListener = onHMChangedListener;
    }

    public ActionTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        chooserPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        chooserPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.CENTER);

        hmTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        hmTextPaint.setTextAlign(Paint.Align.CENTER);

        float m = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 1,
                getResources().getDisplayMetrics());

        minTextSize = 20 * m;
        hourTextSize = 18 * m;
        hmTextPaint.setTextSize(19 * m);

        Rect r = new Rect();
        dyMin = configureText(minTextSize, r);
        dyHour = configureText(hourTextSize, r);

        hmTextPaint.getTextBounds("hour/min", 0, 2, r);
        dText = r.height() / 2;

        for (int i = 0; i < 12; i++) {
            points[i] = new PointF((float) Math.cos(Math.toRadians(i * 30 - 90)),
                    (float) Math.sin(Math.toRadians(i * 30 - 90)));
        }

        animator = ValueAnimator.ofFloat(1, 0).setDuration(200);
        animator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                hours = !hours;
                if(onHMChangedListener!=null){
                    onHMChangedListener.onChange(hours);
                }
                invalidate();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                invalidate();
            }
        });
        animator.addUpdateListener(valueAnimator -> {
            animProgress = (float) valueAnimator.getAnimatedValue();
            invalidate();
        });

        colorNormal = Color.parseColor("#9B9FC2");//ContextCompat.getColor(getContext(), R.color.colorPrimary);
        colorBold = ContextCompat.getColor(getContext(), R.color.colorPrimary);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        float width = right - left;

        center = width / 2;
        fullRadius = width / 2;

        mediumRadius = fullRadius * 0.7f;
        medium2Radius = fullRadius * 0.65f;
        centerRadius = fullRadius * 0.4f;
        highlightHourRadius = fullRadius * 0.13f;
        highlightMinRadius = fullRadius * 0.15f;

        firstHourRadius = (fullRadius + mediumRadius) / 2;
        secondHourRadius = (mediumRadius + centerRadius) / 2;
        minRadius = (medium2Radius + fullRadius) / 2;

        delta = fullRadius * 0.05f;

        expandPickerRadius = medium2Radius - centerRadius;
        expand2PickerRadius = mediumRadius - medium2Radius;
        post(this::invalidate);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawPicker(canvas);

        drawHours(canvas);
        drawMinutes(canvas);

        paint.setColor(Color.parseColor("#f5f5f5"));
        canvas.drawCircle(center, center, centerRadius + expandPickerRadius * animProgress, paint);

        hmTextPaint.setFakeBoldText(false);
        hmTextPaint.setColor(colorNormal);
        hmTextPaint.setTextAlign(Paint.Align.CENTER);
        float w = hmTextPaint.measureText("/");
        canvas.drawText("/", center, center + dText, hmTextPaint);

        hmTextPaint.setFakeBoldText(hours);
        hmTextPaint.setColor(hours ? colorBold : colorNormal);
        hmTextPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("hour", center - w / 2, center + dText, hmTextPaint);

        hmTextPaint.setFakeBoldText(!hours);
        hmTextPaint.setTextAlign(Paint.Align.LEFT);
        hmTextPaint.setColor(!hours ? colorBold : colorNormal);
        canvas.drawText("min", center + w / 2, center + dText, hmTextPaint);

        if(!active)canvas.drawCircle(center, center, centerRadius + expandPickerRadius * animProgress, chooserPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(!active) return false;

        if (animator.isRunning()) return true;
        float dx = event.getX() - center;
        float dy = event.getY() - center;
        float r = (float) Math.sqrt(Math.pow(dy, 2) + Math.pow(dx, 2));

        if (event.getAction() == MotionEvent.ACTION_DOWN && isInCenter(r)) {
            swap();
            invalidate();
            return false;
        } else if (event.getAction() == MotionEvent.ACTION_UP && hours) {
            invalidate();
            return false;
        } else if (!isInPicker(r) || isInCenter(r)) {
            return true;
        }

        int choosed = getSector(dx, dy) + (hours ? getHourRow(r) * 12 : 0);

        if (hours) {
            hour = choosed;
        } else {
            min = choosed;
        }

        if (onTimeChangedListener != null) {
            onTimeChangedListener.onChange(hour, min * 5);
        }

        invalidate();
        return true;
    }

    private float configureText(float size, Rect r) {
        textPaint.setTextSize(size);
        textPaint.getTextBounds("00", 0, 2, r);
        return r.height() / 2;
    }

    private void drawPicker(Canvas canvas) {
        paint.setColor(Color.parseColor("#eeeeee"));
        canvas.drawCircle(center, center, fullRadius, paint);

        paint.setColor(Color.parseColor("#e0e0e0"));
        canvas.drawCircle(center, center, mediumRadius - expand2PickerRadius * animProgress, paint);
    }

    private void drawHours(Canvas canvas) {
        textPaint.setTextSize(hourTextSize);
        float r = secondHourRadius;
        for (int i = 1; i < 25; i++) {
            if (i == 13) r = firstHourRadius;
            drawHour(canvas, i, r);
        }
    }

    private void drawMinutes(Canvas canvas) {
        textPaint.setTextSize(minTextSize);
        for (int i = 0; i < 12; i++) {
            drawMinute(canvas, i, minRadius);
        }
    }

    private void drawMinute(Canvas canvas, int i, float radius) {

        PointF point = new PointF((float) Math.cos(Math.toRadians(i * 30 - 90 - 15 * (1 - animProgress))),
                (float) Math.sin(Math.toRadians(i * 30 - 90 - 15 * (1 - animProgress))));

        float x = point.x * radius + center;
        float y = point.y * radius + center;

        int alpha = 255;

        alpha *= animProgress;

        if (min == i && active) {
            canvas.drawCircle(x, y, animProgress * highlightMinRadius, chooserPaint);
            textPaint.setColor(Color.WHITE);
        } else textPaint.setColor(Color.BLACK);
        textPaint.setAlpha(alpha);
        canvas.drawText((i == 0 || i == 1 ? "0" : "") + String.valueOf(i * 5), x, y + dyMin, textPaint);
    }

    private void drawHour(Canvas canvas, int i, float radius) {

        PointF point = new PointF((float) Math.cos(Math.toRadians(i * 30 - 90 + (i > 12 ? 15 * animProgress : 0))),
                (float) Math.sin(Math.toRadians(i * 30 - 90 + 15 * (i > 12 ? animProgress : 0))));

        float x = point.x * radius + center;
        float y = point.y * radius + center;

        float ax = i < 13 ? -points[i % 12].x * delta : 0;
        float ay = i < 13 ? -points[i % 12].y * delta : 0;
        int alpha = 255;

        if (i < 13) {
            ax *= 1.5f;
            ay *= 1.5f;
        }

        ax *= animProgress;
        ay *= animProgress;
        alpha *= 1 - animProgress;

        if (hour == i && active) {
            canvas.drawCircle(x, y, (1 - animProgress) * highlightHourRadius, chooserPaint);
            textPaint.setColor(Color.WHITE);
        } else textPaint.setColor(Color.BLACK);
        textPaint.setAlpha(alpha);
        canvas.drawText(String.valueOf(i), x + ax, y + ay + dyHour, textPaint);
    }

    private void swap() {
        if (hours) {
            animator.setFloatValues(0, 1);
            animator.start();
        } else {
            animator.setFloatValues(1, 0);
            animator.start();
        }
    }

    private int getSector(float dx, float dy) {
        float angle = (float) Math.toDegrees(Math.atan(dy / dx)) + (dx > 0 ? 90 : 270);
        int i = (int) ((angle + 15) / 30);
        if (hours) {
            if (i == 0) i = 12;
        } else if (i == 12) i = 0;
        return i;
    }

    private int getHourRow(float r) {
        return r > mediumRadius ? 1 : 0;
    }

    private boolean isInCenter(float r) {
        return hours && r < centerRadius || !hours && r < medium2Radius;
    }

    private boolean isInPicker(float r) {
        return r < fullRadius;
    }

    public void setTime(int h, int m) {
        if (h == 0) h = 24;
        min = Math.round((float)m / 5);
        hour = h;
//        choosed = hours ? h : m;
        invalidate();
    }

}
