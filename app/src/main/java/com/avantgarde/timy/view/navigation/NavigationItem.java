package com.avantgarde.timy.view.navigation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.avantgarde.timy.R;

@SuppressLint("ViewConstructor")
public class NavigationItem extends FrameLayout implements View.OnClickListener {

    private OnSelectListener onSelectListener;
    private int position;
    private boolean selected;

    private ImageView icon;
    private View ripple;

    private float radius;

    public NavigationItem(NavigationBar navBar, @DrawableRes int iconId, int position) {
        super(navBar.getContext());
        this.position = position;

        Context context = navBar.getContext();

        LayoutInflater.from(context).inflate(R.layout.navigation_item, this, true);
        icon = findViewById(R.id.icon);
        ripple = findViewById(R.id.ripple);
        icon.setImageResource(iconId);
        setOnClickListener(this);
        navBar.addView(this);

        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1;
        setLayoutParams(layoutParams);
        post(() -> radius = (float) Math.sqrt(getWidth() * getWidth() / 4 + getHeight() * getHeight() / 4)/ripple.getWidth()*2);
    }

    @Override
    public void onClick(View view) {
        onSelectListener.onSelect(this);
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        if (this.selected == selected) return;
        this.selected = selected;
        post(() -> ripple.animate()
                .scaleY(selected ? radius : 0)
                .scaleX(selected ? radius : 0)
                .setDuration(200));
    }

    public int getPosition() {
        return position;
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    public interface OnSelectListener {
        void onSelect(NavigationItem item);
    }

}
