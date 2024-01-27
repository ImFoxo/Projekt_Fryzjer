package com.example.projektfryzjer.Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.projektfryzjer.Models.Appointment;
//import com.example.projektfryzjer.Models.AppointmentWithClientAndEmployee;

import java.util.List;

@Dao
public interface AppointmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Appointment appointment);

    @Update
    void update(Appointment appointment);

    @Delete
    void delete(Appointment appointment);

    @Query("DELETE FROM appointment")
    void deleteAll();

    @Query("SELECT * FROM appointment")
    LiveData<List<Appointment>> findAll();

    @Query("SELECT * FROM appointment")
    List<Appointment> findAllToList();
}
