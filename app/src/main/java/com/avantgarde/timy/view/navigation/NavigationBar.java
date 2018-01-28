package com.avantgarde.timy.view.navigation;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class NavigationBar extends LinearLayout {

    private OnNavItemSelectedListener onNavItemSelectedListener;

    private NavigationItem[] navItems;

    public NavigationBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void configure(@DrawableRes int[] itemIds, int defaultSelected) {
        navItems = new NavigationItem[itemIds.length];
        for (int i = 0; i < itemIds.length; i++) {
            navItems[i] = new NavigationItem(this, itemIds[i], i);
            navItems[i].setOnSelectListener(item -> {
                if (item.isSelected()) return;

                for (int j = 0; j < navItems.length; j++) {
                    navItems[j].setSelected(j == item.getPosition());
                }

                onNavItemSelectedListener.onNavItemSelected(item.getPosition());
            });
        }
        navItems[defaultSelected].setSelected(true);
    }

    public void setOnNavItemSelectedListener(OnNavItemSelectedListener onNavItemSelectedListener) {
        this.onNavItemSelectedListener = onNavItemSelectedListener;
    }

    public interface OnNavItemSelectedListener {
        void onNavItemSelected(int position);
    }

}
