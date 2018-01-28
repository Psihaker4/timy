package com.avantgarde.timy.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.avantgarde.timy.R;

import java.util.ArrayList;
import java.util.List;

public class CategoriesColorsAdapter extends RecyclerView.Adapter<CategoriesColorsAdapter.ColorHolder> {

    private List<Integer> categories = new ArrayList<>();
    private ColorHolder choosedHolder;
    private int choosedPosition = -1;

    private OnNewColorListener onNewColorListener;

    private OnStateChangeListener onStateChangeListener = (holder, position) -> {
        if (position == choosedPosition) {
            if (onNewColorListener == null) return;
            onNewColorListener.onNewColor(categories.get(position));

            return;
        }

        reset();
        holder.complete.setVisibility(View.VISIBLE);
        choosedHolder = holder;
        choosedPosition = position;
    };

    public interface OnNewColorListener{
        void onNewColor(int color);
    }

    private interface OnStateChangeListener {
        void onStateChanged(ColorHolder holder, int position);
    }

    class ColorHolder extends RecyclerView.ViewHolder {

        ImageView color;
        ImageView complete;

        ColorHolder(View itemView) {
            super(itemView);
            color = itemView.findViewById(R.id.color);
            complete = itemView.findViewById(R.id.complete);
            complete.setVisibility(View.GONE);
        }

    }

    public CategoriesColorsAdapter() {
        categories.add(Color.RED);
        categories.add(Color.GREEN);
        categories.add(Color.GRAY);
        categories.add(Color.YELLOW);
    }

    public void setOnNewColorListener(OnNewColorListener onNewColorListener) {
        this.onNewColorListener = onNewColorListener;
    }

    @Override
    public ColorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ColorHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.category_color, parent, false));
    }

    @Override
    public void onBindViewHolder(ColorHolder holder, int position) {
        int color = categories.get(position);
        DrawableCompat.setTint(holder.color.getDrawable(), color);
        holder.color.setOnClickListener(view ->
                onStateChangeListener.onStateChanged(holder, position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void reset() {
        if (choosedHolder == null) return;
        choosedHolder.complete.setVisibility(View.GONE);
        choosedPosition = -1;
    }

}
