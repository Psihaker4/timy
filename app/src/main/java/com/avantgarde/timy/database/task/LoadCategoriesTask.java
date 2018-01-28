package com.avantgarde.timy.database.task;

import android.util.Log;

import com.avantgarde.timy.database.OfflineDatabase;
import com.avantgarde.timy.model.Category;

import java.util.List;

public class LoadCategoriesTask extends DatabaseTask<Void, List<Category>> {

    public LoadCategoriesTask(OfflineDatabase offlineDatabase) {
        super(offlineDatabase);
    }

    @Override
    protected List<Category> doInBackground(Void... voids) {
        try {
            return database.categoryDao().loadAll();
        }catch (Exception e){
            Log.d("ERROR", "doInBackground: "+e.getMessage());
            return null;
        }
    }
}
