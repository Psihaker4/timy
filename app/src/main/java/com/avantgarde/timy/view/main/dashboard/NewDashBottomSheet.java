package com.avantgarde.timy.view.main.dashboard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.avantgarde.timy.R;
import com.avantgarde.timy.adapter.CategoriesAdapter;
import com.avantgarde.timy.adapter.CategoriesColorsAdapter;
import com.avantgarde.timy.model.Category;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewDashBottomSheet extends BottomSheetDialogFragment {

    @BindView(R.id.categories_recycler)
    RecyclerView categoriesRecycler;
    @BindView(R.id.colors_recycler)
    RecyclerView colorsRecycler;
    @BindView(R.id.name)
    EditText nameEdit;
    @BindView(R.id.category)
    EditText categoryEdit;
    @BindView(R.id.category_no)
    TextView noCategoryText;
    @BindView(R.id.category_add)
    ImageView categoryAddButton;


    CategoriesAdapter categoriesAdapter;
    CategoriesColorsAdapter colorsAdapter;

    boolean newCategory;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_new_action, container, false);
        Window window = getDialog().getWindow();

        if (window == null) return v;

        v.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect rect = new Rect();
            v.getWindowVisibleDisplayFrame(rect);

            int keyboardHeight = v.getRootView().getHeight() - rect.bottom;
            window.getDecorView().setPadding(0, 0, 0, keyboardHeight);

        });
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("NewDashBottomSheet", "onActivityCreated: ");
        categoriesAdapter = new CategoriesAdapter(getContext());
        categoriesRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoriesRecycler.setAdapter(categoriesAdapter);

        colorsAdapter = new CategoriesColorsAdapter();
        colorsRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        colorsRecycler.setAdapter(colorsAdapter);

        colorsAdapter.setOnNewColorListener(color -> {
            categoriesAdapter.add(new Category(categoryEdit.getText().toString(),color));
            addCategory();
        });

        categoriesAdapter.setOnCategoryChoosedListener((category,state) ->
                categoryEdit.setText(state?category:""));

        categoryEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!newCategory) {
                    boolean b = categoriesAdapter.filter(charSequence.toString());
                    noCategoryText.animate()
                            .alpha(b?1:0)
                            .setDuration(200)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    if(!b) noCategoryText.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onAnimationStart(Animator animation) {
                                    if(b) noCategoryText.setVisibility(View.VISIBLE);
                                }
                            });
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        nameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @OnClick(R.id.category_add)
    void addCategory() {
        newCategory = !newCategory;
        Log.d("NewDashBottomSheet", "addCategory: " + newCategory);

        categoriesRecycler.animate()
                .alpha(newCategory ? 0 : 1)
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (newCategory) {
                            categoriesRecycler.setVisibility(View.INVISIBLE);
                            categoriesAdapter.reset();
                        }
                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                        if (!newCategory) {
                            categoriesRecycler.setVisibility(View.VISIBLE);
                            boolean b = categoriesAdapter.filter(categoryEdit.getText().toString());
                            noCategoryText.animate()
                                    .alpha(b?1:0)
                                    .setDuration(200)
                                    .setListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            if(!b) noCategoryText.setVisibility(View.INVISIBLE);
                                        }

                                        @Override
                                        public void onAnimationStart(Animator animation) {
                                            if(b) noCategoryText.setVisibility(View.VISIBLE);
                                        }
                                    });
                        }
                    }
                });

        colorsRecycler.animate()
                .alpha(newCategory ? 1 : 0)
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (!newCategory) {
                            colorsRecycler.setVisibility(View.INVISIBLE);
                            colorsAdapter.reset();
                        }
                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                        if (newCategory) colorsRecycler.setVisibility(View.VISIBLE);
                    }
                });
        categoryAddButton.animate()
                .rotation(newCategory ? 45 : 0)
                .setDuration(200);
    }

}