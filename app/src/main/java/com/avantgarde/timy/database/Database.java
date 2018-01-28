package com.avantgarde.timy.database;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.util.Log;

import com.avantgarde.timy.App;
import com.avantgarde.timy.database.task.AddDashTask;
import com.avantgarde.timy.database.task.FindActionTask;
import com.avantgarde.timy.database.task.LoadActionsTask;
import com.avantgarde.timy.database.task.LoadCategoriesTask;
import com.avantgarde.timy.model.Action;
import com.avantgarde.timy.model.Category;
import com.avantgarde.timy.model.Dash;
import com.avantgarde.timy.database.task.LoadDashesTask;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Database {

    private static Database instance;
    private Database() {
        offlineDatabase = Room.databaseBuilder(App.get(),
                OfflineDatabase.class, "database")
                .build();

        new LoadActionsTask(offlineDatabase)
                .setOnCompleteListener(result -> {
                    actions = result;
                    Log.d("Database", "Database: "+result);
                })
                .execute();

        new LoadCategoriesTask(offlineDatabase)
                .setOnCompleteListener(result -> {
                    categories = result;
                    Log.d("Database", "Database: "+result);
                })
                .execute();

    }
    public static Database getInstance() {
        return instance == null ? instance = new Database() : instance;
    }

    private OfflineDatabase offlineDatabase;
    private Map<String,List<Action>> actions;
    private List<Category> categories;

    public void findAction(String name, OnCompleteListener<Action> completeListener){
        new FindActionTask(offlineDatabase)
                .setOnCompleteListener(completeListener)
                .execute(name);
    }

    public void addDash(Dash dash, OnCompleteListener<Dash> completeListener){
        new AddDashTask(offlineDatabase)
                .setOnCompleteListener(completeListener)
                .execute(dash);
    }

    public void getDashesAfter(Date date, OnCompleteListener<List<Dash>> completeListener) {
        new LoadDashesTask(offlineDatabase)
                .setOnCompleteListener(completeListener)
                .execute(date);
    }

    public void getDashes(OnCompleteListener<List<Dash>> completeListener) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Log.d("Database", "getDashes: "+calendar.getTime());
        getDashesAfter(calendar.getTime(),completeListener);
    }

    @SuppressLint("StaticFieldLeak")
    public void clear(){
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                offlineDatabase.categoryDao().clear();
                offlineDatabase.actionDao().clear();
                offlineDatabase.dashDao().clear();
                Log.d("Database", "doInBackground: CLEAR");
                return null;
            }
        }.execute();
    }

}
