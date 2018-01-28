package com.avantgarde.timy.view.main.dashboard;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avantgarde.timy.R;
import com.avantgarde.timy.model.Dash;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActiveDashesAdapter extends RecyclerView.Adapter<ActiveDashesAdapter.DashHolder> {

    private List<Dash> dashes;

    public ActiveDashesAdapter() {
        dashes = new ArrayList<>();
    }

    class DashHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.action)
        ActiveDashLayout activeDashLayout;

        DashHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            activeDashLayout.bind(itemView);
        }
    }

    @Override
    public DashHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.active_dash, parent, false);
        return new DashHolder(v);
    }

    @Override
    public void onBindViewHolder(DashHolder holder, int position) {
        holder.activeDashLayout.configure(dashes.get(position));
    }

    @Override
    public int getItemCount() {
        return dashes.size();
    }

    public void setDashes(List<Dash> dashes) {
        this.dashes = dashes;
        Collections.sort(dashes,(a0,a1)-> ((Float)a0.getDuration(Calendar.getInstance().getTime())).compareTo(a1.getDuration(Calendar.getInstance().getTime())));
        notifyDataSetChanged();
    }
}
