package com.example.testprep_dovada.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.testprep_dovada.data.Dovada;

import java.util.List;

@Dao
public interface DovadaDao {
    @Query("SELECT * FROM dovezi")
    List<Dovada> getAll();

    @Insert
    List<Long> insertAll(List<Dovada> dovezi);

    @Delete
    int delete(List<Dovada> dovezi);
}
