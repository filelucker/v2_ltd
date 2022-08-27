package com.example.v2_ltd.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import com.example.v2_ltd.database.entity.SaveData;

import java.util.List;

@Dao
public interface DataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SaveData about);

    @Query("SELECT * FROM SaveData")
    List<SaveData> findAll();

}
