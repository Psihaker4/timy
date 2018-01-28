package com.avantgarde.timy.database.task;

import android.util.Log;

import com.avantgarde.timy.database.OfflineDatabase;
import com.avantgarde.timy.model.Action;

public class FindActionTask extends DatabaseTask<String, Action> {

    public FindActionTask(OfflineDatabase offlineDatabase) {
        super(offlineDatabase);
    }

    @Override
    protected Action doInBackground(String... strings) {
        try {
            return database.actionDao().find(strings[0]);
        } catch (Exception e) {
            Log.d("ERROR", "doInBackground: "+e.getMessage());
            return null;
        }
    }
}
