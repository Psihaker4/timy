package com.avantgarde.timy.view.main.dashboard;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avantgarde.timy.R;
import com.avantgarde.timy.database.Database;
import com.avantgarde.timy.model.Dash;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActiveDashLayout extends RelativeLayout implements View.OnClickListener {

    private static int MAX_PROGRESS = 150;

    @BindView(R.id.name)
    TextView nameText;
    @BindView(R.id.duration)
    TextView durationText;
    @BindView(R.id.category)
    TextView category;

    @BindView(R.id.progress_0)
    View progress0;
    @BindView(R.id.progress_1)
    View progress1;

    private Dash dash;

    public ActiveDashLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void bind(View v) {
        ButterKnife.bind(v, this);
        setOnClickListener(this);
    }

    public void configure(Dash dash) {
        this.dash = dash;
        durationText.setText(dash.getStringDuration(Calendar.getInstance().getTime()));
        nameText.setText(dash.getName());
        category.setText(dash.getCategory());
        DrawableCompat.setTint(category.getCompoundDrawablesRelative()[2], Color.GREEN);
    }

    public void onClick(View view) {
        Log.d("ActiveDashLayout", "onClick: ");
    }
}
