package com.avantgarde.timy.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.avantgarde.timy.model.Category;

import java.util.List;

@Dao
public interface CategoryDao {

    @Insert
    void insertCategory(Category category);

    @Query("SELECT * FROM category")
    List<Category> loadAll();

    @Query("SELECT * FROM category WHERE name LIKE :name LIMIT 1")
    Category find(String name);

    @Query("DELETE FROM category")
    void clear();
}
