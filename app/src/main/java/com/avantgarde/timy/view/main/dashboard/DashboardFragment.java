package com.avantgarde.timy.view.main.dashboard;

import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.avantgarde.timy.R;
import com.avantgarde.timy.database.Database;
import com.avantgarde.timy.model.Dash;
import com.avantgarde.timy.view.SwipeCallback;
import com.avantgarde.timy.database.OfflineDatabase;
import com.avantgarde.timy.view.main.MainActivity;
import com.avantgarde.timy.viewmodel.DashboardViewModel;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DashboardFragment extends Fragment {

    @BindView(R.id.active_recycler)
    RecyclerView activeRecycler;
    @BindView(R.id.suggested_recycler)
    RecyclerView suggestedRecycler;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private ActiveDashesAdapter activeDashesAdapter;
    private DashboardViewModel dashboardViewModel;
    private OfflineDatabase offlineDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nav_dashboard, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        offlineDatabase = Room.databaseBuilder(getContext(),
                OfflineDatabase.class, "offlineDatabase")
                .allowMainThreadQueries()
                .build();

        ((MainActivity) getActivity()).setSupportActionBar(toolbar);

        activeDashesAdapter = new ActiveDashesAdapter();
        activeRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        activeRecycler.setAdapter(activeDashesAdapter);

        SwipeCallback swipeCallback = new SwipeCallback(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);

        swipeCallback.configureLeft(Color.parseColor("#388E3C"),R.drawable.ic_complete_white);
        swipeCallback.configureRight(Color.parseColor("#FBC02D"),R.drawable.ic_edit_white);

        swipeCallback.attach(activeRecycler);
        swipeCallback.setOnSwipeListener((direction, position) -> {
            if (direction == ItemTouchHelper.LEFT) {
                Log.d("DashRecycler", "init: LEFT" + position);
            } else {
                Log.d("DashRecycler", "init: RIGHT" + position);
            }
        });

        dashboardViewModel = ViewModelProviders.of(getActivity()).get(DashboardViewModel.class);
        dashboardViewModel.getDashes().observe(this, dashes -> {
            Log.d("DashboardFragment", "onActivityCreated: "+dashes);
            activeDashesAdapter.setDashes(dashes);
        });

        dashboardViewModel.getNewDash().observe(this, dash -> {
            Log.d("DashboardFragment", "onActivityCreated: "+dash);
        });

        dashboardViewModel.loadDashes();

        CardView cardView = getActivity().findViewById(R.id.active_card);
        cardView.setPreventCornerOverlap(false);
        cardView.setUseCompatPadding(true);
        cardView.post(()->{

            float d2 = (float) ((cardView.getHeight() -cardView.getMaxCardElevation()*1.5f - (1-Math.cos(Math.toRadians(45)))*cardView.getHeight()/2)/2);
            cardView.setRadius(d2);

        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_dashboard,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.plans){
            Log.d("DashboardFragment", "onOptionsItemSelected: PLANS");
        } else {
            Database.getInstance().clear();
            Log.d("DashboardFragment", "onOptionsItemSelected: COMPLETED");

        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab_add)
    void add() {
        getFragmentManager().beginTransaction()
                .add(R.id.container_full,new NewActionFragment())
                .commit();
    }
}