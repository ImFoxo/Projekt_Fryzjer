package com.example.projektfryzjer.Daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.projektfryzjer.Models.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> all();
    @Query("SELECT * FROM user WHERE user_id in (:findUserId)")
    User findById(int findUserId);
    @Query("SELECT * FROM user WHERE username in (:username)")
    User findByUsername(String username);
    @Insert
    void insertALL(User... users);
    @Insert
    void insertUser(User user);
    @Delete
    void deleleUser(User user);
}