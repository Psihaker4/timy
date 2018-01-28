package com.avantgarde.timy.database.task;

import android.util.Log;

import com.avantgarde.timy.model.Dash;
import com.avantgarde.timy.database.OfflineDatabase;

import java.util.Date;
import java.util.List;

public class LoadDashesTask extends DatabaseTask<Date, List<Dash>> {

    public LoadDashesTask(OfflineDatabase offlineDatabase) {
        super(offlineDatabase);
    }

    @Override
    protected List<Dash> doInBackground(Date... dates) {
        try {
            return database.dashDao().loadAfter(dates[0]);
        } catch (Exception e) {
            Log.d("ERROR", "doInBackground: "+e.getMessage());
            return null;
        }
    }

}
