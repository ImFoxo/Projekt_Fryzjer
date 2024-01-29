package com.example.projektfryzjer.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.projektfryzjer.Models.User;
import com.example.projektfryzjer.Repositories.UserRepo;

import java.util.List;

public class UserViewModel extends AndroidViewModel {
    private final UserRepo userRepo;
    private final LiveData<List<User>> users;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepo = new UserRepo(application);
        users = userRepo.findAllUsers();
    }

    public LiveData<List<User>> findAll() { return users; }

    public void insert(User user) { userRepo.insert(user); }

    public void update(User user) { userRepo.update(user); }

    public void delete(User user) { userRepo.delete(user); }
}
