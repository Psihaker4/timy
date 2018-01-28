package com.avantgarde.timy.view.main.dashboard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avantgarde.timy.R;
import com.avantgarde.timy.view.main.ActionTimePicker;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewActionFragment extends Fragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.time_picker)
    ActionTimePicker timePicker;

    @BindView(R.id.name_card)
    CardView nameCard;
    @BindView(R.id.category_card)
    CardView categoryCard;

    @BindView(R.id.from_card)
    TimeCardView fromCard;
    @BindView(R.id.to_card)
    TimeCardView toCard;

    @BindView(R.id.duration)
    TextView durationText;

    private boolean fromTime;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_action, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Calendar now = Calendar.getInstance();
        timePicker.setTime(now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE));
        fromCard.setDecor(now.get(Calendar.HOUR_OF_DAY),now.get(Calendar.MINUTE));
        fromCard.activate();

        fromCard.setOnStateChangedListener(active -> {

            if (toCard.isActive()) toCard.setActive(false);

            timePicker.setTime(fromCard.h, fromCard.m);
            timePicker.setActive(active);
            fromTime = true;

        });

        toCard.setOnStateChangedListener(active -> {

            if (fromCard.isActive()) fromCard.setActive(false);

            if (toCard.getTime() == null) {
                toCard.setTime(fromCard.h,fromCard.m);
                toCard.setTimeVisible(true);
            }

            timePicker.setTime(toCard.h, toCard.m);
            timePicker.setActive(active);
            fromTime = false;

        });

        timePicker.setOnTimeChangedListener((h, m) -> {
            Log.d("NewActionFragment", "onActivityCreated: " + h + ":" + m);
            if (fromTime) {
                fromCard.setDecor(h, m);
            } else {
                toCard.setDecor(h, m);
            }

            int mins = toCard.getMinutes() - fromCard.getMinutes();
            if (mins < 0) {
                durationText.setText("WRONG");
                return;
            }

            durationText.setText(mins / 60 + "h " + mins % 60 + "m");

        });
        timePicker.setOnHMChangedListener(hours -> {
            Log.d("NewActionFragment", "onActivityCreated: " + hours);
            if(fromTime){
                fromCard.changeTime(hours);
            } else{
                toCard.changeTime(hours);
            }
        });
        nameCard.post(() -> {
            float r = nameCard.getHeight() / 2;
            nameCard.setRadius(r);
            categoryCard.setRadius(r);

            float r2 = fromCard.getHeight() / 2;
            fromCard.setRadius(r2);
            toCard.setRadius(r2);
        });

    }

    @OnClick(R.id.name_card)
    void nameOpen(){
        fromCard.setTimeVisible(true);
    }
    
    @OnClick(R.id.category_card)
    void categoryOpen(){

    }

    @OnClick(R.id.fab_comments)
    void commentsOpen(){
        Log.d("NewActionFragment", "commentsOpen: ");
    }

    @OnClick(R.id.close)
    void close() {
        Log.d("NewActionFragment", "close: ");
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .remove(this)
                .commit();
    }

    @OnClick(R.id.apply)
    void apply() {
        Log.d("NewActionFragment", "apply: added");
        close();
    }
}