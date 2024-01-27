package com.example.projektfryzjer.Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.projektfryzjer.Models.Employee;

import java.util.List;

@Dao
public interface EmployeeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Employee employee);

    @Update
    void update(Employee employee);

    @Delete
    void delete(Employee employee);

    @Query("DELETE FROM employee")
    void deleteAll();

    @Query("SELECT * FROM employee ORDER BY empSurname")
    LiveData<List<Employee>> findAll();

    @Query("SELECT * FROM employee ORDER BY empSurname")
    List<Employee> findAllToList();
}
