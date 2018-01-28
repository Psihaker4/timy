package com.avantgarde.timy.database.task;

import android.util.Log;

import com.avantgarde.timy.database.OfflineDatabase;
import com.avantgarde.timy.model.Dash;

public class AddDashTask extends DatabaseTask<Dash, Dash> {

    public AddDashTask(OfflineDatabase offlineDatabase) {
        super(offlineDatabase);
    }

    @Override
    protected Dash doInBackground(Dash... dashes) {
        try{
            database.dashDao().insertDash(dashes[0]);
            return dashes[0];
        }catch (Exception e){
            Log.d("ERROR", "doInBackground: "+e.getMessage());
            return null;
        }
    }
}
