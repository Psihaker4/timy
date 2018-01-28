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
import com.avantgarde.timy.model.Category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryHolder> {

    private Context context;
    private List<Category> categories = new ArrayList<>();
    private List<Category> searchCategories = new ArrayList<>();
    private CategoryHolder choosedHolder;
    private int choosedPosition = -1;

    private OnCategoryChoosedListener onCategoryChoosedListener;

    private OnStateChangeListener onStateChangeListener = (holder, position) -> {
        if (position == choosedPosition) {
            reset();
            if(onCategoryChoosedListener!=null){
                onCategoryChoosedListener.onChoosed(holder.category.getText().toString(), false);
            }
            return;
        }
        reset();
        if(onCategoryChoosedListener!=null){
            onCategoryChoosedListener.onChoosed(holder.category.getText().toString(), true);
        }
        int c = ContextCompat.getColor(context, R.color.colorAccent);
        DrawableCompat.setTint(holder.background.getBackground(), c);
        holder.category.setTextColor(c);
        choosedHolder = holder;
        choosedPosition = position;
    };

    public interface OnCategoryChoosedListener {
        void onChoosed(String category, boolean state);
    }

    private interface OnStateChangeListener {
        void onStateChanged(CategoryHolder holder, int position);
    }

    class CategoryHolder extends RecyclerView.ViewHolder {

        FrameLayout background;
        TextView category;
        ImageView color;

        CategoryHolder(View itemView) {
            super(itemView);
            background = itemView.findViewById(R.id.background);
            category = itemView.findViewById(R.id.category);
            color = itemView.findViewById(R.id.color);
            DrawableCompat.setTint(background.getBackground(), ContextCompat.getColor(context, R.color.colorPrimary));
        }

    }

    public CategoriesAdapter(Context context) {
        this.context = context;
        categories.add(new Category("HEY MAN", Color.RED));
        categories.add(new Category("Work", Color.GREEN));
        categories.add(new Category("Programming", Color.GRAY));
        categories.add(new Category("BTW", Color.YELLOW));
        categories.add(new Category("University", Color.CYAN));
        Collections.sort(categories, (c0, c1) -> c0.getName().compareTo(c1.getName()));
        searchCategories = new ArrayList<>(categories);
    }

    public void setOnCategoryChoosedListener(OnCategoryChoosedListener onCategoryChoosedListener) {
        this.onCategoryChoosedListener = onCategoryChoosedListener;
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.category, parent, false));
    }

    @Override
    public void onBindViewHolder(CategoryHolder holder, int position) {
        Category cat = categories.get(position);

        holder.category.setText(cat.getName());
        DrawableCompat.setTint(holder.color.getDrawable(), cat.getColor());
        holder.background.setOnClickListener(view ->
                onStateChangeListener.onStateChanged(holder, position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public boolean filter(String name) {
        for (Category category : searchCategories) {
            if (category.getName().toLowerCase().contains(name.toLowerCase())) {
                if (!categories.contains(category)) {
                    categories.add(category);
                    Collections.sort(categories, (c0, c1) -> c0.getName().compareTo(c1.getName()));
                    notifyItemInserted(categories.indexOf(category));
                }
            } else if (categories.contains(category)) {
                notifyItemRemoved(categories.indexOf(category));
                categories.remove(category);
            }
        }
        reset();
        return categories.size()==0;
    }

    public void reset() {
        if (choosedHolder == null) return;
        int c = ContextCompat.getColor(context, R.color.colorPrimary);
        DrawableCompat.setTint(choosedHolder.background.getBackground(), c);
                    choosedHolder.category.setTextColor(c);
        choosedPosition = -1;
    }

    public void add(Category category){
        searchCategories.add(category);
    }

}
