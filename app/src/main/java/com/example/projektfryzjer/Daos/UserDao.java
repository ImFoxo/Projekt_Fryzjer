package com.example.projektfryzjer.Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.projektfryzjer.Models.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Insert
    void insertALL(User... users);

    @Query("SELECT * FROM user WHERE user_id in (:findUserId)")
    User findById(int findUserId);

    @Query("SELECT * FROM user WHERE username in (:username)")
    User findByUsername(String username);

    @Query("SELECT * FROM user")
    LiveData<List<User>> findAll();

    @Query("SELECT * FROM user")
    List<User> findAllToList();
}