package com.avantgarde.timy.database;

import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.avantgarde.timy.model.Action;
import com.avantgarde.timy.model.Category;
import com.avantgarde.timy.model.Dash;
import com.avantgarde.timy.database.dao.ActionDao;
import com.avantgarde.timy.database.dao.CategoryDao;
import com.avantgarde.timy.database.dao.DashDao;

@android.arch.persistence.room.Database(entities = {Action.class, Category.class, Dash.class},version = 1,exportSchema = false)
@TypeConverters(Converters.class)
public abstract class OfflineDatabase extends RoomDatabase {

    public abstract ActionDao actionDao();

    public abstract CategoryDao categoryDao();

    public abstract DashDao dashDao();

}
