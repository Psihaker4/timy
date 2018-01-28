package com.avantgarde.timy.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.avantgarde.timy.model.Action;

import java.util.List;
@Dao
public interface ActionDao {

    @Insert
    void insertAction(Action action);

    @Query("SELECT * FROM 'action'")
    List<Action> loadAll();

    @Query("SELECT name FROM 'action' WHERE category = :category")
    List<String> loadAll(String category);

    @Query("SELECT * FROM 'action' WHERE name LIKE :name LIMIT 1")
    Action find(String name);

    @Query("DELETE FROM 'action'")
    void clear();

}