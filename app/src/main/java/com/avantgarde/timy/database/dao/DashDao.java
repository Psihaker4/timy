package com.avantgarde.timy.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.avantgarde.timy.model.Dash;

import java.util.Date;
import java.util.List;

@Dao
public interface DashDao {

    @Insert
    void insertDash(Dash dash);

    @Query("SELECT * FROM dash")
    List<Dash> loadAll();

    @Query("SELECT * FROM dash WHERE startTime > :nowTime")
    List<Dash> loadAfter(Date nowTime);

    @Query("SELECT * FROM dash WHERE id LIKE :id LIMIT 1")
    Dash find(int id);

    @Query("DELETE FROM dash")
    void clear();

}

