package com.example.projektfryzjer.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.projektfryzjer.Daos.UserDao;
import com.example.projektfryzjer.Database.MyDatabase;
import com.example.projektfryzjer.Models.User;

import java.util.List;

public class UserRepo {
    private final UserDao userDao;
    private final LiveData<List<User>> users;

    public UserRepo(Application application)
    {
        MyDatabase dataBase = MyDatabase.getDatabaseInstance(application);
        userDao = dataBase.userDao();
        users = userDao.findAll();
    }

    public LiveData<List<User>> findAllUsers() { return users; }

    public void insert(User user) {
        MyDatabase.databaseWriteExecutor.execute(() -> userDao.insert(user));
    }

    public void update(User user) {
        MyDatabase.databaseWriteExecutor.execute(() -> userDao.update(user));
    }

    public void delete(User user) {
        MyDatabase.databaseWriteExecutor.execute(() -> userDao.delete(user));
    }
}
