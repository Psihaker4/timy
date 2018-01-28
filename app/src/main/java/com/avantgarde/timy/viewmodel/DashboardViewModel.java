package com.avantgarde.timy.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.util.Log;
import android.view.View;

import com.avantgarde.timy.model.Dash;
import com.avantgarde.timy.database.Database;

import java.util.List;

public class DashboardViewModel extends ViewModel {

    private Database database;
    private MutableLiveData<List<Dash>> dashes;
    private MutableLiveData<Dash> newDash;

    public DashboardViewModel() {
        database = Database.getInstance();
        dashes = new MutableLiveData<>();
        newDash = new MutableLiveData<>();
    }

    public MutableLiveData<List<Dash>> getDashes() {
        return dashes;
    }

    public MutableLiveData<Dash> getNewDash() {
        return newDash;
    }

    public void loadDashes() {
        Database.getInstance().getDashes(result -> {
            Log.d("DashboardViewModel", "loadDashes: " + result.size() + "  " + result);
            dashes.postValue(result);
        });
    }

    public void addDash(Dash dash) {
        database.addDash(dash, result -> {
            newDash.postValue(result);
            Log.d("DashboardViewModel", "addDash: " + result);
        });
    }

}
