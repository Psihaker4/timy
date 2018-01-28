package com.avantgarde.timy.database.task;

import android.util.Log;

import com.avantgarde.timy.database.OfflineDatabase;
import com.avantgarde.timy.model.Action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadActionsTask extends DatabaseTask<Void, Map<String,List<Action>>> {

    public LoadActionsTask(OfflineDatabase offlineDatabase) {
        super(offlineDatabase);
    }

    @Override
    protected Map<String,List<Action>> doInBackground(Void... voids) {
        try {
            List<Action> actionList = database.actionDao().loadAll();
            Map<String, List<Action>> actionMap = new HashMap<>();
            for(Action action : actionList) {
                if (!actionMap.containsKey(action.getCategory())) {
                    actionMap.put(action.getCategory(), new ArrayList<>());
                }
                actionMap.get(action.getCategory()).add(action);
            }

            return actionMap;
        } catch (Exception e) {
            Log.d("ERROR", "doInBackground: " + e.getMessage());
            return null;
        }
    }

}